package com.team01.project.domain.notification.dto;

import com.team01.project.domain.notification.entity.Notification;

import java.time.LocalTime;

public record NotificationDto(
		Long id,
		Long userId,
		String message,
		LocalTime notificationTime,
		boolean isRead
) {
	public NotificationDto(Notification notification) {
		this(
				notification.getId(),
				notification.getUser().getId(),
				notification.getMessage(),
				notification.getNotificationTime(),
				notification.isRead()
		);
	}
}
