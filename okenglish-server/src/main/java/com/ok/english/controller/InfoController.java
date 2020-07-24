package com.ok.english.controller;

import com.ok.english.config.Config;
import com.ok.english.entity.Message;
import com.ok.english.entity.MessageBuilder;
import com.ok.english.entity.MessageCode;
import com.ok.english.service.LogService;
import com.ok.english.service.WebService;
import com.ok.english.tool.SystemInfo;
import com.ok.english.util.CheckTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/info")
public class InfoController {
    @Autowired
    WebService service;

    @GetMapping("/system")
    public Message systemInfo(HttpServletRequest request) {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag))
            return MessageBuilder.addCode(MessageCode.SUCCESS).addInfo("system").addData(SystemInfo.getSystemEnv().split("\r\n")).build();
        else return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "no session found");
    }

    @GetMapping("/file")
    public Message readFile(HttpServletRequest request, @PathParam("path") String path) {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag)) {
            LogService.produceLog("read file:" + path);
            return MessageBuilder.addCode(MessageCode.SUCCESS).addInfo("file").addData(SystemInfo.getFileContent(path).split("\r\n")).build();
        } else return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "no session found");
    }
}
