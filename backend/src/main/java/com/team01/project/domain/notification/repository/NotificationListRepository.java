package com.team01.project.domain.notification.repository;

import com.team01.project.domain.notification.entity.NotificationList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationListRepository extends JpaRepository<NotificationList, Long> {
	List<NotificationList> findByUserId(String userId);

}
