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

	@Test
	void 팔로우를_삭제한다() {
		// given
		Follow 저장된_팔로우 = 팔로우_저장(new Follow(1L, 0L));
		// when
		commandFollowService.delete(저장된_팔로우.getToUserId());

		// then
		Assertions.assertThat(팔로우_조회(저장된_팔로우.getToUserId())
			.isPresent()).isEqualTo(false);
	}

	private Follow 팔로우_저장(Follow follow) {
		return followRepository.save(follow);
	}

	private Optional<Follow> 팔로우_조회(Long toUserId) {
		return followRepository.findByToUserIdAndFromUserId(toUserId, 0L);
	}
}
