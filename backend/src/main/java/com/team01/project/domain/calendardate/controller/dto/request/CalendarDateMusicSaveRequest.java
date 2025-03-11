package com.team01.project.domain.calendardate.controller.dto.request;

import java.util.List;

public record CalendarDateMusicSaveRequest(
	List<String> musicIds
) {
}