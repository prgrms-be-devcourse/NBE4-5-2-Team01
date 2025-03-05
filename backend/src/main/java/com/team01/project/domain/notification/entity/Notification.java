package com.team01.project.domain.notification.entity;

import com.team01.project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private LocalTime notificationTime; // 시:분만 저장

	@Column(nullable = false)
	private String message;

	private boolean isRead = false;

	public void updateMessage(String message) {
		this.message = message;
	}

	public void updateNotificationTime(LocalTime notificationTime) {
		this.notificationTime = notificationTime;
	}

	public void markAsRead() {
		this.isRead = true;
	}
}
