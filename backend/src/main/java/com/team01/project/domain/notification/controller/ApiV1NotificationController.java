package com.team01.project.domain.notification.controller;

import com.team01.project.domain.notification.dto.NotificationDto;
import com.team01.project.domain.notification.entity.Notification;
import com.team01.project.domain.notification.service.NotificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:3000")  // 클라이언트 URL을 지정
public class ApiV1NotificationController {
	private final NotificationService notificationService;

	// 전체 알림 목록 조회
	@GetMapping
	public ResponseEntity<List<NotificationDto>> getNotifications() {
		return ResponseEntity.ok(notificationService.getAllNotifications()
				.stream()
				.map(NotificationDto::new)
				.toList());
	}

	// 특정 사용자의 알림 목록 조회
	@GetMapping("/{userId}/lists")
	public ResponseEntity<List<NotificationDto>> getUserNotifications(@PathVariable(name = "userId") Long userId) {
		return ResponseEntity.ok(notificationService.getUserNotifications(userId)
				.stream()
				.map(NotificationDto::new)
				.toList());
	}

	// 알림 단건 조회
	@GetMapping("/{notificationId}")
	public ResponseEntity<NotificationDto> getUserNotification(
			@PathVariable(name = "notificationId") Long notificationId) {
		Notification notification = notificationService.getNotification(notificationId);
		return ResponseEntity.ok(new NotificationDto(notification));
	}

	record WriteNotificationReqBody(
			@NotNull
			Long userId,
			@NotBlank
			String message,
			@NotNull
			LocalTime notificationTime
	) {
	}

	// 알림 생성
	@PostMapping("/create")
	public ResponseEntity<String> createNotification(
			@RequestBody @Valid WriteNotificationReqBody reqBody) {

		notificationService.createNotification(reqBody.userId, reqBody.message, reqBody.notificationTime);
		return ResponseEntity.ok("알림이 설정되었습니다.");
	}

	// 알림 읽음 처리
	@PutMapping("/{notificationId}/read")
	public ResponseEntity<String> markNotificationAsRead(@PathVariable(name = "notificationId") Long notificationId) {
		notificationService.markAsRead(notificationId);
		return ResponseEntity.ok("Notification marked as read");
	}

	record ModifyNotificationReqBody(
			@NotBlank
			String message,
			@NotNull
			LocalTime notificationTime
	) {
	}

	// 알림 변경
	@PutMapping("/{notificationId}/modify")
	public ResponseEntity<String> modifyNotification(
			@PathVariable(name = "notificationId") Long notificationId,
			@RequestBody @Valid ModifyNotificationReqBody modifyNotificationReqBody) {
		Notification notification = notificationService.updateNotification(
				notificationId, modifyNotificationReqBody.message, modifyNotificationReqBody.notificationTime);

		return ResponseEntity.ok("Notification modified");
	}

	// 알림 삭제
	@DeleteMapping("/{notificationId}")
	public ResponseEntity<String> deleteNotification(@PathVariable(name = "notificationId") Long notificationId) {
		notificationService.deleteNotification(notificationId);
		return ResponseEntity.ok("Notification deleted");
	}
}
