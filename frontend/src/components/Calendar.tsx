"use client";

import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import {useEffect, useState} from "react";
import {DatesSetArg, EventContentArg} from "@fullcalendar/core";
import {useRouter} from "next/navigation";

interface CalendarDate {
    id: number; // 캘린더 아이디
    date: string; // 'yyyy-MM-dd' 형식
    hasMemo: boolean; // 메모 작성 여부
    albumImage: string; // 앨범 이미지 링크
}

interface Monthly {
    monthly: CalendarDate[];
}

interface User {
    id: string;
    name: string;
    nickName: string;
}

interface FollowCount {
    followingCount: number;
    followerCount: number;
}

const BASE_URL = "http://localhost:8080/api/v1";

const Calendar: React.FC = () => {

    const [monthly, setMonthly] = useState<CalendarDate[]>([]);
    const [currentYear, setCurrentYear] = useState<number>(new Date().getFullYear());
    const [currentMonth, setCurrentMonth] = useState<number>(new Date().getMonth() + 1);
    const [user, setUser] = useState<User | null>(null);
    const [followingCount, setFollowingCount] = useState(0);
    const [followerCount, setFollowerCount] = useState(0);
    const router = useRouter();

    useEffect(() => {
        const fetchUserData = async () => {
            const token = localStorage.getItem('accessToken');

            if (!token) {
                router.push("/login");
                return;
            }

            const response = await fetch(BASE_URL + "/user/byToken", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
            });

            if (!response.ok) {
                throw new Error("사용자 정보를 불러오는 데 실패했습니다.");
            }

            const data = await response.json();
            setUser(data);
            fetchFollowCount(data.id);
        };

        fetchUserData();
    }, []);

    const fetchFollowCount = async (userId: string | undefined) => {
        const token = localStorage.getItem('accessToken');

        if (!token) {
            router.push("/login");
            return;
        }

        const response = await fetch(BASE_URL + `/follows/count/${userId}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            throw new Error("사용자 팔로우 정보를 불러오는 데 실패했습니다.");
        }

        const data: FollowCount = await response.json();
        setFollowerCount(data.followerCount);
        setFollowingCount(data.followingCount);
    };

    const fetchCalendarData = async (year: number, month: number) => {
        const token = localStorage.getItem('accessToken');

        if (!token) {
            router.push("/login");
            return;
        }

        const res = await fetch(
            BASE_URL + `/calendar?year=${year}&month=${month}`,
            {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": token ? `Bearer ${token}` : "",
                },
            }
        );

        const data: Monthly = await res.json();
        setMonthly(data.monthly);
    };

    useEffect(() => {
        fetchCalendarData(currentYear, currentMonth);
    }, [currentYear, currentMonth]);

    const handleDateChange = (arg: DatesSetArg) => {
        setCurrentYear(arg.view.currentStart.getFullYear());
        setCurrentMonth(arg.view.currentStart.getMonth() + 1);
    };

    useEffect(() => {
        const style = document.createElement("style");
        style.innerHTML = `
        .fc { background-color: #F8F7FF; color: #393D3F; } 
        .fc-daygrid-day { background-color: white; } 
        .fc-toolbar { background-color: #F8F7FF; color: #393D3F; } 
        .fc-daygrid-day-number { color: #393D3F !important; } 
        `;
        document.head.appendChild(style);
    }, []);

    const handleDayCellContent = (arg: { date: Date; dayNumberText: string }) => {
        const formattedDate = arg.date.toISOString().split("T")[0];
        const hasMemo = monthly.some((item) => item.date === formattedDate && item.hasMemo);

        return (
            <div className="relative flex justify-between items-center w-full">
                {hasMemo && <span className="w-1.5 h-1.5 bg-[#C8B6FF] rounded-full mr-1"></span>}
                <span className="ml-auto">{arg.dayNumberText.replace("일", "")}</span>
            </div>
        );
    };

    const renderEventContent = (eventInfo: EventContentArg) => {
        const imageUrl = eventInfo.event.extendedProps.albumImage;

        return (
            <div className="relative w-full h-full min-h-[50px] overflow-hidden">
                {imageUrl && (
                    <img
                        src={imageUrl}
                        alt="event"
                        className="absolute top-1/2 left-1/2 w-full -translate-x-1/2 -translate-y-1/2 object-cover"
                    />
                )}
            </div>
        );
    };

    return (
        <div className="flex flex-col w-full px-10 justify-center items-center">
            <div className="w-9/12 flex justify-end mt-4 mb-4">
                <h2 className="text-xl text-[#393D3F]">{user?.name ?? "나"}의 캘린더📆</h2>
                <div className="flex space-x-4 ml-4">
                    <button className="text-xl text-[#393D3F]">
                        {followerCount} 팔로워
                    </button>
                    <button className="text-xl text-[#393D3F]">
                        {followingCount} 팔로잉
                    </button>
                </div>
            </div>
            <div className="w-9/12">
                <FullCalendar
                    locale={"ko"}
                    height={"85vh"}
                    contentHeight="auto"
                    plugins={[dayGridPlugin, interactionPlugin]}
                    headerToolbar={{
                        left: "title",
                        right: "prevYear,prev,today,next,nextYear"
                    }}
                    initialView="dayGridMonth"
                    editable={false}
                    selectable={false}
                    selectMirror={true}
                    dayCellContent={handleDayCellContent}
                    datesSet={handleDateChange}
                    dayMaxEvents={true}
                    events={monthly.map((arg) => ({
                        date: arg.date,
                        borderColor: "#FFFFFF",
                        backgroundColor: "#FFFFFF",
                        extendedProps: {
                            albumImage: arg.albumImage
                        },
                    }))}
                    eventContent={renderEventContent}
                    stickyHeaderDates={true}
                    validRange={{
                        end: new Date(),
                    }}
                />
            </div>
        </div>
    );
};

export default Calendar;