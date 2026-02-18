package com.invilia.notification_.consumer.dtos;

import lombok.Data;

@Data
public class NotificationDTO {
    private String emailTo;
    private String subject;
    private String text;
}