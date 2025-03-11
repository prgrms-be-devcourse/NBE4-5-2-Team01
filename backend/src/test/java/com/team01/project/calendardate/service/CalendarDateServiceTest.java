package com.team01.project.calendardate.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.team01.project.domain.calendardate.entity.CalendarDate;
import com.team01.project.domain.calendardate.repository.CalendarDateRepository;
import com.team01.project.domain.calendardate.service.CalendarDateService;
import com.team01.project.domain.user.entity.User;
import com.team01.project.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CalendarDateServiceTest {

	@Mock
	private CalendarDateRepository calendarDateRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CalendarDateService calendarDateService;

	@Test
	void 먼슬리_캘린더를_유저_아이디와_날짜로_조회한다() {

		// given
		String mockUserId = "test-user";
		User mockUser = User.builder().id(mockUserId).build();

		YearMonth yearMonth = YearMonth.of(2025, 3);
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		List<CalendarDate> mockCalendarDates = List.of(
			CalendarDate.builder()
				.user(mockUser)
				.date(LocalDate.of(2025, 3, 1))
				.memo("memo 1")
				.build(),
			CalendarDate.builder()
				.user(mockUser)
				.date(LocalDate.of(2025, 3, 31))
				.memo("memo 2")
				.build()
		);

		when(calendarDateRepository.findByUserIdAndDateBetween(mockUserId, start, end))
			.thenReturn(mockCalendarDates);

		// when
		List<CalendarDate> result = calendarDateService.findAllByYearAndMonth(mockUserId, yearMonth);

		// then
		assertThat(result).hasSize(mockCalendarDates.size());
		result.forEach(calendarDate ->
			assertThat(calendarDate.getUser().getId()).isEqualTo(mockUserId));

	}

	@Test
	void 캘린더를_아이디로_조회한다() {

		// given
		Long mockCalendarDateId = 1L;
		CalendarDate mockCalendarDate = getMockCalendarDate();

		when(calendarDateRepository.findById(mockCalendarDateId)).thenReturn(Optional.of(mockCalendarDate));

		// when
		CalendarDate result = calendarDateService.findById(mockCalendarDateId);

		// then
		assertNotNull(result);
		assertEquals(mockCalendarDate, result);

	}

	@Test
	void 캘린더에_메모를_작성한다() {

		// given
		Long mockCalendarDateId = 1L;
		String newMemo = "new memo";
		CalendarDate mockCalendarDate = getMockCalendarDate();

		when(calendarDateRepository.findById(mockCalendarDateId)).thenReturn(Optional.of(mockCalendarDate));

		// when
		calendarDateService.writeMemo(mockCalendarDateId, newMemo);

		// then
		assertEquals(newMemo, mockCalendarDate.getMemo());

	}

	@Test
	void 캘린더를_유저_아이디로_생성한다() {

		// given
		LocalDate mockDate = LocalDate.of(2025, 3, 1);
		String mockMemo = "memo";
		String mockUserId = "test-user";
		User mockUser = User.builder().id(mockUserId).build();

		when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));
		when(calendarDateRepository.save(any(CalendarDate.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		CalendarDate result = calendarDateService.create(mockUserId, mockDate, mockMemo);

		// then
		assertNotNull(result);
		assertEquals(mockUserId, result.getUser().getId());
		assertEquals(mockDate, result.getDate());
		assertSame(mockMemo, result.getMemo());

	}

	CalendarDate getMockCalendarDate() {
		return CalendarDate.builder()
			.user(new User())
			.date(LocalDate.of(2025, 3, 1))
			.memo("memo 1")
			.build();
	}

}