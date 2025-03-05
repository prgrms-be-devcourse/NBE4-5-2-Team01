package com.team01.project.domain.music.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team01.project.domain.music.dto.MusicDto;
import com.team01.project.domain.music.entity.Music;
import com.team01.project.domain.music.service.MusicService;
import com.team01.project.domain.music.service.SpotifyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/music")
@RequiredArgsConstructor
public class ApiV1MusicController {

	private final MusicService musicService;
	private final SpotifyService spotifyService;

	@GetMapping("/spotify/{id}")
	@ResponseStatus(HttpStatus.OK)
	public MusicDto getMusicFromSpotify(
		@PathVariable String id,
		@RequestHeader(value = "Authorization") String accessToken
	) {
		return spotifyService.getTrackWithGenre(id, accessToken);
	}

	@PostMapping("/spotify/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public MusicDto saveMusicFromSpotify(
		@PathVariable String id,
		@RequestHeader("Authorization") String accessToken
	) {
		MusicDto musicDto = spotifyService.getTrackWithGenre(id, accessToken);
		if (musicDto != null) {
			Music music = musicDto.toEntity();
			Music savedMusic = musicService.saveMusic(music);
			return MusicDto.fromEntity(savedMusic);
		}
		throw new IllegalArgumentException("Invalid music data");
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<MusicDto> getAllMusic() {
		return musicService.getAllMusic();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public MusicDto getMusicById(@PathVariable String id) {
		return musicService.getMusicById(id);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMusic(@PathVariable String id) {
		musicService.deleteMusic(id);
	}
}
