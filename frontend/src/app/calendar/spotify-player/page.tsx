"use client";

import { useEffect, useState } from "react";
import { loadSpotifyPlayer } from "./player";

interface Music {
  id: string;
  name: string;
  singer: string;
  singerId: string;
  releaseDate: string;
  albumImage: string;
  genre: string;
  uri: string;
}

interface MusicRecord {
  id: number;
  date: string;
  memo: string;
  musics: Music[];
}

function getSpotifyAccessToken(): string | null {
  if (typeof document === "undefined") return null;
  const match = document.cookie
    .split(";")
    .map((c) => c.trim())
    .find((c) => c.startsWith("spotifyAccessToken="));

  return match ? decodeURIComponent(match.split("=")[1]) : null;
}

export default function MusicPlayer() {
  const [musicRecord, setMusicRecord] = useState<MusicRecord>();
  const [deviceId, setDeviceId] = useState<string | null>(null);
  const token = getSpotifyAccessToken();

  useEffect(() => {
    if (!token) {
      console.error("No access token found in cookies");
      return;
    }

    const stored = sessionStorage.getItem("spotify-music-record");
    if (stored) {
      try {
        setMusicRecord(JSON.parse(stored));
      } catch (err) {
        console.error("musicRecord 파싱 실패:", err);
      }
    } else {
      alert(
        "음악 데이터를 찾을 수 없습니다. 이전 페이지에서 다시 시도해주세요."
      );
    }

    loadSpotifyPlayer(token, (player, deviceId) => {
      console.log("🚀 플레이어 준비 완료, deviceId:", deviceId);
      setDeviceId(deviceId);
    });
  }, []);

  // 재생 버튼 누르면 Web API로 재생 요청
  const handlePlay = async () => {
    console.log("token:", token);
    console.log("deviceId:", deviceId);
    if (!token || !deviceId) return;

    const uris = musicRecord?.musics.map((music) => music.uri) || [];
    console.log("URIs to be played:", uris);

    const res = await fetch(
      "https://api.spotify.com/v1/me/player/play?device_id=" + deviceId,
      {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          uris: uris,
        }),
      }
    );

    if (res.ok) {
      console.log("Track is playing!");
    } else {
      console.error("Failed to play track:", await res.text());
    }
  };

  return (
    <div className="flex flex-col items-center justify-center w-full max-w-2xl mx-auto mt-10">
      <h2 className="text-2xl font-bold text-[#393D3F] mb-6">
        {musicRecord?.date} 재생 목록
      </h2>

      <ul className="w-full space-y-4">
        {musicRecord?.musics.map((music) => (
          <li
            key={music.id}
            className="flex items-center space-x-4 border p-4 rounded-lg"
          >
            <img
              src={music.albumImage}
              alt={music.name}
              className="w-16 h-16 object-cover rounded-md"
            />
            <div>
              <h3 className="text-lg font-semibold">{music.name}</h3>
              <p className="text-sm text-gray-500">{music.singer}</p>
            </div>
          </li>
        ))}
      </ul>
      <button
        onClick={handlePlay}
        className="mt-2 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
      >
        재생
      </button>
    </div>
  );
}
