package com.team01.project.domain.notification.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ModifyNotificationReqBody(
		@NotNull LocalTime notificationTime
) {
}
