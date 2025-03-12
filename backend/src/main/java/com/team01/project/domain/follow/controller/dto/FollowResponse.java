package com.team01.project.domain.follow.controller.dto;

import com.team01.project.domain.follow.entity.Follow;
import com.team01.project.domain.user.dto.SimpleUserResponse;

public record FollowResponse(
	SimpleUserResponse toUser,
	SimpleUserResponse fromUser
) {

	public static FollowResponse from(Follow follow) {
		return new FollowResponse(
			SimpleUserResponse.from(follow.getToUser()),
			SimpleUserResponse.from(follow.getFromUser())
		);
	}
}
