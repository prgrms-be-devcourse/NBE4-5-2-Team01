package com.team01.project.follow.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team01.project.follow.domain.Follow;
import com.team01.project.follow.repository.FollowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryFollowService {

	private final FollowRepository followRepository;

	public List<Follow> findFollowing(Long fromUserId) {
		return followRepository.findByFromUserId(fromUserId);
	}
}
