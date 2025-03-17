"use client";

import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from "@/components/ui/carousel";
import { Card, CardContent } from "@/components/ui/card";
import {useEffect, useState} from "react";
import {useParams, useRouter, useSearchParams} from "next/navigation";

interface Music {
    id: string;
    name: string;
    singer: string;
    singerId: string;
    releaseDate: string;
    albumImage: string;
    genre: string;
}

interface MusicRecord {
    id: number;
    date: string;
    memo: string;
    musics: Music[];
}

export default function MusicDetailPage() {
    const [musicRecord, setMusicRecord] = useState<MusicRecord>();
    const [currentYear, setCurrentYear] = useState<number>(new Date().getFullYear());
    const [currentMonth, setCurrentMonth] = useState<number>(new Date().getMonth() + 1);
    const [currentDay, setCurrentDay] = useState<number>(new Date().getDay());
    const searchParams = useSearchParams();
    const [isReadOnly, setIsReadOnly] = useState(false);
    const params = useParams();
    const router = useRouter();

    useEffect(() => {
        if (!searchParams.has("readOnly")) {
            setIsReadOnly(false);
        } else {
            const value = searchParams.get("readOnly");
            setIsReadOnly(value === null || value === "true");
        }
    }, [searchParams]);

    useEffect(() => {
        const fetchMusicRecords = async () => {
            const res = await fetch(
                `http://localhost:8080/api/v1/calendar/${params.id}`,
                {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    credentials: "include",
                }
            );

            const data: MusicRecord = await res.json();
            const [year, month, day] = data.date.split("-");

            setMusicRecord(data);
            setCurrentYear(parseInt(year, 10));
            setCurrentMonth(parseInt(month, 10));
            setCurrentDay(parseInt(day, 10));
        };

        fetchMusicRecords();
    }, []);

    const handleButtonClick = () => {
        router.push(`/calendar/record?id=${musicRecord!.id}`);
    }

    return <div className="flex flex-col items-center justify-center w-full max-w-3xl mx-auto mt-3">
        <div className="w-full max-w-2xl flex justify-between mb-3">
            <h2 className="text-lg text-[#393D3F]">{currentYear}년 {currentMonth}월 {currentDay}일</h2>
            {
                !isReadOnly && <button className="text-lg text-[#393D3F]" onClick={handleButtonClick}>수정하기</button>
            }
        </div>
        <Carousel className="w-full max-w-2xl">
            <CarouselContent>
                {musicRecord?.musics?.map((music) => <CarouselItem key={music.id} className="flex justify-center h-full">
                    <Card className="w-full h-full border-1 border-gray-300">
                        <CardContent className="flex flex-col items-center p-4">
                            <img
                                src={music.albumImage}
                                alt={music.name}
                                className="w-full h-60 object-cover rounded-lg"
                            />
                            <h2 className="text-lg font-semibold text-[#393D3F] mt-2">{music.name}</h2>
                            <p className="text-sm text-gray-500">{music.singer}</p>
                            <p className="text-sm text-gray-400">{music.genre}</p>
                            <p className="text-sm text-gray-400">{music.releaseDate.replaceAll("-", ".")}</p>
                        </CardContent>
                    </Card>
                </CarouselItem>)}
            </CarouselContent>
            <CarouselPrevious />
            <CarouselNext />
        </Carousel>

        {/* 메모 영역 */}
        <div className="w-full max-w-2xl mt-6 p-4 rounded-lg shadow">
            <h3 className="text-md font-medium text-[#393D3F]">메모</h3>
            <p className="mt-2 text-sm text-[#393D3F]">{musicRecord?.memo}</p>
        </div>
    </div>;
}