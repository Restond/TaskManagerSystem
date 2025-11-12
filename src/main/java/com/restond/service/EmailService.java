package com.restond.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.File;

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
            mailMessage.setFrom("");

            logger.debug("邮件内容准备完成，开始发送...");
            javaMailSender.send(mailMessage);

            logger.info("登录成功邮件发送成功 - 收件人: {}, 用户名: {}", toEmail, username);
        } catch (Exception e) {
            logger.error("发送登录成功邮件失败 - 收件人: {}, 用户名: {}, 错误: {}", toEmail, username, e.getMessage());
            logger.debug("邮件发送异常详情:", e);
            throw e;
        }
    }
/*
    public void sendLoginSuccessEmailWithAttachment(String toEmail, String username, String filePath, String imagePath) {
        MimeMessage mimeMessage = null;
        try {
            logger.debug("开始准备发送带附件的登录成功邮件 - 收件人：{}，用户名：{}，附件路径：{}，图片路径：{}", toEmail, username, filePath, imagePath);
            mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("登录成功通知");
            helper.setFrom("2313494015@qq.com");

            String htmlContent =  createHtmlEmailContent(username);
            helper.setText(htmlContent, true);

            if (imagePath != null && !imagePath.trim().isEmpty()) {
                logger.debug("添加内嵌图片: {}", imagePath);
                System.out.println("nq:" + imagePath);
                FileSystemResource imageResource = new FileSystemResource(new File(imagePath));
                helper.addInline("logoImage", imageResource);
            }

            if (filePath != null && !filePath.trim().isEmpty()) {
                logger.debug("添加文件附件: {}", filePath);
                FileSystemResource fileResource = new FileSystemResource(new File(filePath));
                String fileName = new File(filePath).getName();
                helper.addAttachment(fileName, fileResource);
            }
            logger.debug("带附件的邮件内容准备完成，开始发送...");
            javaMailSender.send(mimeMessage);

            logger.info("带附件的登录成功邮件发送成功 - 收件人: {}, 用户名: {}", toEmail, username);
        } catch (MessagingException e) {
            logger.error("发送带附件的登录成功邮件失败 - 收件人: {}, 用户名: {}, 错误: {}", toEmail, username, e.getMessage());
            logger.debug("邮件发送异常详情:", e);
            throw new RuntimeException("邮件发送失败: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("发送带附件的登录成功邮件时发生未知错误 - 收件人: {}, 用户名: {}, 错误: {}", toEmail, username, e.getMessage());
            logger.debug("未知异常详情:", e);
            throw new RuntimeException("邮件发送失败: " + e.getMessage(), e);
        }
    }

    private String createHtmlEmailContent(String username) {
        return "<html>" +
                "<body>" +
                "<h3>尊敬的 " + username + "，您已成功登录任务管理系统</h3>" +
                "<p>登录时间: " + java.time.LocalDateTime.now() + "</p>" +
                "<p>如果这不是您本人的操作，请立即联系系统管理员。</p>" +
                "<img src='cid:logoImage' alt='系统Logo'>" +
                "<p>此邮件由系统自动发送，请勿回复。</p>" +
                "</body>" +
                "</html>";
    }
 */
}
