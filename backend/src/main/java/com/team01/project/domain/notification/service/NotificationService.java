package com.team01.project.domain.notification.service;

import com.team01.project.domain.notification.entity.Notification;
import com.team01.project.domain.notification.event.NotificationUpdatedEvent;
import com.team01.project.domain.notification.repository.NotificationRepository;
import com.team01.project.domain.user.entity.User;
import com.team01.project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final NotificationRepository notificationRepository;
	private final ApplicationEventPublisher eventPublisher;    // 🔥 이벤트 발행기 추가

	private final UserRepository userRepository; // User 조회를 위해 필요

	public List<Notification> getAllNotifications() {
		return notificationRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Notification> getUserNotifications(Long userId) {
		return notificationRepository.findByUserId(userId);
	}

	@Transactional(readOnly = true)
	public Notification getNotification(Long notificationId) {
		return notificationRepository.findById(notificationId)
				.orElseThrow(() -> new IllegalArgumentException("Notification not found with ID: " + notificationId));
	}

	@Transactional
	public void createNotification(Long userId, String message, LocalTime notificationTime) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

		notificationRepository.save(Notification.builder()
				.user(user).notificationTime(notificationTime).message(message).build());

		// 🔥 이벤트 발행 (`NotificationScheduler`에서 감지할 수 있도록)
		eventPublisher.publishEvent(new NotificationUpdatedEvent(this, notificationTime));
	}

	@Transactional
	public void markAsRead(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new IllegalArgumentException("Notification not found with ID: " + notificationId));

		notification.markAsRead();
		notificationRepository.save(notification);
	}

	@Transactional
	public void updateNotification(Long notificationId, String message, LocalTime notificationTime) {
		Notification notification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new IllegalArgumentException("Notification not found with ID: " + notificationId));
		notification.updateMessage(message);
		notification.updateNotificationTime(notificationTime);
		notificationRepository.save(notification);

		// 🔥 이벤트 발행 (`NotificationScheduler`에서 감지할 수 있도록)
		eventPublisher.publishEvent(new NotificationUpdatedEvent(this, notification.getNotificationTime()));
	}

	@Transactional
	public void deleteNotification(Long notificationId) {
		notificationRepository.deleteById(notificationId);
	}

	@Transactional(readOnly = true)
	public List<Notification> getNotificationsByTime(LocalTime time) {
		return notificationRepository.findByNotificationTime(time);
	}

	@Transactional(readOnly = true)
	public List<Notification> getNotificationsBetween(LocalTime now, LocalTime plusMinutes) {
		return notificationRepository.findNotificationsBetween(now, plusMinutes);
	}

}
