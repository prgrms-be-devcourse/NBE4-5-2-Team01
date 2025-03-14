package com.team01.project.domain.notification.service;

import java.time.LocalDateTime;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.team01.project.domain.notification.entity.Subscription;
import com.team01.project.domain.notification.repository.SubscriptionRepository;
import com.team01.project.domain.user.entity.User;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class NotificationSender {

	private final JavaMailSender javaMailSender;
	private final NotificationListService notificationListService;
	private final PushNotificationService pushNotificationService;
	private final SubscriptionRepository subscriptionRepository;

	// 이메일 알림
	public void sendEmail(User user, String title, String message) {
		try {
			// MimeMessage 객체 생성
			var mimeMessage = javaMailSender.createMimeMessage();
			var helper = new MimeMessageHelper(mimeMessage, true);

			// 이메일 설정
			helper.setTo(user.getEmail());  // User의 이메일 주소 사용
			helper.setSubject(title);
			helper.setText(message);

			// 이메일 전송
			javaMailSender.send(mimeMessage);

			System.out.println(user.getName() + "님의 " + user.getEmail() + "로 " + title + " 알림이 전송되었습니다.");
		} catch (Exception e) {
			// 예외 처리
			e.printStackTrace();
		}
	}

	// 푸시 알림
	public void sendPush(User user, String title, String message, LocalDateTime notificationTime) {
		Subscription sub = subscriptionRepository.findByUserId(user.getId()).get();
		try {
			pushNotificationService.sendPush(
					sub.getEndpoint(),
					sub.getP256dh(),
					sub.getAuth(),
					title,
					message
			);

			notificationListService.addNotification(user, title, message, notificationTime);
			System.out.println(user.getName() + "님에게 " + title + " 푸시알림이 전송되었습니다.");
		} catch (Exception e) {
			// 실패한 구독은 로그 기록 (실제 서비스에서는 재시도나 구독 삭제 로직 고려)
			e.printStackTrace();
		}
	}
}
