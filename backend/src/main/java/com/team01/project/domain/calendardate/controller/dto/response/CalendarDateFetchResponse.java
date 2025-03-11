package com.team01.project.domain.calendardate.controller.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.team01.project.domain.calendardate.entity.CalendarDate;
import com.team01.project.domain.music.dto.MusicResponse;
import com.team01.project.domain.music.entity.Music;

public record CalendarDateFetchResponse(

	Long id,

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	LocalDate date,

	String memo,

	List<MusicResponse> musics

) {

	public static CalendarDateFetchResponse from(CalendarDate calendarDate, List<Music> musics) {
		return new CalendarDateFetchResponse(
			calendarDate.getId(),
			calendarDate.getDate(),
			calendarDate.getMemo(),
			musics.stream().map(MusicResponse::fromEntity).toList()
		);
	}

}