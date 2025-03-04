package com.team01.project.domain.notification.dto;

import com.team01.project.domain.notification.entity.Notification;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class NotificationDto {
    Long id;
    Long userId;
    String message;
    LocalTime notificationTime;

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.userId = notification.getUser().getId();
        this.message = notification.getMessage();
        this.notificationTime = notification.getNotificationTime();

    }
}
