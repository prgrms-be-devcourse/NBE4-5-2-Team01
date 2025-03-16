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
      // 토큰들을 로컬 스토리지에 저장
      localStorage.setItem("accessToken", jwtToken);
      localStorage.setItem("spotifyToken", spotifyToken);
      localStorage.setItem("refreshToken", refreshToken);

      // 토큰들을 쿠키에 저장
      const cookieOptions = "; path=/; samesite=strict";
      document.cookie = `accessToken=${jwtToken}${cookieOptions}; max-age=3600`;
      document.cookie = `refreshToken=${refreshToken}${cookieOptions}; max-age=86400`;

      console.log("토큰 저장 완료!");

      // 홈으로 리다이렉트
      window.location.href = "/";
    } else {
      console.log("토큰을 찾을 수 없습니다!");
      router.push("/login");
    }
  }, [router]);

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-white">
      <div className="text-center">
        <div className="mb-4">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto"></div>
        </div>
        <p className="text-lg font-medium text-gray-600">로그인 처리 중...</p>
      </div>
    </div>
  );
}
