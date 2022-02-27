package com.adrian99.schoolGradesManager.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailSender {

    private final JavaMailSender mailSender;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendPasswordEmail(String email, String username, String password) throws MessagingException {

        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");

        helper.setFrom("");
        helper.setTo(email);

        String link;

        link = "Salut, numele dvs. de utilizator este: " + username + ", iar parola: " + password;
        helper.setSubject("Account password");

        helper.setText(link, true);

        mailSender.send(mailMessage);
    }
}
