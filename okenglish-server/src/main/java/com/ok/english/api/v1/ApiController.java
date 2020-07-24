package com.ok.english.api.v1;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ok.english.config.Config;
import com.ok.english.entity.*;
import com.ok.english.service.LogService;
import com.ok.english.service.WebService;
import com.ok.english.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("/api/v1")
public class ApiController {
    @Autowired
    WebService webService;

    @PostMapping("/user/login")
    public Message check(@RequestParam("name") String name, @RequestParam("pwd") String pwd, HttpServletResponse response) {
        User user = new User(name, pwd);
        Message message = webService.userLogin(user);
        if (message.getCode() == MessageCode.SUCCESS) {
            String token = CheckTool.jwtSign(user);
            String sessionId = JWT.create().withClaim("session", new Date().toString()).sign(Algorithm.HMAC512(user.getPwd()));
            User user1 = UserPool.getUser(user.getName());
            user1.setToken(token);
            UserPool.addUser(user.getName(), user1);
            SessionIDPool.setSessionID(user.getName(), sessionId);
            response.addHeader(Config.token_header_name, token);
            response.addCookie(new Cookie(Config.cookie_sid_name, sessionId));
            LogService.produceLog("userlogin:" + user.getName());
        }
        return message;
    }

    @PostMapping("/user/{name}/pwd/change")
    public Message changePwd(@PathVariable("name") String name, @RequestParam("pwd") String pwd, HttpServletRequest request) {
        if (CheckTool.tokenCheck(request, name)) {
            LogService.produceLog("pwdchange:" + name);
            MailSender.getSender().sendSimpleText("你于" + new Date() + "更改密码", UserPool.getUser(name).getEmail(), "OkEnglish新用户");
            return webService.changeUserPwd(UserPool.getUser(name), pwd);
        }
        return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "changPwd");
    }

    @PostMapping("/user/add")
    public Message addUser(HttpServletRequest request, @RequestParam("name") String name, @RequestParam("pwd") String pwd, @RequestParam("email") String email) {
        String s = "";
        if (!CheckTool.isRegist(name) && CheckTool.isEmailString(email)) {
            User user = new User(name, pwd, email);
            RestTemplate restTemplate = new RestTemplate();
            String ip = restTemplate.getForEntity(Config.ip_look_server + request.getRemoteAddr() + "?" + Config.ip_look_server_param, Ip.class).getBody().getCity();
            user.setIpRegion((ip != null ? ip : "其他"));
            Message message = webService.addUser(user);
            if (message.getCode().equals(MessageCode.SUCCESS)) {
                MailSender.getSender().sendSimpleText("OkEnglish谢谢你的使用", email, "OkEnglish新用户");
                NewUserPool.addUser(user.getName(), user);
            }
            return message;
        }
        return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, s);
    }

    @GetMapping("/user/{name}/record")
    public Message getWordRecord(@PathVariable("name") String name, HttpServletRequest request) {
        if (CheckTool.tokenCheck(request, name)) {
            return webService.getWordRecord(UserPool.getUser(name));
        }
        return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "getRecord");
    }

    @PostMapping("/user/{name}/record/sync")
    public Message syncRecord(@PathVariable("name") String name, @RequestParam("record") String record, HttpServletRequest request) {
        if (CheckTool.tokenCheck(request, name)) {
            return webService.syncWordRecord(UserPool.getUser(name), record);
        }
        return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "syncRecord");
    }

    @GetMapping("/user/{name}/plan")
    public Message getPlanBook(@PathVariable("name") String name, HttpServletRequest request) {
        if (CheckTool.tokenCheck(request, name)) {
            return webService.getWorkBookPlan(UserPool.getUser(name));
        }
        return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "getUserStatus");
    }

    @PostMapping("/user/{name}/plan/sync")
    public Message syncPlanBook(@PathVariable("name") String name, @RequestParam("plan") int plan, HttpServletRequest request) {
        if (CheckTool.tokenCheck(request, name)) {
            return webService.setWorkBookPlan(UserPool.getUser(name), plan);
        }
        return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "syncRecord");
    }

    @GetMapping("/user/{name}/status")
    public Message getUserstatus(@PathVariable("name") String name, HttpServletRequest request) {
        if (CheckTool.tokenCheck(request, name)) {
            return webService.getUserStatus(UserPool.getUser(name));
        }
        return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "getUserStatus");
    }

    @GetMapping("/user/{name}/dayword")
    public Message getDayWord(@PathVariable("name") String name, HttpServletRequest request, HttpServletResponse response) {
        if (CheckTool.tokenCheck(request, name)) {
            CheckTool.addAuthHeader(response, name);
            return webService.getDayWordPlan(UserPool.getUser(name));
        }
        return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "getDayWord");
    }

    @PostMapping("/user/{name}/dayword/change")
    public Message changeDayWord(@PathVariable("name") String name, @RequestParam("dayword") int dayword, HttpServletRequest request) {
        if (CheckTool.tokenCheck(request, name)) {
            return webService.setDayWordPlan(UserPool.getUser(name), dayword);
        }
        return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "setDayWord");
    }

    @PostMapping("/user/{name}/wordsize/sync")
    public Message changeWordSize(@PathVariable("name") String name, @RequestParam("size") int size, HttpServletRequest request) {
        if (CheckTool.tokenCheck(request, name)) {
            return webService.setWordSize(UserPool.getUser(name), size);
        }
        return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "setDayWord");
    }
}
