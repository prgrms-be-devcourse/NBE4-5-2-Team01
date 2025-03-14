package com.team01.project.domain.notification.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team01.project.domain.notification.dto.NotificationListDto;
import com.team01.project.domain.notification.service.NotificationListService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notification-lists")
public class NotificationListController {

	private final NotificationListService notificationListService;

	// 현재 로그인한 사용자의 알림 목록 조회
	@GetMapping
	public ResponseEntity<List<NotificationListDto>> getUserNotifications(
			@AuthenticationPrincipal OAuth2User user) {
		String userId = user.getName();
		return ResponseEntity.ok(notificationListService.getUserNotificationLists(userId)
				.stream()
				.map(NotificationListDto::new)
				.sorted(Comparator.comparing(NotificationListDto::notificationTime).reversed())
				.toList());
	}

	// 알림 읽음 처리
	@PatchMapping("/{notificationList-id}")
	public ResponseEntity<String> markNotificationAsRead(
			@PathVariable(name = "notificationList-id") Long notificationListId,
			@AuthenticationPrincipal OAuth2User user) {
		String userId = user.getName();

		notificationListService.markAsRead(notificationListId, userId);
		return ResponseEntity.ok("Notification marked as read");
	}

	// 안읽은 알람 모두 읽음 처리
	@PatchMapping("/mark-all-read")
	public ResponseEntity<Void> markAllNotificationAsRead(@AuthenticationPrincipal OAuth2User user) {
		String userId = user.getName();
		notificationListService.markAllAsRead(userId);
		return ResponseEntity.ok().build();
	}


	// 알림 리스트에서 알림 삭제
	@DeleteMapping("/{notificationList-id}")
	public ResponseEntity<String> deleteNotification(
			@PathVariable(name = "notificationList-id") Long notificationListId,
			@AuthenticationPrincipal OAuth2User user) {
		String userId = user.getName();
		notificationListService.deleteNotification(notificationListId, userId);
		return ResponseEntity.ok("Notification deleted");
	}
}
