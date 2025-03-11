package com.team01.project.domain.calendardate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team01.project.domain.calendardate.controller.dto.request.CalendarDateMemoSaveRequest;
import com.team01.project.domain.calendardate.service.CalendarDateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarDateController {

	private final CalendarDateService calendarDateService;

	@PostMapping("/{calendar-date-id}/memo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void writeMemoToCalendarDate(
		@PathVariable(name = "calendar-date-id") Long calendarDateId,
		@RequestBody CalendarDateMemoSaveRequest request,
		@AuthenticationPrincipal OAuth2User user
	) {
		calendarDateService.writeMemo(calendarDateId, request.memo());
	}

}