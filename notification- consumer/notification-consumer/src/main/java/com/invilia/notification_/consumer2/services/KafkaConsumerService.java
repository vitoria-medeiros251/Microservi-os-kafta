package com.invilia.notification_.consumer2.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invilia.notification_.consumer2.dtos.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    
    @Autowired
    private EmailService emailService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consumeNotification(String message) {
        logger.info("=== NOVA MENSAGEM RECEBIDA ===");
        logger.info("Timestamp: {}", System.currentTimeMillis());
        logger.info("Conteúdo: {}", message);
        
        try {
            NotificationDTO notification = objectMapper.readValue(message, NotificationDTO.class);
            logger.info("Mensagem deserializada - Email: {}, Subject: {}", 
                       notification.getEmailTo(), notification.getSubject());
            
            emailService.sendEmail(notification.getEmailTo(), notification.getSubject(), notification.getText());
            
            logger.info(" Email processado com sucesso para: {}", notification.getEmailTo());
            
        } catch (Exception e) {
            logger.error(" Erro ao processar mensagem: {}", e.getMessage(), e);
        }
        
        logger.info("=== FIM DO PROCESSAMENTO ===");
    }
}