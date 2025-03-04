package com.team01.project;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.team01.project.domain.music.controller.ApiV1MusicController;
import com.team01.project.domain.music.dto.MusicDto;
import com.team01.project.domain.music.service.MusicService;
import com.team01.project.domain.music.service.SpotifyService;

public class ApiV1MusicControllerTest {

	private MockMvc mvc;

	@Mock
	private MusicService musicService;

	@Mock
	private SpotifyService spotifyService;

	@InjectMocks
	private ApiV1MusicController apiV1MusicController;

	private String token;
	private List<MusicDto> testMusicList;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(apiV1MusicController).build();

		// 토큰 값 입력
		token = "your-token";

		// 테스트용 음악 데이터 저장
		testMusicList = Arrays.asList(
			new MusicDto(
				"1R0hxCA5R7z5TiaXBZR7Mf",
				"SOLO",
				"JENNIE",
				LocalDate.parse("2018-11-12"),
				"https://i.scdn.co/image/ab67616d0000b273d0b43791d31a569726a34064",
				"k-pop"
			),
			new MusicDto(
				"1SS0WlKhJewviwEDZ6dWj0",
				"SPOT!",
				"ZICO, JENNIE",
				LocalDate.parse("2024-04-26"),
				"https://i.scdn.co/image/ab67616d0000b2731b8ae147aceb9fc130391287",
				"k-rap, k-pop"
			),
			new MusicDto(
				"30HIJzJEUYcL9Qng15UeBo",
				"toxic till the end",
				"ROSÉ",
				LocalDate.parse("2024-12-06"),
				"https://i.scdn.co/image/ab67616d0000b273a9fb6e00986e42ad4764b1f3",
				"k-pop"
			)
		);

		for (MusicDto music : testMusicList) {
			when(musicService.getMusicById(music.getId())).thenReturn(music);
			when(musicService.saveMusic(any())).thenReturn(music);
		}

		when(musicService.getAllMusic()).thenReturn(testMusicList);
	}

}
