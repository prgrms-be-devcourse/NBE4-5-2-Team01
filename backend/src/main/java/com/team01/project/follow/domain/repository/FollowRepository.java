package com.team01.project.follow.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team01.project.follow.domain.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
