package com.team01.project.domain.calendardate.service;

import static com.team01.project.global.permission.CalendarPermission.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.team01.project.domain.calendardate.entity.CalendarDate;
import com.team01.project.domain.calendardate.repository.CalendarDateRepository;
import com.team01.project.domain.notification.event.NotificationRecordEvent;
import com.team01.project.domain.user.entity.User;
import com.team01.project.domain.user.repository.UserRepository;
import com.team01.project.global.exception.PermissionDeniedException;
import com.team01.project.global.permission.CalendarPermission;
import com.team01.project.global.permission.PermissionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarDateService {

	private final CalendarDateRepository calendarDateRepository;
	private final UserRepository userRepository;
	private final PermissionService permissionService;
	private final ApplicationEventPublisher eventPublisher;    // 🔥 이벤트 발행기 추가

	/**
	 * 특정 연도와 월에 해당하는 캘린더 리스트 조회
	 */
	public List<CalendarDate> findAllByYearAndMonth(String calendarOwnerId, String loggedInUserId,
		YearMonth yearMonth) {
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		User calendarOwner = userRepository.getById(calendarOwnerId);
		User loggedInUser = userRepository.getById(loggedInUserId);

		CalendarPermission calendarPermission = permissionService.checkPermission(calendarOwner, loggedInUser);

		if (calendarPermission == NONE) {
			throw new PermissionDeniedException("403-10", "먼슬리 캘린더를 조회할 권한이 없습니다.");
		}

		return calendarDateRepository.findByUserAndDateBetween(calendarOwner, start, end);
	}

	/**
	 * 캘린더 조회
	 */
	public CalendarDate findById(Long calendarDateId, String loggedInUserId) {
		User loggedInUser = userRepository.findById(loggedInUserId)
				.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저입니다."));

		CalendarDate calendarDate = calendarDateRepository.findByIdOrThrow(calendarDateId);

		permissionService.checkCalendarDateFetchPermission(calendarDate, loggedInUser);

		return calendarDate;
	}

	/**
	 * 메모 작성
	 */
	public void writeMemo(Long calendarDateId, String loggedInUserId, String memo) {
		User loggedInUser = userRepository.findById(loggedInUserId)
				.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저입니다."));

		CalendarDate calendarDate = calendarDateRepository.findByIdOrThrow(calendarDateId);

		permissionService.checkCalendarDateUpdatePermission(calendarDateId, loggedInUser);

		calendarDate.writeMemo(memo);
	}

	/**
	 * 캘린더 생성
	 */
	public CalendarDate create(String userId, LocalDate date, String memo) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

		if (calendarDateRepository.existsByUserAndDate(user, date)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 날짜의 캘린더가 이미 존재합니다.");
		}

		// 🔥 이벤트 발행 (`NotificationScheduler`에서 감지할 수 있도록) 캘린더 생성 알림
		eventPublisher.publishEvent(new NotificationRecordEvent(this, LocalTime.now(), user));

		CalendarDate calendarDate = CalendarDate.builder()
				.user(user)
				.date(date)
				.memo(memo)
				.build();

		return calendarDateRepository.save(calendarDate);
	}

}