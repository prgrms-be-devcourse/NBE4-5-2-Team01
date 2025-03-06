package com.team01.project.domain.notification.service;


import com.team01.project.domain.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationScheduler {

	private final NotificationService notificationService;
	private final NotificationSender notificationSender; // 알림을 보내는 클래스

	@EventListener
	public void handleNotificationUpdated(NotificationUpdatedEvent event) {
		System.out.println("🔔 알림 변경 감지됨! 스케줄링을 다시 설정합니다.");
		scheduleNotifications();
	}

	@Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
	public void sendNotifications() {
		LocalTime now = LocalTime.now().withSecond(0).withNano(0); // 현재 시간 (초, 나노초 제거)
		List<Notification> notifications = notificationService.getNotificationsByTime(now);

		for (Notification notification : notifications) {
			notificationSender.send(notification.getUser(), notification.getMessage());
		}
	}
}
