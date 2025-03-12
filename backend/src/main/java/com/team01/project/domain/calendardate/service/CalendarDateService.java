package com.team01.project.domain.calendardate.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.team01.project.domain.calendardate.entity.CalendarDate;
import com.team01.project.domain.calendardate.repository.CalendarDateRepository;
import com.team01.project.domain.user.entity.User;
import com.team01.project.domain.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarDateService {

	private final CalendarDateRepository calendarDateRepository;
	private final UserRepository userRepository;

	public List<CalendarDate> findAllByYearAndMonth(String userId, YearMonth yearMonth) {
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

		return calendarDateRepository.findByUserAndDateBetween(user, start, end);
	}

	public CalendarDate findById(Long calendarDateId) {
		return calendarDateRepository.findById(calendarDateId)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 캘린더 날짜 기록을 찾을 수 없습니다: " + calendarDateId));
	}

	public void writeMemo(Long calendarDateId, String memo) {
		CalendarDate calendarDate = calendarDateRepository.findById(calendarDateId)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 캘린더 날짜 기록을 찾을 수 없습니다: " + calendarDateId));

		calendarDate.writeMemo(memo);
	}

	public CalendarDate create(String userId, LocalDate date, String memo) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

		if (calendarDateRepository.existsByUserAndDate(user, date)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 날짜의 캘린더가 이미 존재합니다.");
		}

		CalendarDate calendarDate = CalendarDate.builder()
			.user(user)
			.date(date)
			.memo(memo)
			.build();

		return calendarDateRepository.save(calendarDate);
	}

}