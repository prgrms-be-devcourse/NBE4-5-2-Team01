package com.team01.project.domain.calendardate.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team01.project.domain.calendardate.entity.CalendarDate;
import com.team01.project.domain.user.entity.User;

public interface CalendarDateRepository extends JpaRepository<CalendarDate, Long> {

	List<CalendarDate> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);

	boolean existsByUserAndDate(User user, LocalDate date);

	boolean existsByIdAndUser(Long calendarDateId, User user);

}