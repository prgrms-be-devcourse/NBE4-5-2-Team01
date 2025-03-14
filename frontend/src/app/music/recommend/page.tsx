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
  const [recentTracks, setRecentTracks] = useState([]);
  const [moodTracks, setMoodTracks] = useState([]);
  const [selectedMood, setSelectedMood] = useState("행복");

  const API_URL = "http://localhost:8080/api/v1/music/spotify";
  const username = "유저";
  const artistName = "aespa";

  const recentTrackRef = useRef(null);
  const moodTrackRef = useRef(null);

  const [isAtStartRecent, setIsAtStartRecent] = useState(true);
  const [isAtEndRecent, setIsAtEndRecent] = useState(false);
  const [isAtStartMood, setIsAtStartMood] = useState(true);
  const [isAtEndMood, setIsAtEndMood] = useState(false);

  useEffect(() => {
    fetchRecentTracks("6YVMFz59CuY7ngCxTxjpxE");
    fetchMoodTracks(selectedMood);
  }, [selectedMood]);

  const fetchRecentTracks = async (artistId) => {
    try {
      const jwt = localStorage.getItem("accessToken");
      const spotify = localStorage.getItem("spotifyToken");

      const response = await axios.get(
        `${API_URL}/artist/${artistId}/top-tracks`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
            "Spotify-Token": spotify,
            "Content-Type": "application/json",
          },
        }
      );
      setRecentTracks(response.data);
    } catch (error) {
      console.error("Error fetching recent tracks:", error);
    }
  };

  const fetchMoodTracks = async (mood) => {
    try {
      const jwt = localStorage.getItem("accessToken");
      const spotify = localStorage.getItem("spotifyToken");

      const response = await axios.get(`${API_URL}/search?keyword=${mood}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
          "Spotify-Token": spotify,
          "Content-Type": "application/json",
        },
      });
      setMoodTracks(response.data);
    } catch (error) {
      console.error("Error fetching mood tracks:", error);
    }
  };

  const moodOptions = [
    "행복",
    "슬픔",
    "에너지",
    "편안함",
    "사랑",
    "우울",
    "설렘",
  ];

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

  return (
    <div className="p-6 space-y-8">
      <div className="space-y-1">
        <h2 className="text-2xl font-bold">음악 추천</h2>
        <p className="text-gray-500">{username}님 맞춤 노래 추천</p>
      </div>

      {/* 🔹 최근 들은 음악 */}
      <section>
        <div className="flex justify-between items-center mb-5">
          <h3 className="text-xl font-semibold break-words w-full">
            최근 들은 <span className="text-[#3F00FF]">{artistName}</span>에
            대한 음악
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
                <p className="text-sm font-medium mt-2 break-words">
                  {track.name}
                </p>
                <p className="text-xs text-gray-500">{track.singer}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* 🔹 기분별 추천 음악 */}
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
              }`}
              onClick={() => setSelectedMood(mood)}
            >
              #{mood}
            </Button>
          ))}
        </div>

        <div className="relative">
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
                <p className="text-sm font-medium mt-2 break-words">
                  {track.name}
                </p>
                <p className="text-xs text-gray-500">{track.singer}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
