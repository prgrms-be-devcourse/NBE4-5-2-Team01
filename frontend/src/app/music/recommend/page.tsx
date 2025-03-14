"use client";

import "@/app/music/recommend/style.css";
import { useState, useEffect, useRef } from "react";
import axios from "axios";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faChevronLeft,
  faChevronRight,
} from "@fortawesome/free-solid-svg-icons";
import { Button } from "@/components/ui/button";

export default function MusicRecommendation() {
  const API_URL = "http://localhost:8080/api/v1";
  const SPOTIFY_URL = "http://localhost:8080/api/v1/music/spotify";

  const [userId, setUserId] = useState("");
  const [userName, setUserName] = useState("사용자");

  const [singer, setSinger] = useState("");

  const [recentTracks, setRecentTracks] = useState([]);
  const [moodTracks, setMoodTracks] = useState([]);

  const moodOptions = ["행복", "슬픔", "에너지", "편안", "사랑", "우울", "설렘"];
  const [selectedMood, setSelectedMood] = useState("행복");
  const [isFetchingMoodTracks, setIsFetchingMoodTracks] = useState(false);
  const [isLoading, setIsLoading] = useState(false); // ✅ 로딩 화면 상태

  const recentTrackRef = useRef(null);
  const moodTrackRef = useRef(null);

  const [isAtStartRecent, setIsAtStartRecent] = useState(true);
  const [isAtEndRecent, setIsAtEndRecent] = useState(false);
  const [isAtStartMood, setIsAtStartMood] = useState(true);
  const [isAtEndMood, setIsAtEndMood] = useState(false);

  useEffect(() => {
    const fetchAllData = async () => {
      try {
        // 1️⃣ fetchUser() 실행 후 userId 가져오기
        const fetchedUserId = await fetchUser();
  
        // 2️⃣ fetchRandomMusic(fetchedUserId) 실행 후 완료되면 fetchRecentTracks() 실행
        const fetchedArtistId = await fetchRandomMusic(fetchedUserId);
  
        // 3️⃣ fetchRecentTracks() 실행 후 fetchMoodTracks(selectedMood) 실행
        await fetchRecentTracks(fetchedArtistId);
  
        // 4️⃣ 마지막으로 fetchMoodTracks(selectedMood) 실행
        await fetchMoodTracks(selectedMood);
      } catch (error) {
        console.error("데이터 로드 중 오류 발생:", error);
      }
    };
  
    fetchAllData();
  }, [selectedMood]);
  
  const fetchUser = async () => {
    try {
      const jwt = localStorage.getItem("accessToken");

      const res = await axios.get(`${API_URL}/user/byToken`, {
        headers: { Authorization: `Bearer ${jwt}`,
        "Content-Type": "application/json", },
      });

      const userId = res.data.id;
      const { name, nickName } = res.data;

      setUserId(userId);
      setUserName(nickName || name);

      return userId;
    } catch (error) {
      console.error("사용자 정보 조회 실패:", error);
      throw error;
    }
  };

  const fetchRandomMusic = async (userId) => {
    try {
      const jwt = localStorage.getItem("accessToken");
      const spotify = localStorage.getItem("spotifyToken");

      const randomRes = await axios.get(`${API_URL}/music/recent/random/${userId}`, {
        headers: { Authorization: `Bearer ${jwt}`,
        "Content-Type": "application/json", },
      });
      setSinger(randomRes.data.singer);

      return randomRes.data.singerId;
    } catch (error) {
      console.error("랜덤 음악 조회 실패:", error);
      throw error;
    }
  };

  const fetchRecentTracks = async (artistId) => {
    try {
      const jwt = localStorage.getItem("accessToken");
      const spotify = localStorage.getItem("spotifyToken");

      const res = await axios.get(
        `${SPOTIFY_URL}/artist/${artistId}/top-tracks`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
            "Spotify-Token": spotify,
            "Content-Type": "application/json",
          },
        }
      );
      setRecentTracks(res.data);
    } catch (error) {
      console.error("최근 음악 조회 실패:", error);
      throw error;
    }
  };

  const fetchMoodTracks = async (mood) => {
    if (isFetchingMoodTracks) return;

    try {
      const jwt = localStorage.getItem("accessToken");
      const spotify = localStorage.getItem("spotifyToken");

      const res = await axios.get(`${SPOTIFY_URL}/search?keyword=${mood}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
          "Spotify-Token": spotify,
          "Content-Type": "application/json",
        },
      });
      setMoodTracks(res.data);
    } catch (error) {
      console.error("기분 음악 조회 실패:", error);
      throw error;
    } finally {
      setIsFetchingMoodTracks(false); // ✅ 요청 완료 후 상태 해제
      setIsLoading(false); // ✅ 로딩 화면 숨기기
    }
  };

  const handleMoodChange = (mood) => {
    if (isFetchingMoodTracks) return;
    setIsFetchingMoodTracks(true);
    setIsLoading(true);
    setSelectedMood(mood);
    fetchMoodTracks(mood);
  };

  const updateScrollState = (ref, setStart, setEnd) => {
    if (ref.current) {
      const { scrollLeft, scrollWidth, clientWidth } = ref.current;
      setStart(scrollLeft === 0);
      setEnd(scrollLeft + clientWidth >= scrollWidth);
    }
  };

  useEffect(() => {
    if (recentTrackRef.current) {
      recentTrackRef.current.addEventListener("scroll", () =>
        updateScrollState(recentTrackRef, setIsAtStartRecent, setIsAtEndRecent)
      );
    }
    if (moodTrackRef.current) {
      moodTrackRef.current.addEventListener("scroll", () =>
        updateScrollState(moodTrackRef, setIsAtStartMood, setIsAtEndMood)
      );
    }
  }, []);

  const scrollLeft = (ref) => {
    if (ref.current) {
      ref.current.scrollTo({ left: 0, behavior: "smooth" });
    }
  };

  const scrollRight = (ref) => {
    if (ref.current) {
      ref.current.scrollTo({
        left: ref.current.scrollWidth,
        behavior: "smooth",
      });
    }
  };

  const LoadingScreen = () => {
    return (
      <div className="absolute inset-0 flex justify-center items-center bg-white z-50"
        style={{
          backgroundColor: "rgba(255, 255, 255, 0.7)", // ✅ 투명도 적용
        }}
      >
        <div className="text-center">
          <p className="text-lg font-semibold text-gray-800">🎵 추천 음악을 불러오는 중...</p>
          <div className="w-8 h-8 mt-2 border-t-4 border-blue-500 border-solid rounded-full animate-spin"></div>
        </div>
      </div>
    );
  };

  return (
    <div className="p-6 space-y-8">
      <div className="space-y-1">
        <h2 className="text-2xl font-bold">음악 추천</h2>
        <p className="text-gray-500">{userName}님 맞춤 노래 추천</p>
      </div>

      <section>
        <div className="flex justify-between items-center mb-5">
          <h3 className="text-xl font-semibold break-words w-full">
            최근 들은 <span className="text-[#3F00FF]">{singer}</span>의
            인기 음악
          </h3>
          <div className="flex space-x-2">
            <button
              onClick={() => scrollLeft(recentTrackRef)}
              className={`px-3 transition-colors ${
                isAtStartRecent ? "text-gray-300 cursor-default" : "text-black"
              }`}
              disabled={isAtStartRecent}
            >
              <FontAwesomeIcon icon={faChevronLeft} />
            </button>
            <button
              onClick={() => scrollRight(recentTrackRef)}
              className={`px-3 transition-colors ${
                isAtEndRecent ? "text-gray-300 cursor-default" : "text-black"
              }`}
              disabled={isAtEndRecent}
            >
              <FontAwesomeIcon icon={faChevronRight} />
            </button>
          </div>
        </div>

        <div className="relative">
          <div
            ref={recentTrackRef}
            className="flex gap-4 overflow-x-auto hide-scrollbar whitespace-nowrap"
            onWheel={(e) => {
              e.preventDefault();
              recentTrackRef.current.scrollLeft += e.deltaY;
            }}
          >
            {recentTracks.map((track) => (
              <div key={track.id} className="w-40 flex-shrink-0">
                <img
                  src={track.albumImage}
                  alt={track.name}
                  className="rounded-lg w-full h-auto"
                />
                <p className="text-sm font-medium mt-2 break-words track-title">
                  {track.name}
                </p>
                <p className="text-xs text-gray-500 track-artist">{track.singer}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section>
        <div className="flex justify-between items-center">
          <h3 className="text-xl font-semibold mb-2">
            기분이 <span className="text-[#3F00FF]">{selectedMood}</span>할 때
            추천하는 음악
          </h3>
          <div className="flex space-x-2">
            <button
              onClick={() => scrollLeft(moodTrackRef)}
              className={`px-3 transition-colors ${
                isAtStartMood ? "text-gray-300 cursor-default" : "text-black"
              }`}
              disabled={isAtStartMood}
            >
              <FontAwesomeIcon icon={faChevronLeft} />
            </button>
            <button
              onClick={() => scrollRight(moodTrackRef)}
              className={`px-3 transition-colors ${
                isAtEndMood ? "text-gray-300 cursor-default" : "text-black"
              }`}
              disabled={isAtEndMood}
            >
              <FontAwesomeIcon icon={faChevronRight} />
            </button>
          </div>
        </div>

        <div className="flex flex-wrap gap-1 mb-5">
          {moodOptions.map((mood) => (
            <Button
            key={mood}
            className={`rounded-full px-4 py-2 ${
              selectedMood === mood ? "mood-button-active" : "mood-button"
            } ${isFetchingMoodTracks ? "cursor-not-allowed opacity-50" : ""}`} // ✅ 즉시 UI 변경
            onClick={() => handleMoodChange(mood)}
            disabled={isFetchingMoodTracks} // ✅ 즉시 비활성화
          >
            #{mood}
          </Button>
          ))}
        </div>

        <div className="relative">
          {isLoading && <LoadingScreen />} {/* ✅ 로딩 중이면 표시 */}
          <div
            ref={moodTrackRef}
            className="flex gap-4 overflow-x-auto hide-scrollbar whitespace-nowrap"
            onWheel={(e) => {
              e.preventDefault();
              moodTrackRef.current.scrollLeft += e.deltaY;
            }}
          >
            {moodTracks.map((track) => (
              <div key={track.id} className="w-40 flex-shrink-0">
                <img
                  src={track.albumImage}
                  alt={track.name}
                  className="rounded-lg w-full h-auto"
                />
                <p className="text-sm font-medium mt-2 break-words track-title">
                  {track.name}
                </p>
                <p className="text-xs text-gray-500 track-artist">{track.singer}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
