package com.ok.english.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ok.english.config.Config;
import com.ok.english.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.util.Date;

public class CheckTool {
    public static boolean tokenCheck(HttpServletRequest request, String name) {
        synchronized (request) {
            boolean returned = true;
            User user = UserPool.getUser(name);
            if (user == null) return false;
            try {
                String token = request.getHeader(Config.token_header_name);
                if (!user.getToken().equals(token)) returned = false;
                if (!(JWT.decode(token).getExpiresAt().getTime() - new Date().getTime() > 0)) {
                    user.setToken(jwtSign(user));
                    UserPool.addUser(user.getName(), user);
                }
            } catch (Exception e) {
                returned = false;
            }
            return returned;
        }
    }

    public static boolean isRegist(String name) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select name from user where name==?", name);
            if (rowSet.next()) return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isEmailString(String email) {
        return true;
    }

    public static String jwtSign(User user) {
        return JWT.create().withClaim("uid", user.getName()).withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 20)).sign(Algorithm.HMAC256(user.getPwd()));
    }

    public static String mdSign(String str) {
        String signedStr = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] buffer = digest.digest(str.getBytes());
            for (int i = 0, j; i < buffer.length; i++) {
                j = buffer[i];
                if (j < 0) j += 256;
                if (j < 16) signedStr += "0";
                signedStr += Integer.toHexString(j);
            }
        } catch (Exception e) {
            LogManager.getLogger().log(Level.ERROR, e.getMessage());
        }
        return signedStr;
    }

    public static boolean sessionCheck(HttpServletRequest request, String key, String expect) {
        HttpSession object = request.getSession(false);
        if (null != object && object.getAttribute(key).equals(expect)) {
            return true;
        }
        return false;
    }

    public static void addAuthHeader(HttpServletResponse response, String name) {
        User user = UserPool.getUser(name);
        if (user == null) return;
        response.addHeader(Config.token_header_name, user.getToken());
    }
}
