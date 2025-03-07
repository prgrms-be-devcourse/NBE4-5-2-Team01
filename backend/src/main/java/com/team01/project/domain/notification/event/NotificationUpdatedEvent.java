package com.team01.project.domain.notification.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalTime;

@Getter
public class NotificationUpdatedEvent extends ApplicationEvent {
	private final LocalTime updateTime;

	public NotificationUpdatedEvent(Object source, LocalTime updateTime) {
		super(source);
		this.updateTime = updateTime;
	}
}
