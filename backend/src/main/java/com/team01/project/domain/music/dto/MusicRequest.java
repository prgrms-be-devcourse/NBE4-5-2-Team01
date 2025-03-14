package com.team01.project.domain.music.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team01.project.domain.music.entity.Music;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MusicRequest(
	@NotBlank String id,
	@NotBlank String name,
	@NotBlank String singer,
	@NotBlank String singerId,

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate releaseDate,

	@NotBlank String albumImage,
	String genre
) {
	public Music toEntity() {
		return new Music(id, name, singer, singerId, releaseDate, albumImage, genre);
	}
}
