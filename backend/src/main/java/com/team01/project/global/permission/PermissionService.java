package com.team01.project.global.permission;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.team01.project.domain.calendardate.repository.CalendarDateRepository;
import com.team01.project.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissionService {

	private final CalendarDateRepository calendarDateRepository;

	public void checkCalendarDateUpdatePermission(Long calendarDateId, User loggedInUser) {
		if (!calendarDateRepository.existsByIdAndUser(calendarDateId, loggedInUser)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 캘린더를 수정할 권한이 없습니다.");
		}
	}

}