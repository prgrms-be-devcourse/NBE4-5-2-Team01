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
  const [isPaused, setIsPaused] = useState<boolean>(true);
  const [playerInstance, setPlayerInstance] = useState<Spotify.Player | null>(
    null
  );
  const [hasLoadedTrack, setHasLoadedTrack] = useState(false);
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

      setPlayerInstance(player);

      // 재생 상태 변화 감지
      player.addListener("player_state_changed", (state) => {
        if (!state) return;
        setIsPaused(state.paused);
        console.log("🎧 상태 변경됨: isPaused =", state.paused);
      });
    });
  }, []);

  // 재생/일시정지 버튼 누르면 자동으로 재생 <-> 일시정지 전환
  const handleTogglePlay = async () => {
    if (!playerInstance || !deviceId || !musicRecord) return;

    try {
      // 트랙이 아직 로드되지 않았다면, 먼저 플레이 API로 트랙 로드
      if (!hasLoadedTrack) {
        const uris = musicRecord.musics.map((music) => music.uri);
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

        if (!res.ok) {
          console.error("🎵 트랙 로드 실패:", await res.text());
          return;
        }

        console.log("✅ 트랙 로드 완료");
        setHasLoadedTrack(true);
        return; // 재생은 Spotify가 자동으로 해주기 때문에 여기서 return
      }

      // 트랙이 로드되어 있다면, togglePlay 실행
      await playerInstance.togglePlay();
    } catch (err) {
      console.error("🎧 토글 실패:", err);
    }
  };

  // 볼륨 조절 슬라이더
  const handleVolumeChange = async (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const volume = Number(event.target.value); // 0 ~ 1 범위
    if (!playerInstance) return;

    try {
      await playerInstance.setVolume(volume);
      console.log("🔊 볼륨 조절됨:", volume);
    } catch (err) {
      console.error("볼륨 조절 실패:", err);
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
      <div className="mt-4 flex space-x-4 items-center">
        <button
          onClick={handleTogglePlay}
          className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
        >
          {isPaused ? "▶️ 재생" : "⏸️ 일시정지"}
        </button>

        <label className="text-gray-600 text-sm">
          볼륨:
          <input
            type="range"
            min="0"
            max="1"
            step="0.01"
            onChange={handleVolumeChange}
            defaultValue="0.5"
            className="ml-2 w-32"
          />
        </label>
      </div>
    </div>
  );
}
