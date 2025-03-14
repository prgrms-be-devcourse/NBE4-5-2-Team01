"use client";

import { useState, useEffect } from "react";
import axios from "axios";

const BASE_URL = "http://localhost:8080/api/v1/music"; // Spring Boot 서버 주소
const jwt = localStorage.getItem("accessToken");
const spotify = localStorage.getItem("spotifyToken");

export default function MusicPage() {
  const [musicList, setMusicList] = useState([]);
  const [music, setMusic] = useState(null);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [artistId, setArtistId] = useState("");
  const [topTracks, setTopTracks] = useState([]);

  // 모든 음악 조회
  useEffect(() => {
    axios
      .get(`${BASE_URL}`)
      .then((res) => setMusicList(res.data))
      .catch((err) => console.error(err));
  }, []);

  // 특정 음악 조회
  const fetchMusicById = async (id: string) => {
    try {
      const res = await axios.get(`${BASE_URL}/${id}`);
      setMusic(res.data);
    } catch (error) {
      console.error(error);
    }
  };

  // Spotify에서 음악 검색
  const searchMusic = async () => {
    try {
      const res = await axios.get(`${BASE_URL}/spotify/search`, {
        params: { keyword: searchKeyword },
        headers: {
          Authorization: `Bearer ${jwt}`,
          "Spotify-Token": spotify,
          "Content-Type": "application/json",
        },
      });
      setSearchResults(res.data);
    } catch (error) {
      console.error(error);
    }
  };

  // 특정 Spotify 음악 가져오기
  const getMusicFromSpotify = async (id: string) => {
    try {
      const res = await axios.get(`${BASE_URL}/spotify/${id}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
          "Spotify-Token": spotify,
          "Content-Type": "application/json",
        },
      });
      setMusic(res.data);
    } catch (error) {
      console.error(error);
    }
  };

  // Spotify 음악 저장
  const saveMusicFromSpotify = async (id: string) => {
    try {
      const res = await axios.post(`${BASE_URL}/spotify/${id}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
          "Spotify-Token": spotify,
          "Content-Type": "application/json",
        },
      });
      setMusic(res.data);
    } catch (error) {
      console.error(error);
    }
  };

  // 아티스트 ID로 상위 트랙 조회
  const fetchTopTracks = async () => {
    try {
      const res = await axios.get(
        `${BASE_URL}/spotify/artist/${artistId}/top-tracks`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
            "Spotify-Token": spotify,
            "Content-Type": "application/json",
          },
        }
      );
      setTopTracks(res.data);
    } catch (error) {
      console.error(error);
    }
  };

  // 음악 삭제
  const deleteMusic = async (id: string) => {
    try {
      await axios.delete(`${BASE_URL}/${id}`);
      setMusicList(musicList.filter((m) => m.id !== id));
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold">Music Controller API 테스트</h1>

      {/* 모든 음악 조회 */}
      <h2 className="mt-4 text-xl">저장된 음악 목록</h2>
      <ul>
        {musicList.map((m) => (
          <li key={m.id}>
            {m.title} - {m.artist}{" "}
            <button
              onClick={() => deleteMusic(m.id)}
              className="ml-2 text-red-500"
            >
              삭제
            </button>
          </li>
        ))}
      </ul>

      {/* 특정 음악 조회 */}
      <div className="mt-4">
        <input
          type="text"
          placeholder="음악 ID 입력"
          onChange={(e) => fetchMusicById(e.target.value)}
          className="border p-2"
        />
        {music && (
          <div className="mt-2">
            <h3>{music.title}</h3>
            <p>{music.artist}</p>
          </div>
        )}
      </div>

      {/* Spotify에서 검색 */}
      <div className="mt-4">
        <input
          type="text"
          placeholder="검색어 입력"
          value={searchKeyword}
          onChange={(e) => setSearchKeyword(e.target.value)}
          className="border p-2"
        />
        <button
          onClick={searchMusic}
          className="ml-2 p-2 bg-blue-500 text-white"
        >
          검색
        </button>
        <ul>
          {searchResults.map((m) => (
            <li key={m.id}>
              {m.title} - {m.artist}{" "}
              <button
                onClick={() => saveMusicFromSpotify(m.id)}
                className="ml-2 text-green-500"
              >
                저장
              </button>
            </li>
          ))}
        </ul>
      </div>

      {/* Spotify 음악 가져오기 */}
      <div className="mt-4">
        <input
          type="text"
          placeholder="Spotify 음악 ID 입력"
          onChange={(e) => getMusicFromSpotify(e.target.value)}
          className="border p-2"
        />
        {music && (
          <div className="mt-2">
            <h3>{music.title}</h3>
            <p>{music.artist}</p>
          </div>
        )}
      </div>

      {/* Spotify 아티스트의 상위 트랙 */}
      <div className="mt-4">
        <input
          type="text"
          placeholder="아티스트 ID 입력"
          value={artistId}
          onChange={(e) => setArtistId(e.target.value)}
          className="border p-2"
        />
        <button
          onClick={fetchTopTracks}
          className="ml-2 p-2 bg-purple-500 text-white"
        >
          상위 트랙 조회
        </button>
        <ul>
          {topTracks.map((m) => (
            <li key={m.id}>
              {m.title} - {m.artist}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
