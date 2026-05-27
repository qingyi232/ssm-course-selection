package com.course.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class EmailUtil {
    // ============ 邮件服务器配置 ============
    private static final String SMTP_HOST = "smtp.qq.com";
    private static final String SMTP_PORT = "587";
    private static final String FROM_EMAIL = "3123155744@qq.com";
    private static final String FROM_PASSWORD = "gpnsiofydsgxddga";
    
    // 是否启用真实发送（false=模拟发送，true=真实发送邮件）
    private static final boolean REAL_SEND = true;
    
    // 验证码缓存
    private static Map<String, String> codeCache = new HashMap<>();
    private static Map<String, Long> codeExpireTime = new HashMap<>();
    
    // 生成6位验证码
    public static String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
    
    // 发送验证码（根据配置决定真实发送还是模拟）
    public static boolean sendVerifyCode(String toEmail, String code) {
        if (REAL_SEND) {
            return sendRealEmail(toEmail, code);
        } else {
            return sendVerifyCodeMock(toEmail, code);
        }
    }
    
    // 真实发送邮件
    private static boolean sendRealEmail(String toEmail, String code) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("【在线选课系统】密码找回验证码");
            message.setText("您好！\n\n您正在找回密码，验证码是：" + code + 
                          "\n\n验证码有效期为5分钟，请勿泄露给他人。\n\n如非本人操作，请忽略此邮件。");
            
            Transport.send(message);
            
            // 缓存验证码
            codeCache.put(toEmail, code);
            codeExpireTime.put(toEmail, System.currentTimeMillis() + 5 * 60 * 1000);
            
            System.out.println("【邮件发送成功】验证码 " + code + " 已发送到 " + toEmail);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("【邮件发送失败】" + e.getMessage());
            return false;
        }
    }
    
    // 模拟发送（用于测试，不实际发送邮件）
    public static boolean sendVerifyCodeMock(String toEmail, String code) {
        System.out.println("【模拟发送】验证码 " + code + " 已发送到 " + toEmail);
        codeCache.put(toEmail, code);
        codeExpireTime.put(toEmail, System.currentTimeMillis() + 5 * 60 * 1000);
        return true;
    }
    
    // 验证验证码
    public static boolean verifyCode(String email, String code) {
        String cachedCode = codeCache.get(email);
        Long expireTime = codeExpireTime.get(email);
        
        if (cachedCode == null || expireTime == null) return false;
        if (System.currentTimeMillis() > expireTime) {
            codeCache.remove(email);
            codeExpireTime.remove(email);
            return false;
        }
        
        if (cachedCode.equals(code)) {
            codeCache.remove(email);
            codeExpireTime.remove(email);
            return true;
        }
        return false;
    }
}
