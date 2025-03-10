package com.team01.project.domain.notification.service;

import com.team01.project.domain.notification.entity.NotificationList;
import com.team01.project.domain.notification.repository.NotificationListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationListService {

	private final NotificationListRepository notificationListRepository;

	@Transactional(readOnly = true)
	public List<NotificationList> getUserNotificationLists(String userId) {
		return notificationListRepository.findByUserId(userId);
	}

}
