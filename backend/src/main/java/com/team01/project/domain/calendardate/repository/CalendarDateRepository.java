package com.team01.project.domain.calendardate.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team01.project.domain.calendardate.entity.CalendarDate;

public interface CalendarDateRepository extends JpaRepository<CalendarDate, Long> {

	List<CalendarDate> findByUserIdAndDateBetween(String userId, LocalDate start, LocalDate end);

}