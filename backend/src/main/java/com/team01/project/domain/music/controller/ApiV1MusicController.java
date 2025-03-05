package com.team01.project.domain.music.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public ResponseEntity<MusicDto> getMusicFromSpotify(
		@PathVariable String id,
		@RequestHeader(value = "Authorization") String accessToken
	) {
		MusicDto musicDto = spotifyService.getTrackWithGenre(id, accessToken);
		return ResponseEntity.ok(musicDto);
	}

	@PostMapping("/spotify/{id}")
	public ResponseEntity<MusicDto> saveMusicFromSpotify(
		@PathVariable String id,
		@RequestHeader("Authorization") String accessToken
	) {
		MusicDto musicDto = spotifyService.getTrackWithGenre(id, accessToken);
		if (musicDto != null) {
			Music music = musicDto.toEntity();
			Music savedMusic = musicService.saveMusic(music);
			return ResponseEntity.ok(MusicDto.fromEntity(savedMusic));
		}
		return ResponseEntity.badRequest().build();
	}

	@GetMapping
	public ResponseEntity<List<MusicDto>> getAllMusic() {
		return ResponseEntity.ok(musicService.getAllMusic());
	}

	@GetMapping("/{id}")
	public ResponseEntity<MusicDto> getMusicById(@PathVariable String id) {
		return ResponseEntity.ok(musicService.getMusicById(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMusic(@PathVariable String id) {
		musicService.deleteMusic(id);
		return ResponseEntity.noContent().build();
	}
}
