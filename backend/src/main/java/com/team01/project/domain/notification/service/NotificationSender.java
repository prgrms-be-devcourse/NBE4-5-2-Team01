package com.team01.project.domain.notification.service;

import com.team01.project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSender {

	private final JavaMailSender javaMailSender;

	public void send(User user, String message) {
		try {
			// MimeMessage 객체 생성
			var mimeMessage = javaMailSender.createMimeMessage();
			var helper = new MimeMessageHelper(mimeMessage, true);

			// 이메일 설정
			helper.setTo(user.getEmail());  // User의 이메일 주소 사용
			helper.setSubject("Music Calendar Notification!!");
			helper.setText(message);

			// 이메일 전송
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			// 예외 처리
			e.printStackTrace();
		}
	}

}
