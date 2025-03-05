package com.team01.project.domain.music.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.team01.project.domain.music.entity.Music;

public record MusicDto(
	String id,
	String name,
	String singer,

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	LocalDate releaseDate,

	String albumImage,
	String genre
) {
	public static MusicDto fromSpotifyResponse(SpotifyTrackResponse track, List<String> artistGenres) {
		// 여러 아티스트 이름을 ','로 결합
		String singer = track.getArtists().stream()
			.map(SpotifyTrackResponse.Artist::getName)
			.collect(Collectors.joining(", "));

		// 중복 제거 후 여러 장르를 ','로 결합
		String genre = String.join(", ", artistGenres);

		return new MusicDto(
			track.getId(),
			track.getName(),
			singer,
			LocalDate.parse(track.getAlbum().getReleaseDate(), DateTimeFormatter.ISO_DATE),
			track.getAlbum().getImages().get(0).getUrl(),
			genre
		);
	}

	public static MusicDto fromEntity(Music music) {
		return new MusicDto(
			music.getId(),
			music.getName(),
			music.getSinger(),
			music.getReleaseDate(),
			music.getAlbumImage(),
			music.getGenre()
		);
	}

	public Music toEntity() {
		return new Music(
			id,
			name,
			singer,
			releaseDate,
			albumImage,
			genre
		);
	}

}
