package com.team01.project.domain.notification.entity;

import com.team01.project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotificationList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String message;

	@Column(nullable = false)
	private LocalDateTime notificationTime; // 저장된 시간 기록

	private boolean isRead = false;

	public NotificationList(User user, String message) {
		this.user = user;
		this.message = message;
		this.notificationTime = LocalDateTime.now();
	}

	public void markAsRead() {
		this.isRead = true;
	}
}
