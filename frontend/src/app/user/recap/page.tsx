"use client";

import React, { useState } from "react";
import "./recap.css";

interface MusicRecord {
  date: string;
  genre: string;
  artist: string;
}

interface ArtistCount {
  artist: string;
  count: number;
}

interface RecapData {
  genre: string;
  date: string;
  artists: ArtistCount[];
}

// 예시 데이터 (API 또는 DB로 대체)
const dummyData: MusicRecord[] = [
  { date: "2025-03-10", genre: "Rock", artist: "Artist A" },
  { date: "2025-03-10", genre: "Pop", artist: "Artist Bbb" },
  { date: "2025-03-11", genre: "Rock", artist: "Artist A" },
  { date: "2025-03-12", genre: "Jazz", artist: "Artist Cccc" },
  { date: "2025-03-12", genre: "Rock", artist: "Artist A" },
  { date: "2025-03-12", genre: "Pop", artist: "Artist B" },
  // ... 추가 데이터
];

// 가장 많이 들은 장르 계산 함수
const getMostListenedGenre = (data: MusicRecord[]): string => {
  const genreCount: Record<string, number> = {};
  data.forEach((item) => {
    genreCount[item.genre] = (genreCount[item.genre] || 0) + 1;
  });

  return Object.entries(genreCount).reduce(
    (maxGenre, [genre, count]) =>
      count > genreCount[maxGenre] ? genre : maxGenre,
    Object.keys(genreCount)[0] || ""
  );
};

// 가장 많이 기록한 날짜 계산 함수
const getMostRecordedDate = (data: MusicRecord[]): string => {
  const dateCount: Record<string, number> = {};
  data.forEach((item) => {
    dateCount[item.date] = (dateCount[item.date] || 0) + 1;
  });

  return Object.entries(dateCount).reduce(
    (maxDate, [date, count]) => (count > dateCount[maxDate] ? date : maxDate),
    Object.keys(dateCount)[0] || ""
  );
};

// 아티스트별 기록 횟수를 계산 후 내림차순 정렬하는 함수
const getFavoriteArtists = (data: MusicRecord[]): ArtistCount[] => {
  const artistCount: Record<string, number> = {};
  data.forEach((item) => {
    artistCount[item.artist] = (artistCount[item.artist] || 0) + 1;
  });

  return Object.entries(artistCount)
    .map(([artist, count]) => ({ artist, count }))
    .sort((a, b) => b.count - a.count);
};

const RecapPage = () => {
  const [view, setView] = useState<"weekly" | "monthly">("weekly");

  // 실제 환경에서는 API 호출 또는 DB 조회로 변경
  const weeklyRecap: RecapData = {
    genre: getMostListenedGenre(dummyData),
    date: getMostRecordedDate(dummyData),
    artists: getFavoriteArtists(dummyData),
  };

  const monthlyRecap: RecapData = {
    genre: getMostListenedGenre(dummyData),
    date: getMostRecordedDate(dummyData),
    artists: getFavoriteArtists(dummyData),
  };

  return (
    <div id="recap-bar">
      <div id="container">
        <div className="header">
          <div className="">
            <div className="title">
              <h1>Music Calendar Recap</h1>
            </div>

            <div className="explan">
              <p>user님이 들었던 음악들의 Recap</p>
            </div>
          </div>
          <div className="button">
            <div>
              {/* 버튼 컨트롤 */}
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  gap: "10px",
                }}
              >
                <button
                  onClick={() => setView("weekly")}
                  className={`recap-button ${
                    view === "weekly" ? "active" : ""
                  }`}
                >
                  Weekly
                </button>
                <button
                  onClick={() => setView("monthly")}
                  className={`recap-button ${
                    view === "monthly" ? "active" : ""
                  }`}
                >
                  Monthly
                </button>
              </div>
            </div>
          </div>
        </div>
        <div className="content1">
          <section>
            <h1 className="subject">
              내가 좋아하는 <span>아티스트</span>는..?
            </h1>
            {view === "weekly" && (
              <ul>
                {weeklyRecap.artists.map((item, index) => (
                  <li key={index}>
                    <div className="artist">{item.artist}</div>{" "}
                    <div className="count">{item.count}회</div>
                  </li>
                ))}
              </ul>
            )}
            {view === "monthly" && (
              <ul>
                {monthlyRecap.artists.map((item, index) => (
                  <li key={index}>
                    <div className="artist">{item.artist}</div>{" "}
                    <div className="count">{item.count}회</div>
                  </li>
                ))}
              </ul>
            )}
          </section>
        </div>
        {view === "weekly" && (
          <div className="content2">
            <div className="genre">
              <div>
                가장 많이 들은 <span>장르</span>
              </div>
              <div>{weeklyRecap.genre}</div>
            </div>
            <div className="date">
              <div>
                가장 많이 기록한 <span>날짜</span>
              </div>
              <div>
                {weeklyRecap.date} <br />
                <span>00</span>개의 노래를 기록했어요!!
              </div>
            </div>
          </div>
        )}

        {view === "monthly" && (
          <div className="content2">
            <div className="genre">
              <div>
                가장 많이 들은 <span>장르</span>
              </div>
              <div>{monthlyRecap.genre}</div>
            </div>
            <div className="date">
              <div>
                가장 많이 기록한 <span>날짜</span>
              </div>
              <div>
                {monthlyRecap.date} <br />
                <span>00</span>개의 노래를 기록했어요!!
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default RecapPage;
