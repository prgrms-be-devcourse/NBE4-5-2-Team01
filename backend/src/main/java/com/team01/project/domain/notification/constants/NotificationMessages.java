package com.team01.project.domain.notification.constants;

import java.util.Map;

public class NotificationMessages {

	// 메시지 키와 필요한 파라미터 개수에 맞는 메시지 포맷을 맵으로 저장
	public static final Map<String, String> DEFAULT_MESSAGES = Map.of(
			"DAILY CHALLENGE", "%s님, 하루 한 곡 기록 도전해보세요! 📅",    // 기본 설정 21:00
			"SHARE MUSIC", "%s님, 좋아하는 음악을 공유해보세요! 🎶",    // 음악을 캘린더에 기록 시
			"BUILD PLAYLIST", "%s님, 꾸준한 기록으로 나만의 플레이리스트를 만들어보세요! 🌟",    // 18:00
			"YEAR HISTORY", "%s님, 1년 전에는 어떤 음악을 기록했는지 확인해보세요!",    // 09:00
			"FOLLOW REQUEST", "%s님이 팔로우를 요청했습니다.",    // 미구현
			"FOLLOW ACCEPTED", "%s님이 팔로우 요청을 수락했습니다.",    // 미구현
			"FOLLOWING", "%s님이 회원님을 팔로우하기 시작했습니다."    // 팔로우 시
	);

}