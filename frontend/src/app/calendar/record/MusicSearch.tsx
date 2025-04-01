"use client";

import { useState, useRef, useEffect } from "react";
import { searchSpotifyTracks } from "@/app/utils/spotifyApi";
import "./style.css";
import { useGlobalAlert } from "@/components/GlobalAlert";

const MAX_MUSIC_COUNT = 20;

export default function MusicSearch({ onSelectTrack, selectedTracks }) {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const [selectedIndex, setSelectedIndex] = useState(-1);

  const { setAlert } = useGlobalAlert();
  const containerRef = useRef(null);
  const inputRef = useRef(null);

  // 🔸 검색 결과 캐시 저장용
  const latestResultsRef = useRef([]);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (containerRef.current && !containerRef.current.contains(event.target)) {
        setResults([]); // 리스트 닫기
        setSelectedIndex(-1);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleFocus = () => {
    if (query.length > 2 && latestResultsRef.current.length > 0) {
      setResults(latestResultsRef.current);
    }
  };

  const handleSearch = async (event) => {
    const keyword = event.target.value;
    setQuery(keyword);
    setSelectedIndex(-1);

    if (keyword.length > 2) {
      const searchResults = await searchSpotifyTracks(keyword);
      latestResultsRef.current = searchResults;
      setResults(searchResults);
    } else {
      latestResultsRef.current = [];
      setResults([]);
    }
  };

  const handleKeyDown = (event) => {
    if (event.key === "ArrowDown") {
      setSelectedIndex((prev) => Math.min(prev + 1, results.length - 1));
    } else if (event.key === "ArrowUp") {
      setSelectedIndex((prev) => Math.max(prev - 1, 0));
    } else if (event.key === "Enter" && selectedIndex >= 0) {
      handleSelectTrack(results[selectedIndex]);
    }
  };

  const handleSelectTrack = (track) => {
    const isDuplicate = selectedTracks.some((t) => t.id === track.id);
    if (isDuplicate) {
      setAlert({
        code: "400-1",
        message: "이미 추가한 곡이에요.",
      });
      return;
    }

    if (selectedTracks.length >= MAX_MUSIC_COUNT) {
      setAlert({
        code: "400-2",
        message: `최대 ${MAX_MUSIC_COUNT}곡까지 추가할 수 있어요.`,
      });
      return;
    }

    onSelectTrack(track);
    setQuery("");
    setResults([]);
    setSelectedIndex(-1);
  };

  return (
    <div className="search-container" ref={containerRef}>
      <input
        type="text"
        value={query}
        onChange={handleSearch}
        onKeyDown={handleKeyDown}
        onFocus={handleFocus}
        ref={inputRef}
        placeholder="Spotify에서 검색할 곡 또는 가수를 입력하세요."
        className="search-input"
      />
      {results.length > 0 && (
        <ul className="search-results">
          {results.map((track, index) => (
            <li
              key={track.id}
              className={`search-item ${index === selectedIndex ? "selected" : ""}`}
              onClick={() => handleSelectTrack(track)}
            >
              <img src={track.albumImage} alt={track.name} />
              <div>
                <p className="font-medium">{track.name}</p>
                <p className="text-sm text-gray-500">{track.singer}</p>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
