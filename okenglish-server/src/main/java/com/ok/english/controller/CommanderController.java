package com.ok.english.controller;

import com.ok.english.config.Config;
import com.ok.english.entity.Message;
import com.ok.english.entity.MessageBuilder;
import com.ok.english.entity.MessageCode;
import com.ok.english.service.LogService;
import com.ok.english.tool.CmdExecutor;
import com.ok.english.util.CheckTool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cmd")
public class CommanderController {
    @GetMapping("/execute")
    public Message executeCmd(HttpServletRequest request, @RequestParam("cmd") String cmd) {
        if (CheckTool.sessionCheck(request, Config.admin_session_key, Config.admin_session_tag)) {
            LogService.produceLog("execute:" + cmd);
            return MessageBuilder.addCode(MessageCode.SUCCESS).addInfo("cmd").addData(CmdExecutor.executeCmd(cmd).split("\r\n")).build();
        } else return MessageBuilder.buildSimpleMessage(MessageCode.ERROR, "no session found");
    }
}
