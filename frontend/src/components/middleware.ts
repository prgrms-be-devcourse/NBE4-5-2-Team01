// middleware.ts
import { NextResponse } from "next/server";

// 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받는 함수
const refreshAccessToken = async (refreshToken: string) => {
  const response = await fetch("http://localhost:8080/refresh", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ refreshToken }),
  });

  if (!response.ok) {
    throw new Error("Failed to refresh access token");
  }

  const data = await response.json();
  return data.accessToken; // 새로운 액세스 토큰 반환
};

// 액세스 토큰 만료 여부를 확인하는 함수
const isAccessTokenExpired = (accessToken: string): boolean => {
  try {
    const payload = JSON.parse(atob(accessToken.split('.')[1])); // JWT payload 디코딩
    const expiration = payload.exp * 1000; // 만료 시간 (초 -> 밀리초 변환)
    return Date.now() >= expiration;
  } catch (error) {
    console.error("Error decoding token:", error);
    return true;
  }
};

export async function middleware(req: Request) {
  const accessToken = req.headers.get("Authorization")?.replace("Bearer ", "");
  const refreshToken = req.headers.get("refreshToken");  // 예시로 리프레시 토큰을 헤더에 포함했다고 가정

  if (!accessToken || !refreshToken) {
    return NextResponse.redirect(new URL("/login", req.url)); // 토큰이 없으면 로그인 페이지로 리다이렉트
  }

  // 액세스 토큰이 만료되었으면 리프레시 토큰을 사용하여 갱신
  if (isAccessTokenExpired(accessToken)) {
    try {
      const newAccessToken = await refreshAccessToken(refreshToken);
      if (!newAccessToken) {
        return NextResponse.redirect(new URL("/login", req.url)); // 리프레시 토큰이 유효하지 않으면 로그인 페이지로 리다이렉트
      }

      // 새로운 액세스 토큰을 응답에 추가
      const response = NextResponse.next();
      response.headers.set("Authorization", `Bearer ${newAccessToken}`);
      return response;
    } catch (error) {
      console.error("Error refreshing token:", error);
      return NextResponse.redirect(new URL("/login", req.url)); // 재발급 실패 시 로그인 페이지로 리다이렉트
    }
  }

  // 액세스 토큰이 유효하면 그대로 요청 처리
  return NextResponse.next();
}