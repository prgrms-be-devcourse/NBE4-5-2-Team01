package com.team01.project.domain.notification.controller;

import com.team01.project.domain.notification.entity.Notification;
import com.team01.project.domain.notification.service.NotificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class ApiV1NotificationController {
    private final NotificationService notificationService;

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
    public ResponseEntity<NotificationDto> getUserNotification(@PathVariable(name = "notificationId") Long notificationId) {
        Notification notification = notificationService.getNotification(notificationId);
        return ResponseEntity.ok(new NotificationDto(notification));
    }

    // 알림 생성
    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(
            @RequestParam Long userId,
            @RequestParam String message,
            @RequestParam String notificationTime) {

        // "HH:mm" 형식의 문자열을 LocalTime으로 변환
        LocalTime time = LocalTime.parse(notificationTime, DateTimeFormatter.ofPattern("HH:mm"));

        Notification notification = notificationService.createNotification(userId, message, time);
        return ResponseEntity.ok(notification);
    }

    // 알림 읽음 처리
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }

    record ModifyNotificationReqBody(
            @NotBlank
            String message,
            @NotBlank
            LocalTime notificationTime
    ) {
    }

    // 알림 변경
    @PutMapping("/{notificationId}/modify")
    public ResponseEntity<Notification> ModifyNotification(@PathVariable Long notificationId, @RequestBody @Valid ModifyNotificationReqBody modifyNotificationReqBody) {
        Notification notification = notificationService.updateNotification(
                notificationId, modifyNotificationReqBody.message, modifyNotificationReqBody.notificationTime);

        return ResponseEntity.ok(notification);
    }

    // 알림 삭제
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("Notification deleted");
    }
}
