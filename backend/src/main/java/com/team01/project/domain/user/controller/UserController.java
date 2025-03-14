package com.team01.project.domain.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.team01.project.domain.user.dto.SimpleUserResponse;
import com.team01.project.domain.user.repository.RefreshTokenRepository;
import com.team01.project.domain.user.service.UserService;
import com.team01.project.global.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;

	@GetMapping("/login")
	public String loginPage(Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated()) {
			System.out.println("인증확인:" + authentication);
			System.out.println("redirect front");
			return "redirect:http://localhost:3000"; // 이미 인증된 사용자는 메인 페이지로 리다이렉트
		}
		return "redirect:http://localhost:3000/login"; // 로그인 페이지를 반환
	}

	@GetMapping("/loginFailure")
	public String loginFailure(Model model) {
		model.addAttribute("error", "로그인 실패");
		return "login";  // 로그인 실패 시 사용자에게 오류 메시지 표시
	}
	//
	// @ResponseBody
	// @PostMapping("/logout")
	// public String logout(@RequestBody String userId) {
	// 	refreshTokenRepository.deleteByUserId(userId);
	// 	return "로그아웃 성공";
	// }

	@Transactional
	@GetMapping("/spotify/logout")
	public String forceLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		System.out.println("강제 로그아웃 요청 받음");

		if (authentication == null) {
			System.out.println("authentication 객체가 NULL입니다. SecurityContext에 인증 정보 없음.");
			return "redirect:https://accounts.spotify.com/en/logout"; // 프론트에서 토큰 삭제해야 함
		}

		System.out.println("로그아웃 된 유저 ID: " + authentication.getName());

		if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
			OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
			String userId = oAuth2User.getAttribute("id");

			if (userId != null) {
				System.out.println("저장된 RefreshToken 삭제: " + userId);
				refreshTokenRepository.deleteByUserId(userId); // 로그아웃 시 리프레시 토큰 삭제
			}

			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}

		request.getSession().invalidate();
		SecurityContextHolder.clearContext(); //SecurityContext 명시적으로 초기화
		System.out.println("SecurityContext 초기화 완료");

		return "redirect:https://accounts.spotify.com/en/logout";
	}

	@ResponseBody
	@GetMapping("/refresh")
	public String refreshToken(@RequestBody String refreshToken) {
		//String refreshToken = payload.get("refreshToken").toString();
		String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
		String newAccessToken = getNewSpotifyAccessToken(refreshToken);

		String newJwtToken = jwtTokenProvider.createToken(userId, newAccessToken);
		System.out.println("토큰: " + newJwtToken);
		return newJwtToken;
	}

	private String getNewSpotifyAccessToken(String refreshToken) {
		return refreshToken;
	}

	@ResponseBody
	@GetMapping("testApi")
	public Map<String, String> testApi(@AuthenticationPrincipal OAuth2User user) {
		String userId = user.getName();
		System.out.println("유저아이디 체크:" + userId);
		Map<String, String> resMap = new HashMap<>();
		resMap.put("res", "테스트 api 입니다.");
		resMap.put("userId", userId);
		return resMap;
	}

	@ResponseBody
	@GetMapping("/search")
	public List<SimpleUserResponse> search(@RequestParam(name = "q") String name) {
		return userService.search(name).stream()
			.map(SimpleUserResponse::from)
			.toList();
	}

	@ResponseBody
	@GetMapping("/byToken")
	public SimpleUserResponse getUserByToken(@RequestHeader("Authorization") String accessToken) {
		String token = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;
		String userId = jwtTokenProvider.getUserIdFromToken(token);
		return SimpleUserResponse.from(userService.getUserById(userId));
	}
}

