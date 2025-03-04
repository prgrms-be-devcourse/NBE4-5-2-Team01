package com.team01.project.domain.notification.service;

import com.team01.project.domain.notification.entity.Notification;
import com.team01.project.domain.notification.repository.NotificationRepository;
import com.team01.project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository; // User 조회를 위해 필요

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }


}
