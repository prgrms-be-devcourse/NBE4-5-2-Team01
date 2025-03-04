package com.team01.project.domain.music.dto;

import java.time.LocalDate;

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
