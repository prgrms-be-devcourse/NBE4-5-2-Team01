"use client";

import "@/app/music/style.css";
import { useState, useEffect } from "react";
import axios from "axios";
import { getCookie } from "@/app/utils/cookie";
import RecentTracks from "./RecentTracks";
import MoodTracks from "./MoodTracks";

const API_URL = "http://localhost:8080/api/v1";
const SPOTIFY_URL = "http://localhost:8080/api/v1/music/spotify";

export default function MusicRecommendation() {
  const [userName, setUserName] = useState("사용자");
  const [singer, setSinger] = useState("");
  const [recentTracks, setRecentTracks] = useState([]);
  const [moodTracks, setMoodTracks] = useState([]);
  const [selectedMood, setSelectedMood] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchAllData = async () => {
      try {
        setIsLoading(true);
        const fetchedUserId = await fetchUser();
        const fetchedArtistId = await fetchRandomMusic(fetchedUserId);
        setSinger(fetchedArtistId);
        const randomMood = getRandomMood();
        setSelectedMood(randomMood);
        await fetchRecentTracks(fetchedArtistId);
        await fetchMoodTracks(randomMood);
      } catch (error) {
        console.error("데이터 로드 중 오류 발생:", error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchAllData();
  }, []);

  const fetchUser = async () => {
    try {
      const jwt = localStorage.getItem("accessToken");
      const res = await axios.get(`${API_URL}/user/byToken`, {
        headers: { Authorization: `Bearer ${jwt}`, "Content-Type": "application/json" }
      });
      setUserName(res.data.nickName || res.data.name);
      return res.data.id;
    } catch (error) {
      console.error("사용자 정보 조회 실패:", error);
      throw error;
    }
  };

  const fetchRandomMusic = async (userId) => {
    try {
      const jwt = localStorage.getItem("accessToken");
      const res = await axios.get(`${API_URL}/music/recent/random/${userId}`, {
        headers: { Authorization: `Bearer ${jwt}`, "Content-Type": "application/json" }
      });
      return res.data.singerId;
    } catch (error) {
      console.error("랜덤 음악 조회 실패:", error);
      throw error;
    }
  };

  const fetchRecentTracks = async (artistId) => {
    try {
      const jwt = localStorage.getItem("accessToken");
      const res = await axios.get(`${SPOTIFY_URL}/artist/${artistId}/top-tracks`, {
        headers: { Authorization: `Bearer ${jwt}`, "Content-Type": "application/json" }
      });
      setRecentTracks(res.data);
    } catch (error) {
      console.error("최근 음악 조회 실패:", error);
      throw error;
    }
  };

  const fetchMoodTracks = async (mood) => {
    try {
      const jwt = localStorage.getItem("accessToken");
      const res = await axios.get(`${SPOTIFY_URL}/search?keyword=${mood}`, {
        headers: { Authorization: `Bearer ${jwt}`, "Content-Type": "application/json" }
      });
      setMoodTracks(res.data);
    } catch (error) {
      console.error("기분 음악 조회 실패:", error);
      throw error;
    }
  };

  const getRandomMood = () => {
    const moodOptions = ["행복", "슬픔", "에너지", "편안", "사랑", "우울", "설렘"];
    return moodOptions[Math.floor(Math.random() * moodOptions.length)];
  };

  return (
    <div className="p-6 space-y-8">
      <div className="space-y-1">
        <h2 className="text-2xl font-bold">음악 추천</h2>
        <p className="text-gray-500">{userName}님 맞춤 노래 추천</p>
      </div>
      {isLoading && <div>로딩 중...</div>}
      <RecentTracks singer={singer} tracks={recentTracks} />
      <MoodTracks mood={selectedMood} tracks={moodTracks} />
    </div>
  );
}
