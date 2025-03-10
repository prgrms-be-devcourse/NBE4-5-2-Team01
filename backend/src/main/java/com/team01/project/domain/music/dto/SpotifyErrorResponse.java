package com.team01.project.domain.music.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpotifyErrorResponse<T> {
	private boolean success;
	private String message;
	private T data;

	public static <T> SpotifyErrorResponse<T> success(T data) {
		return new SpotifyErrorResponse<>(true, "Success", data);
	}

	public static <T> SpotifyErrorResponse<T> error(String message) {
		return new SpotifyErrorResponse<>(false, message, null);
	}
}
