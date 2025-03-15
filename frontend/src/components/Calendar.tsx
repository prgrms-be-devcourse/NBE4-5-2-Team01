"use client";

import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import {useEffect} from "react";

const Calendar: React.FC = () => {

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