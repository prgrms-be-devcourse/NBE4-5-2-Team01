package com.team01.project.domain.musicrecord.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team01.project.domain.musicrecord.entity.MusicRecord;
import com.team01.project.domain.musicrecord.entity.MusicRecordId;

public interface MusicRecordRepository extends JpaRepository<MusicRecord, MusicRecordId> {

	List<MusicRecord> findByCalendarDateId(Long calendarDateId);

}