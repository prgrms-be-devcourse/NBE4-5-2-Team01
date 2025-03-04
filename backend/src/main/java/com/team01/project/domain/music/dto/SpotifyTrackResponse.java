package com.team01.project.domain.music.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpotifyTrackResponse {
	private String id;
	private String name;

	@JsonProperty("artists")
	private List<Artist> artists;

	private Album album;

	@Getter
	@Setter
	public static class Album {
		private String name;

		@JsonProperty("release_date")
		private String releaseDate;

		@JsonProperty("images")
		private List<Image> images;
	}

	@Getter
	@Setter
	public static class Artist {
		private String id;
		private String name;
	}

	@Getter
	@Setter
	public static class Image {
		private String url;
	}
}
