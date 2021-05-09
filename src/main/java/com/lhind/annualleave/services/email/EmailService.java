package com.lhind.annualleave.services.email;

import com.lhind.annualleave.services.email.builder.EmailInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {
    public static String CHANGE_PASSWORD_SUBJECT = "Annual Leave - Password changed successfully!";
    public static String LEAVE_STATUS_CHANGE_SUBJECT = "Annual Leave - Request status change!";

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(EmailInfo emailInfo) throws MailException {
        String from = emailInfo.getFrom();
        String to = emailInfo.getTo();
        String subject = emailInfo.getSubject();
        String content = emailInfo.getContent();
        String bcc = emailInfo.getBCC();
        String cc = emailInfo.getCC();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        if (from != null && !from.trim().isEmpty()) {
            simpleMailMessage.setFrom(from);
        }
        if (to != null && !to.trim().isEmpty()) {
            simpleMailMessage.setTo(to);
        }
        if (subject != null && !subject.trim().isEmpty()) {
            simpleMailMessage.setSubject(subject);
        }
        if (content != null && !content.trim().isEmpty()) {
            simpleMailMessage.setText(content);
        }
        if (bcc != null && !bcc.trim().isEmpty()) {
            simpleMailMessage.setBcc(bcc);
        }
        if (cc != null && !cc.trim().isEmpty()) {
            simpleMailMessage.setCc(cc);
        }

        javaMailSender.send(simpleMailMessage);
    }

    public String getEmailAccountOfSystem() {
        return "7klauskalemi@gmail.com";
    }
}
