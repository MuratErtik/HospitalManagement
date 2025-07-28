package com.hospitalmanagementsystem.demo.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void afterTheRegisteration(String userEmail,String name,String lastname) throws MessagingException {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,"utf-8");
            String subject = "Registration Confirmation";
            String text = "Dear " + name + " " + lastname   + " Welcome to the KOU Hospital. We are so glad to see you here. \nYour register process has been completed successfully. Have a great day!";

            messageHelper.setSubject(subject);
            messageHelper.setText(text);
            messageHelper.setTo(userEmail);

            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new MailSendException("failed to send mail");
        }

    }
}
