package com.team01.project.domain.music.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team01.project.domain.music.dto.MusicRequest;
import com.team01.project.domain.music.dto.MusicResponse;
import com.team01.project.domain.music.entity.Music;
import com.team01.project.domain.music.service.MusicService;
import com.team01.project.domain.music.service.SpotifyService;
import com.team01.project.domain.user.entity.RefreshToken;
import com.team01.project.domain.user.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicController {

	private final MusicService musicService;
	private final SpotifyService spotifyService;
	private final RefreshTokenRepository refreshTokenRepository;

	private String getAccessToken(String userId) {
		Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
		return refreshTokenOptional.map(RefreshToken::getRefreshToken)
			.orElseThrow(() -> new RuntimeException("Access token not found for user: " + userId));
	}

	@GetMapping("/spotify/{id}")
	@ResponseStatus(HttpStatus.OK)
	public MusicResponse getMusicFromSpotify(
		@PathVariable String id,
		@AuthenticationPrincipal OAuth2User user
	) {
		String accessToken = getAccessToken(user.getName());
		MusicRequest musicRequest = spotifyService.getTrackWithGenre(id, accessToken);
		if (musicRequest != null) {
			Music music = musicRequest.toEntity();
			return MusicResponse.fromEntity(music);
		}
		throw new IllegalArgumentException("Invalid music data");
	}

	@PostMapping("/spotify/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public MusicResponse saveMusicFromSpotify(
		@PathVariable String id,
		@AuthenticationPrincipal OAuth2User user
	) {
		String accessToken = getAccessToken(user.getName());
		MusicRequest musicRequest = spotifyService.getTrackWithGenre(id, accessToken);
		if (musicRequest != null) {
			Music savedMusic = musicService.saveMusic(musicRequest.toEntity());
			return MusicResponse.fromEntity(savedMusic);
		}
		throw new IllegalArgumentException("Invalid music data");
	}

	@GetMapping("/spotify/search")
	@ResponseStatus(HttpStatus.OK)
	public List<MusicResponse> searchTracks(
		@RequestParam String keyword,
		@AuthenticationPrincipal OAuth2User user
	) {
		String accessToken = getAccessToken(user.getName());
		List<MusicRequest> tracks = spotifyService.searchByKeyword(keyword, accessToken);
		return tracks.stream()
			.map(request -> MusicResponse.fromEntity(request.toEntity()))
			.collect(Collectors.toList());
	}

	@GetMapping("/spotify/artist/{artistId}/top-tracks")
	public List<MusicResponse> getTopTracksByArtist(
		@PathVariable String artistId,
		@AuthenticationPrincipal OAuth2User user
	) {
		String accessToken = getAccessToken(user.getName());
		List<MusicRequest> topTracks = spotifyService.getTopTracksByArtist(artistId, accessToken);
		return topTracks.stream()
			.map(request -> MusicResponse.fromEntity(request.toEntity()))
			.collect(Collectors.toList());
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<MusicResponse> getAllMusic() {
		return musicService.getAllMusic().stream()
			.map(MusicResponse::fromEntity)
			.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public MusicResponse getMusicById(@PathVariable String id) {
		return MusicResponse.fromEntity(musicService.getMusicById(id));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMusic(@PathVariable String id) {
		musicService.deleteMusic(id);
	}
}
