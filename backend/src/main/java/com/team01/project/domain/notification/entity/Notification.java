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

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String message;

	private LocalTime notificationTime;

	@Column(nullable = false)
	private boolean isEmailEnabled = true;

	@Column(nullable = false)
	private boolean isPushEnabled = true;


	public void updateNotificationTime(LocalTime notificationTime) {
		this.notificationTime = notificationTime;
	}

	public void updateNotificationSettings(Boolean emailEnabled, Boolean pushEnabled) {
		if (emailEnabled != null) {
			this.isEmailEnabled = emailEnabled;
		}
		if (pushEnabled != null) {
			this.isPushEnabled = pushEnabled;
		}
	}
}
