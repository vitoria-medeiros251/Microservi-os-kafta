package com.invilia.notification_.producer2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invilia.notification_.producer2.dtos.NotificationDTO;

@Service
public class KafkaProducerService {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Value("${app.kafka.topic.notification}")
    private String topicName;
    
    public void publishMessageEmail(String userEmail, String userName) {  // parametros
        // variavel local
        var notificationDto = new NotificationDTO();
        notificationDto.setEmailTo(userEmail);
        notificationDto.setSubject("Cadastro realizado com sucesso");
        notificationDto.setText(userName + ", seja bem vindo(a)! \nAgradecemos o seu cadastro em nossa plataforma, aproveite todos os recursos da nossa plataforma.");

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonMessage = mapper.writeValueAsString(notificationDto);
            kafkaTemplate.send(topicName, jsonMessage);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar mensagem", e);
        }
    }
    
    public void sendMessage(Object message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonMessage = mapper.writeValueAsString(message);
            kafkaTemplate.send(topicName, jsonMessage);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar mensagem", e);
        }
    }
}