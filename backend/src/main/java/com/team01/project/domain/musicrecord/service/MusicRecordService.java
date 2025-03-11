package com.team01.project.domain.musicrecord.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team01.project.domain.calendardate.repository.CalendarDateRepository;
import com.team01.project.domain.music.entity.Music;
import com.team01.project.domain.music.repository.MusicRepository;
import com.team01.project.domain.musicrecord.entity.MusicRecord;
import com.team01.project.domain.musicrecord.entity.MusicRecordId;
import com.team01.project.domain.musicrecord.repository.MusicRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MusicRecordService {

	private final MusicRecordRepository musicRecordRepository;
	private final CalendarDateRepository calendarDateRepository;
	private final MusicRepository musicRepository;

	public List<Music> findMusicsByCalendarDateId(Long calendarDateId) {
		return musicRecordRepository.findByCalendarDateId(calendarDateId)
			.stream().map(MusicRecord::getMusic).toList();
	}

	public void updateMusicRecords(Long calendarDateId, List<String> newMusicIds) {

		// 1. 기존 MusicRecord 조회
		List<MusicRecord> oldMusicRecords = musicRecordRepository.findByCalendarDateId(calendarDateId);

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
				calendarDateRepository.getReferenceById(calendarDateId),
				musicRepository.getReferenceById(musicId)
			))
			.toList();

		// 6. MusicRecord 업데이트
		musicRecordRepository.deleteAll(musicRecordsToDelete);
		musicRecordRepository.saveAll(musicRecordsToAdd);

	}

}