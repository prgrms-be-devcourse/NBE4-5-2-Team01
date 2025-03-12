package com.team01.project.domain.follow.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team01.project.domain.follow.controller.dto.FollowResponse;
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

	public List<FollowResponse> findFollowing(String currentUserId, String userId) {
		User currentUser = userRepository.getById(currentUserId);
		User user = userRepository.getById(userId);

		return followRepository.findByFromUserId(user).stream()
			.map(follow -> FollowResponse.of(follow.getFromUser(), currentUser != null && checkFollow(follow.getFromUser(), currentUser)))
			.toList();
	}

	public List<FollowResponse> findFollower(String currentUserId, String userId) {
		User currentUser = userRepository.getById(currentUserId);
		User user = userRepository.getById(userId);

		return followRepository.findByToUserId(user).stream()
			.map(follow -> FollowResponse.of(follow.getToUser(), currentUser != null && checkFollow(follow.getToUser(), currentUser)))
			.toList();
	}

	private boolean checkFollow(User user, User currentUser) {
		return followRepository.existsByToUserAndFromUser(user, currentUser);
	}
}
