package com.team01.project.domain.music.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.team01.project.domain.music.dto.MusicDto;
import com.team01.project.domain.music.entity.Music;
import com.team01.project.domain.music.repository.MusicRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicService {

	private final MusicRepository musicRepository;

	public MusicDto saveMusic(MusicDto musicDto) {
		Music music = musicDto.toEntity();
		Music savedMusic = musicRepository.save(music);
		return MusicDto.fromEntity(savedMusic);
	}

	public List<MusicDto> getAllMusic() {
		return musicRepository.findAll()
			.stream()
			.map(MusicDto::fromEntity)
			.collect(Collectors.toList());
	}

	public MusicDto getMusicById(String id) {
		Music music = musicRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 음악을 찾을 수 없습니다: " + id));
		return MusicDto.fromEntity(music);
	}

	public void deleteMusic(String id) {
		if (!musicRepository.existsById(id)) {
			throw new IllegalArgumentException("해당 ID의 음악을 찾을 수 없습니다: " + id);
		}
		musicRepository.deleteById(id);
	}

}
