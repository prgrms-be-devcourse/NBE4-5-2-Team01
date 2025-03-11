package com.team01.project.domain.calendardate.controller.dto.request;

import java.util.List;

public record CalendarDateCreateRequest(
	String memo,
	List<String> musicIds
) {

	public CalendarDateCreateRequest(
		String memo,
		List<String> musicIds
	) {
		this.memo = memo == null ? "" : memo;
		this.musicIds = musicIds == null ? List.of() : musicIds;
	}

}