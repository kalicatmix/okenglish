package com.ok.english.service;

import com.ok.english.entity.*;
import com.ok.english.util.LogPool;
import com.ok.english.util.NewUserPool;
import com.ok.english.util.UserPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.function.Consumer;

@Service
@Transactional(isolation = Isolation.DEFAULT)
public class WebService implements AdminDao, UserDao {
    @Autowired()
    private JdbcTemplate jdbcTemplate;

    @Override
    public Message userLogin(User user) {
        String sql = "select * from user where name=?;";
        String info = "login";
        MessageCode code = MessageCode.ERROR;
        Object data = null;
        try {
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql, user.getName());
            if (row.next()) {
                if (row.getString("pwd").equals(user.getPwd()) && row.getInt("status") == User.STATUS_OK) {
                    code = MessageCode.SUCCESS;
                    User user1 = new User();
                    user1.setName(user.getName());
                    user1.setEmail(row.getString("email"));
                    user1.setDayWord(row.getInt("dayword"));
                    user1.setPlan(row.getInt("plan"));
                    UserPool.addUser(user.getName(), user1);
                    data = user1;
                }
            }
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addInfo(info).addData(data).build();
    }

    @Override
    public Message getWorkBookPlan(User user) {
        String sql = "select plan from user where name=?;";
        MessageCode code = MessageCode.ERROR;
        Object data = null;
        try {
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql, user.getName());
            if (row.next()) {
                code = MessageCode.SUCCESS;
                data = row.getInt("plan");
            }
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addData(data).build();
    }

    @Override
    public Message setWorkBookPlan(User user, int book) {
        String sql = "update user set plan=? where name=?;";
        String info = "setWordBook";
        MessageCode code = MessageCode.ERROR;
        try {
            int rowEffected = jdbcTemplate.update(sql, book, user.getName());
            if (rowEffected > 0) code = MessageCode.SUCCESS;
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addInfo(info).build();
    }

    @Override
    public Message changeUserPwd(User user, String pwdHash) {
        String sql = "update user set pwd=? where name=?;";
        String info = "setPwd";
        MessageCode code = MessageCode.ERROR;
        try {
            int rowEffected = jdbcTemplate.update(sql, pwdHash, user.getName());
            if (rowEffected > 0) code = MessageCode.SUCCESS;
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addInfo(info).build();
    }

    @Override
    public Message setDayWordPlan(User user, int wordNumber) {
        String sql = "update user set dayword=? where name=?;";
        String info = "setdayWord";
        MessageCode code = MessageCode.ERROR;
        try {
            int rowEffected = jdbcTemplate.update(sql, wordNumber, user.getName());
            if (rowEffected > 0) code = MessageCode.SUCCESS;
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addInfo(info).build();
    }

    @Override
    public Message getDayWordPlan(User user) {
        String sql = "select dayword from user where name=?;";
        MessageCode code = MessageCode.ERROR;
        Object data = null;
        try {
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql, user.getName());
            if (row.next()) {
                code = MessageCode.SUCCESS;
                data = row.getInt("dayword");
            }
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addData(data).build();
    }

    @Override
    public Message syncWordRecord(User user, String wordJson) {
        String sql = "update record set record=? where name=?;";
        String info = "syncRecord";
        MessageCode code = MessageCode.ERROR;
        try {
            int rowEffected = jdbcTemplate.update(sql, wordJson, user.getName());
            if (rowEffected > 0) code = MessageCode.SUCCESS;
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addInfo(info).build();
    }

    @Override
    public Message getWordRecord(User user) {
        String sql = "select record  from record where name=?;";
        MessageCode code = MessageCode.ERROR;
        Object data = null;
        try {
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql, user.getName());
            if (row.next()) {
                code = MessageCode.SUCCESS;
                data = row.getString("record");
            }
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addData(data).build();
    }

    @Override
    public Message getUserStatus(User user) {
        String sql = "select status  from user where name=?;";
        MessageCode code = MessageCode.ERROR;
        Object data = null;
        try {
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql, user.getName());
            if (row.next()) {
                code = MessageCode.SUCCESS;
                data = row.getInt("status");
            }
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addData(data).build();
    }

    @Override
    public Message setUserStatus(User user, int status) {
        String sql = "update user set status=? where name=?;";
        String info = "setStatus";
        MessageCode code = MessageCode.ERROR;
        try {
            int rowEffected = jdbcTemplate.update(sql, status, user.getName());
            if (rowEffected > 0) code = MessageCode.SUCCESS;
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addInfo(info).build();
    }

    public Message addUser(User user) {
        String sql = "insert into user(name,pwd,status,email,ipregion) values(?,?,?,?,?);";
        String info = "addUser";
        MessageCode code = MessageCode.ERROR;
        try {
            int rowEffected = jdbcTemplate.update(sql, user.getName(), user.getPwd(), User.STATUS_OK, user.getEmail(), user.getIpRegion());
            int recordRowEffected = jdbcTemplate.update("insert into record values(?,?)", user.getName(), "");
            if (rowEffected > 0 && recordRowEffected > 0) code = MessageCode.SUCCESS;
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addInfo(info).build();
    }

    @Override
    public boolean adminLogin(Admin admin) {
        String sql = "select pwd from oeadmin where name=?;";
        try {
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql, admin.getName());
            if (row.next()) {
                if (row.getString("pwd").equals(admin.getPwd())) return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public Message getAllOnlineUsers() {
        ArrayList<OnLineUser> onLineUsers = new ArrayList<>();
        UserPool.getAllUser().forEach(new Consumer<User>() {
            @Override
            public void accept(User user) {
                onLineUsers.add(new OnLineUser(user.getName(), user.getEmail(), user.getId()));
            }
        });
        return MessageBuilder.addCode(MessageCode.SUCCESS).addInfo("allUserStatus").addData(onLineUsers).build();
    }

    @Override
    public Message getAllNewUsers() {
        ArrayList<OnLineUser> newUsers = new ArrayList<>();
        NewUserPool.getAllUser().forEach(new Consumer<User>() {
            @Override
            public void accept(User user) {
                newUsers.add(new OnLineUser(user.getName(), user.getEmail(), user.getId()));
            }
        });
        NewUserPool.clearPool();
        return MessageBuilder.addCode(MessageCode.SUCCESS).addInfo("allUserStatus").addData(newUsers).build();
    }

    @Override
    public Message getAllLogs() {
        ArrayList<String> logs = (ArrayList<String>) LogPool.getAllLogs().clone();
        LogPool.clearPool();
        return MessageBuilder.addCode(MessageCode.SUCCESS).addInfo("allLogs").addData(logs).build();
    }

    @Override
    public Message getNotification(int limit) {
        ArrayList<Notification> notifications = new ArrayList<>();
        String sql = "select  date,content from notification order by id desc limit ? ;";
        try {
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql, limit);
            while (row.next()) {
                notifications.add(new Notification(row.getString("date"), row.getString("content")));
            }
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(MessageCode.SUCCESS).addInfo("notify").addData(notifications).build();
    }

    @Override
    public boolean addNotification(Notification notification) {
        String sql = "insert into notification(date,content) values(?,?);";
        String info = "notify";
        try {
            int rowEffected = jdbcTemplate.update(sql, notification.getDate(), notification.getContent());
            if (rowEffected > 0) return true;
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public Message getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "select  * from user;";
        try {
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
            while (row.next()) {
                User user = new User();
                user.setName(row.getString("name"));
                user.setEmail(row.getString("email"));
                user.setDayWord(row.getInt("dayword"));
                user.setWordSize(row.getInt("wordsize"));
                String region = row.getString("ipregion");
                user.setIpRegion((region != null ? region : "未知"));
                users.add(user);
            }
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(MessageCode.SUCCESS).addInfo("users").addData(users).build();
    }

    @Override
    public Message getApkVersion() {
        String sql = "select apkversion from app where id=1 ;";
        String data = "";
        try {
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
            if (row.next()) data = row.getString("apkversion");
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(MessageCode.SUCCESS).addInfo("apkversion").addData(data).build();
    }

    @Override
    public Message updateApkVersion(String version) {
        String sql = "update app set apkversion=? where id = 1;";
        String info = "apkversion";
        MessageCode code = MessageCode.ERROR;
        try {
            int rowEffected = jdbcTemplate.update(sql, version);
            if (rowEffected > 0) code = MessageCode.SUCCESS;
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addInfo(info).build();
    }

    @Override
    public Message setWordSize(User user, int size) {
        String sql = "update user set wordsize=? where name = ?;";
        String info = "wordsize";
        MessageCode code = MessageCode.ERROR;
        try {
            int rowEffected = jdbcTemplate.update(sql, size, user.getName());
            if (rowEffected > 0) code = MessageCode.SUCCESS;
        } catch (Exception e) {
        }
        return MessageBuilder.addCode(code).addInfo(info).build();
    }
}
