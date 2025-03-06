package com.team01.project.follow.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.team01.project.common.service.ServiceTest;
import com.team01.project.follow.domain.Follow;
import com.team01.project.follow.repository.FollowRepository;

public class CommandFollowServiceTest extends ServiceTest {

	@Autowired
	private CommandFollowService commandFollowService;

	@Autowired
	private FollowRepository followRepository;

	@Test
	void 팔로우를_생성한다() {
		// when
		commandFollowService.create(1L);

		// then
		Assertions.assertThat(팔로우_조회(1L).isPresent()).isEqualTo(true);
	}

	private Optional<Follow> 팔로우_조회(Long toUserId) {
		return followRepository.findByToUserIdAndFromUserId(toUserId, 0L);
	}
}
