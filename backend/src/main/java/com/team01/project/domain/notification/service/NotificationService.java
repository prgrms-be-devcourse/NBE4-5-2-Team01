package com.team01.project.domain.notification.service;

import com.team01.project.domain.notification.entity.Notification;
import com.team01.project.domain.notification.repository.NotificationRepository;
import com.team01.project.domain.user.entity.User;
import com.team01.project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository; // User 조회를 위해 필요

    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Transactional
    public Notification createNotification(Long userId, String message, LocalDateTime notificationTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        return notificationRepository.save(Notification.builder().user(user).notificationTime(notificationTime).message(message).build());
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with ID: " + notificationId));

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public Notification updateNotification(Long notificationId, String message, LocalDateTime notificationTime) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with ID: " + notificationId));
        notification.setMessage(message);
        notification.setNotificationTime(notificationTime);
        return notificationRepository.save(notification);
    }
}
