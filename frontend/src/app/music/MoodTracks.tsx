"use client";

import { useRef } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faChevronLeft, faChevronRight } from "@fortawesome/free-solid-svg-icons";

const moodMapping = {
    "행복": "기분이 좋을 때",
    "슬픔": "마음이 울적할 때",
    "에너지": "활기차고 싶을 때",
    "편안": "편안함을 느끼고 싶을 때",
    "사랑": "사랑이 가득할 때",
    "우울": "우울한 기분일 때",
    "설렘": "설레는 순간일 때",
};

const MoodTracks = ({ mood, tracks }) => {
    const trackRef = useRef(null);
    const isAtStart = trackRef.current?.scrollLeft === 0;
    const isAtEnd = trackRef.current?.scrollLeft + trackRef.current?.clientWidth >= trackRef.current?.scrollWidth;

    const scrollLeft = () => trackRef.current?.scrollTo({ left: 0, behavior: "smooth" });
    const scrollRight = () => trackRef.current?.scrollTo({ left: trackRef.current.scrollWidth, behavior: "smooth" });

    return (
        <section className="mb-7">
            <div className="flex justify-between items-center mb-5">
                <h3 className="text-xl font-semibold break-words w-full">
                    <span className="point-color mr-1">{moodMapping[mood]}</span> 이런 음악 어때요?
                </h3>
                <div className="flex space-x-2">
                    <button onClick={scrollLeft} className={`px-3 ${isAtStart ? "text-gray-300 cursor-default" : "text-black"}`} disabled={isAtStart}>
                        <FontAwesomeIcon icon={faChevronLeft} />
                    </button>
                    <button onClick={scrollRight} className={`px-3 ${isAtEnd ? "text-gray-300 cursor-default" : "text-black"}`} disabled={isAtEnd}>
                        <FontAwesomeIcon icon={faChevronRight} />
                    </button>
                </div>
            </div>
            <div className="relative">
                <div ref={trackRef} className="flex gap-4 overflow-x-auto hide-scrollbar whitespace-nowrap">
                    {tracks.map(track => (
                        <div key={track.id} className="w-40 flex-shrink-0">
                            <img src={track.albumImage} alt={track.name} className="rounded-lg w-full h-auto" />
                            <p className="text-sm font-medium mt-2 break-words track-title">{track.name}</p>
                            <p className="text-xs text-gray-500 track-artist">{track.singer}</p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
};

export default MoodTracks;