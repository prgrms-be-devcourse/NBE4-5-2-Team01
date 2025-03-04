package com.team01.project.follow.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team01.project.follow.domain.Follow;
import com.team01.project.follow.domain.repository.FollowRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommandFollowService {

	private final FollowRepository followRepository;

	public void create(Long followingId) {
		followRepository.save(new Follow(followingId, 0L));
	}
}
