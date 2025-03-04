package com.team01.project.domain.notification.service;

import com.team01.project.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public class NotificationSender {

	public void send(User user, String message) {
		System.out.println("📢 [" + user.getUsername() + "] 님에게 알림 전송: " + message);
		// 이메일, 푸시 알림, SMS 등을 여기에 구현
	}
}
