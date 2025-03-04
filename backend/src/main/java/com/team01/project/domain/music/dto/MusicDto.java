package com.team01.project.domain.music.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.team01.project.domain.music.entity.Music;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MusicDto {

	private String id;
	private String name;
	private String singer;
	private LocalDate releaseDate;
	private String albumImage;
	private String genre;

	public static MusicDto fromSpotifyResponse(SpotifyTrackResponse track, List<String> artistGenres) {
		// 여러 아티스트 이름을 ','로 결합
		String singer = track.getArtists().stream()
			.map(SpotifyTrackResponse.Artist::getName)
			.collect(Collectors.joining(", "));

		// 중복 제거 후 여러 장르를 ','로 결합
		String genre = String.join(", ", artistGenres);

		return MusicDto.builder()
			.id(track.getId())
			.name(track.getName())
			.singer(singer)
			.releaseDate(LocalDate.parse(track.getAlbum().getRelease_date(), DateTimeFormatter.ISO_DATE))
			.albumImage(track.getAlbum().getImages().get(0).getUrl())
			.genre(genre)
			.build();
	}

	public static MusicDto fromEntity(Music music) {
		return MusicDto.builder()
			.id(music.getId())
			.name(music.getName())
			.singer(music.getSinger())
			.releaseDate(music.getReleaseDate())
			.albumImage(music.getAlbumImage())
			.genre(music.getGenre())
			.build();
	}

	public Music toEntity() {
		return Music.builder()
			.id(this.id)
			.name(this.name)
			.singer(this.singer)
			.releaseDate(this.releaseDate)
			.albumImage(this.albumImage)
			.genre(this.genre)
			.build();
	}

}
