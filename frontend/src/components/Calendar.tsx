"use client";

import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import {useEffect, useState} from "react";
import {DatesSetArg} from "@fullcalendar/core";

interface CalendarDate {
    id: number; // 캘린더 아이디
    date: string; // 'yyyy-MM-dd' 형식
    hasMemo: boolean; // 메모 작성 여부
    albumImage: string; // 앨범 이미지 링크
}

interface Monthly {
    monthly: CalendarDate[];
}

const Calendar: React.FC = () => {

    const [monthly, setMonthly] = useState<CalendarDate[]>([]);
    const [currentYear, setCurrentYear] = useState<number>(new Date().getFullYear());
    const [currentMonth, setCurrentMonth] = useState<number>(new Date().getMonth() + 1);

    const fetchCalendarData = async (year: number, month: number) => {
        const token = localStorage.getItem('accessToken');

        const res = await fetch(
            `http://localhost:8080/api/v1/calendar?year=${year}&month=${month}`,
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

    const handleDayCellContent = (arg: { dayNumberText: string; }) => {
        return arg.dayNumberText.replace("일", "");
    }

    return (
        <div className="flex w-full justify-center">
            <div className="flex w-full px-10 justify-center gap-8">
                <div className="w-9/12 mt-8">
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
                        stickyHeaderDates={true}
                        validRange={{
                            end: new Date(),
                        }}
                    />
                </div>
            </div>
        </div>
    );
};

export default Calendar;