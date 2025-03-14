package com.team01.project.domain.music.controller;

import com.team01.project.domain.music.dto.MusicRequest;
import com.team01.project.domain.music.dto.MusicResponse;
import com.team01.project.domain.music.entity.Music;
import com.team01.project.domain.music.service.MusicService;
import com.team01.project.domain.music.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicController {

	private final MusicService musicService;
	private final SpotifyService spotifyService;

	@GetMapping("/spotify/{id}")
	@ResponseStatus(HttpStatus.OK)
	public MusicResponse getMusicFromSpotify(
		@PathVariable String id,
		@RequestHeader("Spotify-Token") String spotifyToken
	) {
		MusicRequest musicRequest = spotifyService.getTrackWithGenre(id, spotifyToken);
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
		@RequestHeader("Spotify-Token") String spotifyToken
	) {
		MusicRequest musicRequest = spotifyService.getTrackWithGenre(id, spotifyToken);
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
		@RequestHeader("Spotify-Token") String spotifyToken
	) {
		List<MusicRequest> tracks = spotifyService.searchByKeyword(keyword, spotifyToken);
		return tracks.stream()
			.map(request -> MusicResponse.fromEntity(request.toEntity()))
			.collect(Collectors.toList());
	}

	@GetMapping("/spotify/artist/{artistId}/top-tracks")
	public List<MusicResponse> getTopTracksByArtist(
		@PathVariable String artistId,
		@RequestHeader("Spotify-Token") String spotifyToken
	) {
		List<MusicRequest> topTracks = spotifyService.getTopTracksByArtist(artistId, spotifyToken);
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
