package com.team01.project.domain.musicrecord.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team01.project.domain.calendardate.entity.CalendarDate;
import com.team01.project.domain.calendardate.repository.CalendarDateRepository;
import com.team01.project.domain.music.entity.Music;
import com.team01.project.domain.music.repository.MusicRepository;
import com.team01.project.domain.musicrecord.entity.MusicRecord;
import com.team01.project.domain.musicrecord.entity.MusicRecordId;
import com.team01.project.domain.musicrecord.repository.MusicRecordRepository;
import com.team01.project.domain.user.entity.User;
import com.team01.project.domain.user.repository.UserRepository;
import com.team01.project.global.permission.PermissionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MusicRecordService {

	private final MusicRecordRepository musicRecordRepository;
	private final CalendarDateRepository calendarDateRepository;
	private final MusicRepository musicRepository;
	private final UserRepository userRepository;
	private final PermissionService permissionService;

	public List<Music> findMusicsByCalendarDateId(Long calendarDateId) {
		CalendarDate calendarDate = calendarDateRepository.findById(calendarDateId)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 캘린더 날짜 기록을 찾을 수 없습니다: " + calendarDateId));

		return musicRecordRepository.findByCalendarDate(calendarDate)
			.stream().map(MusicRecord::getMusic).toList();
	}

	public Optional<MusicRecord> findOneByCalendarDateId(Long calendarDateId) {
		CalendarDate calendarDate = calendarDateRepository.findById(calendarDateId)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 캘린더 날짜 기록을 찾을 수 없습니다: " + calendarDateId));

		return musicRecordRepository.findTopByCalendarDate(calendarDate);
	}

	public void updateMusicRecords(Long calendarDateId, String loggedInUserId, List<String> newMusicIds) {
		User loggedInUser = userRepository.findById(loggedInUserId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저입니다."));

		CalendarDate calendarDate = calendarDateRepository.findById(calendarDateId)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 캘린더 날짜 기록을 찾을 수 없습니다: " + calendarDateId));

		permissionService.checkCalendarDateUpdatePermission(calendarDateId, loggedInUser);

		// 1. 기존 MusicRecord 조회
		List<MusicRecord> oldMusicRecords = musicRecordRepository.findByCalendarDate(calendarDate);

		// 2. 기존 MusicId 목록 조회
		Set<String> oldMusicIdset = oldMusicRecords.stream()
			.map(musicRecord -> musicRecord.getMusic().getId())
			.collect(Collectors.toSet());

		// 3. 새로 추가될 MusicId 목록
		Set<String> newMusicIdSet = new HashSet<>(newMusicIds);

		// 4. 삭제할 MusicRecord 목록
		List<MusicRecord> musicRecordsToDelete = oldMusicRecords.stream()
			.filter(musicRecord -> !newMusicIdSet.contains(musicRecord.getMusic().getId()))
			.toList();

		// 5. 추가할 MusicRecord 목록
		List<MusicRecord> musicRecordsToAdd = newMusicIdSet.stream()
			.filter(musicId -> !oldMusicIdset.contains(musicId))
			.map(musicId -> new MusicRecord(
				new MusicRecordId(calendarDateId, musicId),
				calendarDate,
				musicRepository.getReferenceById(musicId)
			))
			.toList();

		// 6. MusicRecord 업데이트
		musicRecordRepository.deleteAll(musicRecordsToDelete);
		musicRecordRepository.saveAll(musicRecordsToAdd);

	}

}