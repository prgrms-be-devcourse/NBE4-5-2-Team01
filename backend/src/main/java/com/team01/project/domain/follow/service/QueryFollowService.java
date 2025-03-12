package com.team01.project.domain.follow.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team01.project.domain.follow.entity.Follow;
import com.team01.project.domain.follow.repository.FollowRepository;
import com.team01.project.domain.user.entity.User;
import com.team01.project.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryFollowService {

	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	public List<Follow> findFollowing(String userId) {
		User user = userRepository.getById(userId);
		return followRepository.findByFromUserId(user);
	}

	public List<Follow> findFollower(String userId) {
		User user = userRepository.getById(userId);
		return followRepository.findByToUserId(user);
	}
}
