package com.hospitalmanagementsystem.demo.services;

import com.hospitalmanagementsystem.demo.entities.Appointment;
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

    public void makeAppointment(String userEmail, String name, String lastname, Appointment appointment) throws MessagingException {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,"utf-8");
            String subject = "Your Appointment has been confirmed";
            String text = "Dear " + name + " " + lastname   + " Welcome to the KOU Hospital. We are so glad to see you here. \nYour appointment has been completed successfully. \n" +
                    "-----------Details----------\n" +
                    "Start Time:" + appointment.getSlot().getStartTime() + "\n" +
                    "End Time:" + appointment.getSlot().getEndTime() + "\n" +
                    "Doctor Name:" + appointment.getSlot().getDoctorSchedule().getDoctor().getUser().getName() + " " + appointment.getSlot().getDoctorSchedule().getDoctor().getUser().getLastname() + "\n" +
                    "Department:" + appointment.getSlot().getDoctorSchedule().getDoctor().getDepartment().getDepartmentName() + "\n" +
                    " Have a great day!";

            messageHelper.setSubject(subject);
            messageHelper.setText(text);
            messageHelper.setTo(userEmail);

            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new MailSendException("failed to send mail");
        }
    }
}
