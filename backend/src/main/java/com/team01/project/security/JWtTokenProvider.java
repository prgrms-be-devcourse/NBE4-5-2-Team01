package com.team01.project.security;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.team01.project.domain.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWtTokenProvider {
	private static final String SECRET_KEY = "secret-key";
	private static final long VALIDITY_IN_MS = 3600000L; // 1시간\
	private User user;

	//jwt 토큰 생성
	public String createToken(String userId, String spotifyAccessToken){
		Claims claims = Jwts.claims().setSubject(userId).build();
		claims.put("spotifyToken", spotifyAccessToken);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime validity = now.plus(Duration.ofMillis(VALIDITY_IN_MS));

		Date issuedAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		Date expiration = Date.from(validity.atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder()
			.setClaims(claims)
			//.setSubject(user.getNickName())
			.setIssuedAt(issuedAt)
			.setExpiration(expiration)
			.signWith(SignatureAlgorithm.ES256, SECRET_KEY)
			.compact();
	}

	//jwt 토큰 검증 및 사용자 정보 추출
	public String getUserIdFromToken(String token){

		JwtParser parser = Jwts.parser().setSigningKey(SECRET_KEY).build();
		Claims claims = parser.parseClaimsJwt(token).getBody();
		return claims.getSubject();
	}

	//jwt 토큰 유효한 형식인 지 검증
	public boolean validateToken(String token) {
		try {
			JwtParser parser = Jwts.parser().setSigningKey(SECRET_KEY).build();
			parser.parseClaimsJwt(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}
}
