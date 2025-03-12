package com.team01.project.domain.calendardate.controller.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.team01.project.domain.calendardate.entity.CalendarDate;
import com.team01.project.domain.music.entity.Music;

public record MonthlyFetchResponse(
	List<SingleCalendarDate> monthly
) {

	public record SingleCalendarDate(

		Long id,

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
		@JsonDeserialize(using = LocalDateDeserializer.class)
		LocalDate date,

		boolean hasMemo,

		String albumImage

	) {

		public static SingleCalendarDate of(CalendarDate calendarDate, Music music) {
			return new SingleCalendarDate(
				calendarDate.getId(),
				calendarDate.getDate(),
				!(calendarDate.getMemo().isBlank()),
				music != null ? music.getAlbumImage() : "");
		}

		public static SingleCalendarDate from(CalendarDate calendarDate) {
			return of(calendarDate, null);
		}

	}

}