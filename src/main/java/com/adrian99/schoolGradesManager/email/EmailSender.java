package com.adrian99.schoolGradesManager.email;

import com.adrian99.schoolGradesManager.token.TokenType;
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
    public void sendPasswordEmail(String email, String username, String password, String token, TokenType tokenType) throws MessagingException {

        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");

        helper.setFrom("");
        helper.setTo(email);

        String link;
        if (tokenType.equals(TokenType.ACCOUNT_ACTIVATION)) {
            link = "Salut, numele dvs. de utilizator este: " + username + ", iar parola: " + password +
                    "\n Pentru a activa contul accesati urmatorul link: <a href=\"http://localhost:10000/passwordResetForm.html?token=" + token + "\" target=\"_blank\">Link</a>";
            helper.setSubject("Informatii cont.");
        } else {
            link = "Link pentru resetare parola: <a href=\"http://localhost:10000/passwordResetForm.html?token=" + token + "\" target=\"_blank\">Link</a>";
            helper.setSubject("Resetare parola.");
        }
        helper.setText(link, true);

        mailSender.send(mailMessage);
    }
}
