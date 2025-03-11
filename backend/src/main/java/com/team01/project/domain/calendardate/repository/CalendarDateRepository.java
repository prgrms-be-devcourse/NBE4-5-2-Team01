package com.team01.project.domain.calendardate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team01.project.domain.calendardate.entity.CalendarDate;

public interface CalendarDateRepository extends JpaRepository<CalendarDate, Long> {
}