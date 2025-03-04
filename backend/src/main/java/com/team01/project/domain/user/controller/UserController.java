package com.team01.project.domain.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.team01.project.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
}
