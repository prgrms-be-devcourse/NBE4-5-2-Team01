package com.team01.project.global.init;

import java.time.LocalDateTime;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import com.team01.project.domain.follow.entity.Follow;
import com.team01.project.domain.follow.repository.FollowRepository;
import com.team01.project.domain.user.entity.User;
import com.team01.project.domain.user.repository.RefreshTokenRepository;
import com.team01.project.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BaseInit {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final FollowRepository followRepository;

	@Transactional
	@Bean
	@Order(1)
	public ApplicationRunner initUsers() {
		return args -> {
			if (userRepository.count() == 0) {
				User user = User.builder()
						.id("test1")
						.name("user1")
						.email("team1testuser1@gmail.com")
						.createdDate(LocalDateTime.now())
						.build();

				userRepository.save(user);

				User user2 = User.builder()
						.id("test2")
						.name("user2")
						.email("team1testuser2@gmail.com")
						.createdDate(LocalDateTime.now())
						.build();

				userRepository.save(user2);
			}
		};
	}

	@Bean
	@Order(2)
	public ApplicationRunner initFollow() {
		return args -> {
			if (followRepository.count() == 0) {
				User user1 = userRepository.findById("test1").get();
				User user2 = userRepository.findById("test2").get();
				Follow follow = new Follow(user1, user2);
				followRepository.save(follow);
			}
		};
	}
}
