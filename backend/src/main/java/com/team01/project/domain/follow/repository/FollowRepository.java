package com.team01.project.domain.follow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team01.project.domain.follow.entity.Follow;
import com.team01.project.domain.user.entity.User;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	Optional<Follow> findByToUserIdAndFromUserId(User toUser, User fromUser);

	List<Follow> findByFromUserId(User fromUser);

	List<Follow> findByToUserId(User toUser);
}
