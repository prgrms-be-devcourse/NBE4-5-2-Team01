package com.team01.project.domain.notification.repository;

import com.team01.project.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByUserId(Long userId);

	@EntityGraph(attributePaths = {"user"})
	List<Notification> findByNotificationTime(LocalTime notificationTime);
}
