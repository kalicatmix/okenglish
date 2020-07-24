package com.ok.english.util;

import com.ok.english.config.Config;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public class MailSender {
    private JavaMailSenderImpl senderImpl;
    private static MailSender sender;

    private MailSender() {
        senderImpl = new JavaMailSenderImpl();
        senderImpl.setHost(Config.mail_smtp_sever);
        senderImpl.setPort(Config.mail_smtp_port);
        senderImpl.setUsername(Config.mail_auth_user);
        senderImpl.setPassword(Config.mail_auth_code);
    }

    public static MailSender getSender() {
        synchronized (MailSender.class) {
            if (sender == null) sender = new MailSender();
        }
        return sender;
    }

    public void send(MimeMessage message) {
        senderImpl.send(message);
    }

    public MimeMessage createMimeMessage() {
        return senderImpl.createMimeMessage();
    }

    public void sendSimpleHtml(String html, String target, String subject) {
        MimeMessage mimeMessage = senderImpl.createMimeMessage();
        try {
            mimeMessage.setFrom(Config.mail_auth_user);
            mimeMessage.setContent(html, "text/html");
            mimeMessage.setRecipients(Message.RecipientType.TO, target);
            mimeMessage.setSentDate(new Date());
            mimeMessage.setSubject(subject);
        } catch (Exception e) {
        }
        send(mimeMessage);
    }

    public void sendSimpleText(String message, String target, String subject) {
        MimeMessageHelper messageHelper = new MimeMessageHelper(senderImpl.createMimeMessage());
        try {
            messageHelper.setSubject(subject);
            messageHelper.setTo(target);
            messageHelper.setText(message);
            messageHelper.setFrom(Config.mail_auth_user);
            messageHelper.setSentDate(new Date());
        } catch (Exception e) {
            LoggerFactory.getLogger(MailSender.class).error(e.toString());
        }
        send(messageHelper.getMimeMessage());
    }
}
