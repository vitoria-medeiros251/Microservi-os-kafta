package com.invilia.notification_.consumer2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String emailTo, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("notificacoes.teste.dev@gmail.com");
            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            System.out.println("Email enviado para: " + emailTo);
            
        } catch (Exception e) {
            System.out.println("Erro ao enviar email: " + e.getMessage());
        }
    }
}
