package com.invilia.notification_.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.invilia.notification_.consumer.dtos.NotificationDTO;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendEmail(NotificationDTO notification) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notification.getEmailTo());
            message.setSubject(notification.getSubject());
            message.setText(notification.getText());
            message.setFrom("notificacoes.teste.dev@gmail.com");
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }
}