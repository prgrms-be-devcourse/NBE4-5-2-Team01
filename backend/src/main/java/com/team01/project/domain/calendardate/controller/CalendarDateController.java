package com.team01.project.domain.calendardate.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team01.project.domain.calendardate.controller.dto.request.CalendarDateCreateRequest;
import com.team01.project.domain.calendardate.controller.dto.request.CalendarDateMemoSaveRequest;
import com.team01.project.domain.calendardate.controller.dto.request.CalendarDateMusicSaveRequest;
import com.team01.project.domain.calendardate.controller.dto.response.CalendarDateCreateResponse;
import com.team01.project.domain.calendardate.controller.dto.response.CalendarDateFetchResponse;
import com.team01.project.domain.calendardate.controller.dto.response.MonthlyFetchResponse;
import com.team01.project.domain.calendardate.entity.CalendarDate;
import com.team01.project.domain.calendardate.service.CalendarDateService;
import com.team01.project.domain.music.entity.Music;
import com.team01.project.domain.musicrecord.entity.MusicRecord;
import com.team01.project.domain.musicrecord.service.MusicRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarDateController {

	private final CalendarDateService calendarDateService;
	private final MusicRecordService musicRecordService;

	@GetMapping(params = {"year", "month"})
	@ResponseStatus(HttpStatus.OK)
	public MonthlyFetchResponse fetchMonthlyCalendar(
		@RequestParam int year,
		@RequestParam int month,
		@AuthenticationPrincipal OAuth2User user
	) {
		String userId = user.getName();
		YearMonth yearMonth = YearMonth.of(year, month);

		// CalendarDate 리스트 조회
		List<CalendarDate> calendarDates = calendarDateService.findAllByYearAndMonth(userId, yearMonth);

		// CalendarDate 리스트를 순회하며 SingleCalendarDate 리스트로 변환
		List<MonthlyFetchResponse.SingleCalendarDate> monthly = mapToMonthly(calendarDates);

		return new MonthlyFetchResponse(monthly);
	}

	@GetMapping("/{calendar-date-id}")
	@ResponseStatus(HttpStatus.OK)
	public CalendarDateFetchResponse fetchCalendarDate(
		@PathVariable(name = "calendar-date-id") Long calendarDateId,
		@AuthenticationPrincipal OAuth2User user
	) {
		// CalendarDate 조회
		CalendarDate calendarDate = calendarDateService.findById(calendarDateId);

		// CalendarDate와 연관된 MusicRecord를 이용해 Music 리스트 조회
		List<Music> musics = musicRecordService.findMusicsByCalendarDateId(calendarDateId);

		return CalendarDateFetchResponse.of(calendarDate, musics);
	}

	@PostMapping(params = {"year", "month", "day"})
	@ResponseStatus(HttpStatus.CREATED)
	public CalendarDateCreateResponse createCalendarDate(
		@RequestParam int year,
		@RequestParam int month,
		@RequestParam int day,
		@RequestBody CalendarDateCreateRequest request,
		@AuthenticationPrincipal OAuth2User user
	) {
		String userId = user.getName();
		LocalDate date = LocalDate.of(year, month, day);

		// 캘린더 생성
		CalendarDate calendarDate = calendarDateService.create(userId, date, request.memo());

		Long calendarDateId = calendarDate.getId();

		// 음악 기록 저장
		musicRecordService.updateMusicRecords(calendarDateId, request.musicIds());

		return new CalendarDateCreateResponse(calendarDateId);
	}

	@PostMapping("/{calendar-date-id}/music")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void saveMusicToCalendarDate(
		@PathVariable(name = "calendar-date-id") Long calendarDateId,
		@RequestBody CalendarDateMusicSaveRequest request,
		@AuthenticationPrincipal OAuth2User user
	) {
		musicRecordService.updateMusicRecords(calendarDateId, request.musicIds());
	}

	@PostMapping("/{calendar-date-id}/memo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void writeMemoToCalendarDate(
		@PathVariable(name = "calendar-date-id") Long calendarDateId,
		@RequestBody CalendarDateMemoSaveRequest request,
		@AuthenticationPrincipal OAuth2User loggedInUser
	) {
		String loggedInUserId = loggedInUser.getName();
		calendarDateService.writeMemo(calendarDateId, loggedInUserId, request.memo());
	}

	private List<MonthlyFetchResponse.SingleCalendarDate> mapToMonthly(List<CalendarDate> calendarDates) {
		return calendarDates.stream()
			.map(this::mapToSingleCalendarDate)
			.toList();
	}

	private MonthlyFetchResponse.SingleCalendarDate mapToSingleCalendarDate(CalendarDate calendarDate) {
		Optional<MusicRecord> optionalMusicRecord = musicRecordService.findOneByCalendarDateId(calendarDate.getId());

		return optionalMusicRecord
			.map(musicRecord -> MonthlyFetchResponse.SingleCalendarDate.of(calendarDate, musicRecord.getMusic()))
			.orElseGet(() -> MonthlyFetchResponse.SingleCalendarDate.from(calendarDate));
	}

}