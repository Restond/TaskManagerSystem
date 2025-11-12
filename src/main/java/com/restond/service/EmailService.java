package com.restond.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendLoginSuccessEmail(String toEmail, String username) {
        try {
            logger.debug("开始准备发送登录成功邮件 - 收件人: {}, 用户名: {}", toEmail, username);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toEmail);
            mailMessage.setSubject("登录成功通知");
            mailMessage.setText("尊敬的 " + username + "，您已成功登录任务管理系统。");
            mailMessage.setFrom("2313494015@qq.com");

            logger.debug("邮件内容准备完成，开始发送...");
            javaMailSender.send(mailMessage);

            logger.info("登录成功邮件发送成功 - 收件人: {}, 用户名: {}", toEmail, username);
        } catch (Exception e) {
            logger.error("发送登录成功邮件失败 - 收件人: {}, 用户名: {}, 错误: {}", toEmail, username, e.getMessage());
            logger.debug("邮件发送异常详情:", e);
            throw e;
        }
    }
}
