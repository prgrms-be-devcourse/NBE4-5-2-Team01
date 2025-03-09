package com.team01.project.domain.music.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team01.project.domain.music.dto.MusicRequest;
import com.team01.project.domain.music.dto.SpotifyArtistResponse;
import com.team01.project.domain.music.dto.SpotifyTrackResponse;

@Service
public class SpotifyService {

	private final WebClient webClient;
	private final ObjectMapper objectMapper;

	public SpotifyService() {
		this.webClient = WebClient.builder().baseUrl("https://api.spotify.com/v1").build();
		this.objectMapper = new ObjectMapper();
	}

	public SpotifyTrackResponse getTrackInfo(String trackId, String accessToken) {
		String url = "/tracks/" + trackId + "?market=KO";
		String token = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;

		return webClient.get()
			.uri(url)
			.headers(headers -> headers.setBearerAuth(token))
			.retrieve()
			.bodyToMono(SpotifyTrackResponse.class)
			.block();
	}

	public List<String> getArtistGenres(String artistId, String accessToken) {
		String url = "/artists/" + artistId;
		String token = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;

		SpotifyArtistResponse response = webClient.get()
			.uri(url)
			.headers(headers -> headers.setBearerAuth(token))
			.retrieve()
			.bodyToMono(SpotifyArtistResponse.class)
			.block();

		return response != null ? response.getGenres() : List.of();
	}

	public MusicRequest getTrackWithGenre(String trackId, String accessToken) {
		SpotifyTrackResponse track = getTrackInfo(trackId, accessToken);
		if (track == null) {
			return null;
		}

		List<String> artistIds = track.getArtists().stream()
			.map(SpotifyTrackResponse.Artist::getId)
			.collect(Collectors.toList());

		Set<String> allGenres = artistIds.stream()
			.flatMap(id -> getArtistGenres(id, accessToken).stream())
			.collect(Collectors.toSet());

		return new MusicRequest(
			track.getName(),
			track.getArtistsAsString(),
			LocalDate.parse(track.getAlbum().getReleaseDate(), DateTimeFormatter.ISO_DATE),
			track.getAlbum().getImages().get(0).getUrl(),
			String.join(", ", allGenres)
		);
	}

	public List<MusicRequest> searchByKeyword(String keyword, String accessToken) {
		String url = String.format("/search?q=%s&type=track&limit=10&market=KO", keyword);
		String token = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;

		String jsonResponse = webClient.get()
			.uri(url)
			.headers(headers -> headers.setBearerAuth(token))
			.retrieve()
			.bodyToMono(String.class)
			.block();

		if (jsonResponse == null) return List.of();

		try {
			JsonNode root = objectMapper.readTree(jsonResponse);
			JsonNode items = root.path("tracks").path("items");

			if (!items.isArray()) return List.of();

			List<SpotifyTrackResponse> tracks = new ArrayList<>();
			for (JsonNode item : items) {
				SpotifyTrackResponse track = objectMapper.treeToValue(item, SpotifyTrackResponse.class);
				tracks.add(track);
			}

			return tracks.stream()
				.map(track -> getTrackWithGenre(track.getId(), accessToken))
				.collect(Collectors.toList());

		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}
}
