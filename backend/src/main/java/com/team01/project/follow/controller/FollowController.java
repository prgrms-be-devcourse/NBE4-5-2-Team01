package com.team01.project.follow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team01.project.follow.service.CommandFollowService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

	private final CommandFollowService commandFollowService;

	@PostMapping("/{user-id}")
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@PathVariable(name = "user-id") Long userId) {
		commandFollowService.create(userId);
	}

	@DeleteMapping("/{user-id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable(name = "user-id") Long userId) {
		commandFollowService.delete(userId);
	}
}
