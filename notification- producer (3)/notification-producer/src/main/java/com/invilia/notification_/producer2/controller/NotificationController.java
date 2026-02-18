package com.invilia.notification_.producer2.controller;
import com.invilia.notification_.producer2.dtos.NotificationDTO;
import com.invilia.notification_.producer2.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/notifications")
    public ResponseEntity<String> createNotification(@RequestBody NotificationDTO notificationDTO) {
        kafkaProducerService.sendMessage(notificationDTO);
        return ResponseEntity.ok("Notificação enviada com sucesso!");
    }
}