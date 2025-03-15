"use client";
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function AuthCallback() {
  const router = useRouter();
  useEffect(() => {
    console.log("OAuth2 인증 후 URL:", window.location.href);

    const urlParams = new URLSearchParams(window.location.search);
    const jwtToken = urlParams.get("access_token");
    const spotifyToken = urlParams.get("spotify_access_token");
    const refreshToken = urlParams.get("refresh_token");

    console.log("JWT Token:", jwtToken);
    console.log("Spotify Token:", spotifyToken);

    if (jwtToken && spotifyToken && refreshToken) {
      localStorage.setItem("accessToken", jwtToken);
      localStorage.setItem("spotifyToken", spotifyToken);
      localStorage.setItem("refreshToken", refreshToken);
      console.log("토큰 저장 완료!");
      router.push("/"); //토큰 저장 후 대시보드로 이동
    } else {
      console.log("토큰을 찾을 수 없습니다!");
      router.push("/login"); //토큰이 없으면 로그인 페이지로 이동
    }
  }, [router]);

  return <p>로그인 처리 중...</p>;
}
