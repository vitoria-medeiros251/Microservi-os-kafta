package com.invilia.notification_.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invilia.notification_.consumer.dtos.NotificationDTO;

@Service
public class KafkaConsumerService {
    
    @Autowired
    private EmailService emailService;
    
    @KafkaListener(topics = "${app.kafka.topic.notification}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            NotificationDTO notification = mapper.readValue(message, NotificationDTO.class);
            
            System.out.println("Mensagem recebida: " + notification.getEmailTo());
            
            emailService.sendEmail(notification);
            
            System.out.println("Email enviado com sucesso para: " + notification.getEmailTo());
            
        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
            throw new RuntimeException("Erro ao processar mensagem", e);
        }
    }
}