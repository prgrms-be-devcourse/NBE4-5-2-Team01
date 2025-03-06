package com.team01.project.follow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team01.project.follow.domain.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	Optional<Follow> findByToUserIdAndFromUserId(Long toUserId, Long fromUserID);
}
