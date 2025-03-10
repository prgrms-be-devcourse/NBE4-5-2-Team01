package com.team01.project.domain.notification.dto;

import com.team01.project.domain.notification.entity.NotificationList;

import java.time.LocalDateTime;

public record NotificationListDto(
		Long id,
		String userId,
		String message,
		LocalDateTime notificationTime
) {
	public NotificationListDto(NotificationList notificationList) {
		this(
				notificationList.getId(),
				notificationList.getUser().getId(),
				notificationList.getMessage(),
				notificationList.getNotificationTime()
		);
	}
}
