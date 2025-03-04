package com.team01.project.domain.notification.controller;

import com.team01.project.domain.notification.entity.Notification;
import com.team01.project.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class ApiV1NotificationController {
    private final NotificationService notificationService;

    // 특정 사용자의 알림 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    // 알림 생성
    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(
            @RequestParam Long userId,
            @RequestParam String message,
            @RequestParam LocalDateTime notificationTime) {

        Notification notification = notificationService.createNotification(userId, message, notificationTime);
        return ResponseEntity.ok(notification);
    }


}
