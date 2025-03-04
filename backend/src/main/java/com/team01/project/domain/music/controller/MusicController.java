package com.team01.project.domain.music.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.team01.project.domain.music.dto.MusicDto;
import com.team01.project.domain.music.service.MusicService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MusicController {

	private final MusicService musicService;

	@PostMapping
	public ResponseEntity<MusicDto> createMusic(@RequestBody MusicDto musicDto) {
		return ResponseEntity.ok(musicService.saveMusic(musicDto));
	}

	@GetMapping
	public ResponseEntity<List<MusicDto>> getAllMusic() {
		return ResponseEntity.ok(musicService.getAllMusic());
	}

	@GetMapping("/{id}")
	public ResponseEntity<MusicDto> getMusicById(@PathVariable String id) {
		return ResponseEntity.ok(musicService.getMusicById(id));
	}

	@DeleteMapping("{/id}")
	public ResponseEntity<Void> deleteMusic(@PathVariable String id) {
		musicService.deleteMusic(id);
		return ResponseEntity.noContent().build();
	}
}
