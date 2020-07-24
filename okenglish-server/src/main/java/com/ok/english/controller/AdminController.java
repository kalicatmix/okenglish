package com.ok.english.controller;

import com.ok.english.config.Config;
import com.ok.english.entity.*;
import com.ok.english.service.LogService;
import com.ok.english.service.WebService;
import com.ok.english.util.CheckTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    WebService webService;

    @PostMapping("/login")
    public String adminLogin(HttpServletRequest request, @RequestParam("name") String name, @RequestParam("pwd") String pwd) {
        if (webService.adminLogin(new Admin(name, CheckTool.mdSign(pwd)))) {
            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(60 * 30);
            session.setAttribute(Config.admin_session_key, Config.admin_session_tag);
            return "redirect:/admin/manager";
        } else {
            LogService.produceLog("admin login fail");
            return "redirect:/";
        }
    }

    @GetMapping("/manager")
    public String manage(HttpServletRequest request, HttpServletResponse response) {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag)) {
            LogService.produceLog("admin login success");
            return "adminmg";
        }
        return "index";
    }

    @ResponseBody
    @PostMapping("/upload")
    public Message adminUpload(@RequestPart("apk") MultipartFile apk, HttpServletRequest request) throws IOException {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag)) {
            File file = new File(this.getClass().getClassLoader().getResource("public/apk/").getFile() + "apk.upload");
            apk.transferTo(file);
            LogService.produceLog("apk file upload");
            return MessageBuilder.buildSimpleMessage(MessageCode.SUCCESS, "upload success");
        }
        return MessageBuilder.buildSimpleMessage(MessageCode.WRONG, "upload fail");
    }

    @ResponseBody
    @GetMapping("/deploy")
    public Message deployApk(HttpServletRequest request, @RequestParam("version") String version) {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag)) {
            String path = this.getClass().getClassLoader().getResource("public/apk/").getFile();
            File oldApk = new File(path + "okenglish.apk");
            File bakApk = new File(path + "apk.bak");
            File targetApk = new File(path + "okenglish.apk");
            File uploadApk = new File(path + "apk.upload");
            oldApk.renameTo(bakApk);
            uploadApk.renameTo(targetApk);
            return webService.updateApkVersion(version);
        } else return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "deploy fail");
    }

    @ResponseBody
    @GetMapping("/version")
    public Message apkVersion(HttpServletRequest request) {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag))
            return webService.getApkVersion();
        else return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "userStatus");
    }

    @ResponseBody
    @GetMapping("/status")
    public Message userStatus(HttpServletRequest request) {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag))
            return webService.getAllOnlineUsers();
        else return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "userStatus");
    }

    @ResponseBody
    @GetMapping("/new")
    public Message newUsers(HttpServletRequest request) {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag))
            return webService.getAllNewUsers();
        else return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "userStatus");
    }

    @ResponseBody
    @GetMapping("/logs")
    public Message log(HttpServletRequest request) {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag))
            return webService.getAllLogs();
        else return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "logs");
    }

    @ResponseBody
    @PostMapping("/notify")
    public Message addNotification(HttpServletRequest request, @RequestParam(value = "date") String date, @RequestParam(value = "content") String content) {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag) && webService.addNotification(new Notification(date, content))) {
            return MessageBuilder.buildSimpleMessage(MessageCode.SUCCESS, "notify");
        } else return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "notify");
    }

    @ResponseBody
    @GetMapping("/notify/get")
    public Message getNotification(HttpServletRequest request) {
        return webService.getNotification(5);
    }

    @ResponseBody
    @GetMapping("/users")
    public Message getAllUsers(HttpServletRequest request) {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag))
            return webService.getAllUsers();
        else return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "users");
    }
}
