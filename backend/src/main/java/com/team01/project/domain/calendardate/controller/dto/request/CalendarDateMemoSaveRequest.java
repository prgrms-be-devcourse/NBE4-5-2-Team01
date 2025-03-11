package com.team01.project.domain.calendardate.controller.dto.request;

import jakarta.validation.constraints.Size;

public record CalendarDateMemoSaveRequest(

	@Size(max = 1000, message = "메모는 1000자 이내로 입력해주세요.")
	String memo

) {
}