package com.team01.project.domain.notification.service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.team01.project.domain.notification.constants.NotificationMessages;
import com.team01.project.domain.notification.dto.NotificationUpdateDto;
import com.team01.project.domain.notification.entity.Notification;
import com.team01.project.domain.notification.event.NotificationUpdatedEvent;
import com.team01.project.domain.notification.repository.NotificationRepository;
import com.team01.project.domain.user.entity.User;
import com.team01.project.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;


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
	public List<Notification> getUserNotifications(String userId) {
		return notificationRepository.findByUserId(userId);
	}

	@Transactional(readOnly = true)
	public Notification getNotification(Long notificationId) {
		return notificationRepository.findById(notificationId)
				.orElseThrow(() -> new IllegalArgumentException("Notification not found with ID: " + notificationId));
	}

	@Transactional
	public void updateNotification(String userId, Long notificationId, LocalTime notificationTime) {
		Notification notification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new IllegalArgumentException("Notification not found with ID: " + notificationId));

		if (!notification.getUser().getId().equals(userId)) { // 유저가 동일한지 확인
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"You do not have permission to update this notification.");
		}

		notification.updateNotificationTime(notificationTime);
		notificationRepository.save(notification);

		// 🔥 이벤트 발행 (`NotificationScheduler`에서 감지할 수 있도록)
		eventPublisher.publishEvent(new NotificationUpdatedEvent(this, notification.getNotificationTime()));
	}

	@Transactional(readOnly = true)
	public List<Notification> getNotificationsByTime(LocalTime time) {
		return notificationRepository.findByNotificationTime(time);
	}

	@Transactional(readOnly = true)
	public List<Notification> getNotificationsBetween(LocalTime now, LocalTime plusMinutes) {
		return notificationRepository.findNotificationsBetween(now, plusMinutes);
	}

	// 유저가 회원가입할 때 생성
	@Transactional
	public void createDefaultNotifications(User user) {
		List<Notification> notifications = NotificationMessages.DEFAULT_MESSAGES.entrySet().stream()
				.map(entry -> {
					// 메시지 타입에 따라 알림 시간 설정
					LocalTime notificationTime = null;
					if ("DAILY_CHALLENGE".equals(entry.getKey())) {
						notificationTime = LocalTime.of(21, 0);
					} else if ("YEAR_HISTORY".equals(entry.getKey())) {
						notificationTime = LocalTime.of(9, 0);
					} else if ("BUILD_PLAYLIST".equals(entry.getKey())) {
						notificationTime = LocalTime.of(18, 0);
					}

					return Notification.builder()
							.user(user)
							.notificationTime(notificationTime)
							.title(entry.getKey())
							.message(String.format(entry.getValue(), user.getName()))
							.build();
				})
				.collect(Collectors.toList());

		notificationRepository.saveAll(notifications);
	}

	@Transactional
	public void updateNotifications(List<NotificationUpdateDto> notifications, String userId) {
		for (NotificationUpdateDto dto : notifications) {
			Notification notification = notificationRepository.findById(dto.notificationId())
					.orElseThrow(() ->
							new IllegalArgumentException("Notification not found with ID: " + dto.notificationId()));

			if (!notification.getUser().getId().equals(userId)) {    // 유저가 동일한지 확인
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,
						"You do not have permission to update this notification.");
			}

			notification.updateNotificationSettings(
					dto.isEmailNotificationEnabled(),
					dto.isPushNotificationEnabled()
			);
		}
	}
}
