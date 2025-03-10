package com.team01.project.domain.notification.dto;

import com.team01.project.domain.notification.entity.Notification;

import java.time.LocalTime;

public record NotificationDto(
		Long id,
		String userId,
		String title,
		String message,
		LocalTime notificationTime
) {
	public NotificationDto(Notification notification) {
		this(
				notification.getId(),
				notification.getUser().getId(),
				notification.getTitle(),
				notification.getMessage(),
				notification.getNotificationTime()
		);
	}
}
