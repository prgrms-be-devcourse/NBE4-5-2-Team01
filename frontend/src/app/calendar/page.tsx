"use client";

import React, { useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import {
  addMonths,
  subMonths,
  format,
  startOfMonth,
  endOfMonth,
  startOfWeek,
  endOfWeek,
  eachDayOfInterval,
  isSameMonth,
} from "date-fns";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

// 슬라이더 애니메이션 Variants
const sliderVariants = {
  enter: (direction: number) => ({
    x: direction > 0 ? "100%" : "-100%",
  }),
  center: { x: 0 },
  exit: (direction: number) => ({
    x: direction > 0 ? "-100%" : "100%",
  }),
};

export default function MusicCalendar() {
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [albumRecords, setAlbumRecords] = useState<{ [date: string]: string }>(
    {}
  );
  const [direction, setDirection] = useState<number>(0);

  // 이번 달의 시작, 끝, 그리고 캘린더에 표시할 날짜 범위 계산
  const monthStart = startOfMonth(currentMonth);
  const monthEnd = endOfMonth(currentMonth);
  const startDate = startOfWeek(monthStart, { weekStartsOn: 0 });
  const endDate = endOfWeek(monthEnd, { weekStartsOn: 0 });
  const days = eachDayOfInterval({ start: startDate, end: endDate });
  const daysOfWeek = ["일", "월", "화", "수", "목", "금", "토"];

  // 날짜 클릭 시: 현재 달이 아니라면 해당 달로 전환 후 상세 영역 열기
  const handleDateClick = (day: Date) => {
    const dateStr = format(day, "yyyy-MM-dd");
    if (!isSameMonth(day, currentMonth)) {
      setDirection(day > currentMonth ? 1 : -1);
      setCurrentMonth(day);
    }
    setSelectedDate(dateStr);
  };

  // 데모용 앨범 커버 추가
  const handleAddAlbumCover = () => {
    if (!selectedDate) return;
    const dummyCover = "/dummy-album.jpg"; // public 폴더에 이미지 파일 필요
    setAlbumRecords((prev) => ({
      ...prev,
      [selectedDate]: dummyCover,
    }));
  };

  // 이전/다음 달 이동
  const prevMonth = () => {
    setDirection(-1);
    setCurrentMonth(subMonths(currentMonth, 1));
    setSelectedDate(null);
  };

  const nextMonth = () => {
    setDirection(1);
    setCurrentMonth(addMonths(currentMonth, 1));
    setSelectedDate(null);
  };

  return (
    <div className="flex flex-col items-center min-h-screen bg-gradient-to-br from-purple-300 to-blue-300 p-8">
      <motion.h1
        className="text-4xl font-bold text-white mb-6"
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        🎵 Music Calendar 🎶
      </motion.h1>

      {/* 달 이동 및 현재 달 제목 */}
      <div className="flex items-center mb-4">
        <Button onClick={prevMonth} className="mr-4 bg-white text-purple-500">
          이전 달
        </Button>
        <h2 className="text-2xl text-white">
          {format(currentMonth, "yyyy년 MMMM")}
        </h2>
        <Button onClick={nextMonth} className="ml-4 bg-white text-purple-500">
          다음 달
        </Button>
      </div>

      {/* 캘린더 카드 */}
      <Card className="w-full max-w-4xl p-6 bg-white rounded-2xl shadow-lg overflow-hidden">
        {/* 슬라이더 컨테이너: relative와 명시적 높이를 부여 */}
        <div className="relative h-[700px]">
          <CardContent>
            <AnimatePresence custom={direction}>
              <motion.div
                key={format(currentMonth, "yyyy-MM")}
                custom={direction}
                variants={sliderVariants}
                initial="enter"
                animate="center"
                exit="exit"
                transition={{ duration: 0.5 }}
                className="absolute top-0 left-0 w-full h-full"
              >
                {/* 요일 헤더 */}
                <div className="grid grid-cols-7 mb-2 text-center font-semibold">
                  {daysOfWeek.map((day, index) => (
                    <div key={index} className="py-2">
                      {day}
                    </div>
                  ))}
                </div>
                {/* 날짜 셀 */}
                <div className="grid grid-cols-7 gap-0 text-center">
                  {days.map((day, index) => {
                    const formattedDate = format(day, "yyyy-MM-dd");
                    const isCurrent = isSameMonth(day, currentMonth);
                    const albumCover = albumRecords[formattedDate];

                    return (
                      <motion.div
                        key={index}
                        onClick={() => handleDateClick(day)}
                        className={`relative p-2 ${
                          isCurrent
                            ? "bg-white hover:bg-purple-200"
                            : "bg-gray-300 opacity-50"
                        } border-t border-b last:border-r`}
                        whileHover={{ scale: 1.05 }}
                        whileTap={{ scale: 0.95 }}
                      >
                        {/* 날짜 숫자 (중앙 정렬) */}
                        <div className="absolute inset-0 flex items-center justify-center text-xl font-bold">
                          {format(day, "d")}
                        </div>
                        {/* 앨범 커버 이미지 또는 빈 배경 */}
                        {albumCover ? (
                          <img
                            src={albumCover}
                            alt={`Album cover for ${formattedDate}`}
                            className="w-full h-20 object-cover rounded-lg"
                          />
                        ) : (
                          <div className="w-full h-20 rounded-lg"></div>
                        )}
                      </motion.div>
                    );
                  })}
                </div>
              </motion.div>
            </AnimatePresence>
          </CardContent>
        </div>
      </Card>

      {/* 선택된 날짜 상세 영역 */}
      {selectedDate && (
        <motion.div
          className="mt-6 w-full max-w-4xl p-6 bg-gray-100 rounded-2xl shadow-lg"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <h2 className="text-xl font-bold text-purple-600 mb-4">
            📅 {selectedDate}의 음악 기록
          </h2>
          <div className="flex items-center">
            <Button
              onClick={handleAddAlbumCover}
              className="bg-blue-500 text-white px-4 py-2 rounded-lg shadow-md mr-4"
            >
              앨범 커버 추가
            </Button>
            {albumRecords[selectedDate] && (
              <img
                src={albumRecords[selectedDate]}
                alt={`Album cover for ${selectedDate}`}
                className="w-20 h-20 object-cover rounded-lg shadow-md"
              />
            )}
          </div>
        </motion.div>
      )}
    </div>
  );
}
