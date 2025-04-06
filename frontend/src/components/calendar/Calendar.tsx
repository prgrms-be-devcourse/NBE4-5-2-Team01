"use client";

import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import { useEffect, useMemo, useState } from "react";
import { DatesSetArg } from "@fullcalendar/core";
import { useRouter, useSearchParams } from "next/navigation";
import { CalendarDate, Monthly } from "@/types/calendar";
import { User } from "@/types/user";
import { FollowCount } from "@/types/follow";
import { handleDayCellDidMount, handleEventDidMount } from "@/components/calendar/eventHandlers";
import { AxiosError } from "axios";
import { useGlobalAlert } from "../GlobalAlert";
import { fetchUser } from "@/lib/api/user";
import { fetchFollowCount } from "@/lib/api/follow";
import { fetchMonthlyData } from "@/lib/api/calendar";

const Calendar: React.FC = () => {
  const [monthly, setMonthly] = useState<CalendarDate[]>([]);
  const [selectedYear, setSelectedYear] = useState<number>(new Date().getFullYear());
  const [selectedMonth, setSelectedMonth] = useState<number>(new Date().getMonth() + 1);
  const [followingCount, setFollowingCount] = useState(0);
  const [followerCount, setFollowerCount] = useState(0);
  const [isCalendarOwner, setIsCalendarOwner] = useState<boolean>(false);
  const [calendarOwner, setCalendarOwner] = useState<User | null>(null);
  const [today, setToday] = useState(new Date());
  const { setAlert } = useGlobalAlert();

  const router = useRouter();
  const params = useSearchParams();
  const queryString = params.toString();

  const events = useMemo(
    () =>
      monthly.map((arg) => ({
        start: arg.date,
        display: "background",
        extendedProps: {
          albumImage: arg.albumImage,
        },
      })),
    [monthly]
  );

  useEffect(() => {
    const interval = setInterval(() => {
      setToday(new Date());
    }, 60 * 1000 * 10); // 10분마다 갱신

    return () => clearInterval(interval);
  }, []);

  // 캘린더 소유자 데이터 조회
  useEffect(() => {
    const calendarOwnerId = params.get("userId");

    async function initCalendarOwner() {
      if (calendarOwnerId === null) {
        const response = await fetchUser(`/user/byCookie`);

        const calendarOwner: User = response.data;

        setCalendarOwner(calendarOwner);
        setIsCalendarOwner(true);
      } else {
        const responseByCookie = await fetchUser(`/user/byCookie`);
        const responseById = await fetchUser(`/user/${calendarOwnerId}`)

        const currentUser: User = responseByCookie.data;
        const calendarOwner: User = responseById.data;

        setCalendarOwner(calendarOwner);
        setIsCalendarOwner(currentUser.id === calendarOwner.id);
      }
    }

    initCalendarOwner();
  }, [queryString])

  // 캘린더 소유자의 팔로잉, 팔로워 수 조회
  useEffect(() => {
    async function initFollowCount() {
      if (calendarOwner) {
        const response = await fetchFollowCount(calendarOwner.id);

        const followCount: FollowCount = response.data.data;

        setFollowingCount(followCount.followingCount);
        setFollowerCount(followCount.followerCount);
      }
    }

    initFollowCount();
  }, [calendarOwner])

  // 캘린더 소유자의 먼슬리 캘린더 데이터 조회
  useEffect(() => {
    async function initMonthly(year: number, month: number) {
      if (isCalendarOwner === null || !calendarOwner) return;

      try {
        const response = await fetchMonthlyData(
            year,
            month,
            isCalendarOwner ? undefined : calendarOwner.id
        );

        const monthly: Monthly = response.data.data;

        setMonthly(monthly.monthly);
      } catch (error) { // 예외 처리
        if (error instanceof AxiosError)
          setAlert({
            code: error.response!.status.toString(),
            message:  error.response!.data.msg,
          });

        setTimeout(() => {
          router.push("/calendar");
        }, 2000); // 2초 대기 후 이동

        return;
      }
    }

    initMonthly(selectedYear, selectedMonth);
  }, [isCalendarOwner, selectedYear, selectedMonth])

  // 페이지를 떠날 때 스타일 속성 삭제
  useEffect(() => {
    return () => {

      // 기록이 있는 날짜 셀 선택
      const cells = document.querySelectorAll(".fc-daygrid-day.has-record");

      cells.forEach((cell) => {
        if (cell instanceof HTMLElement) {
          const dateNumber = cell.querySelector(".fc-daygrid-day-number") as HTMLElement;

          if (dateNumber) { // 속성 삭제
            dateNumber.style.removeProperty("color");
            dateNumber.style.removeProperty("font-weight");
            dateNumber.style.removeProperty("text-shadow");
          }

          // 클래스 삭제
          cell.classList.remove("has-record");
        }
      });
    };
  }, [queryString]);

  const handleDateChange = (arg: DatesSetArg) => {
    setSelectedYear(arg.view.currentStart.getFullYear());
    setSelectedMonth(arg.view.currentStart.getMonth() + 1);
  };

  const handleDayCellContent = (arg: { dayNumberText: string }) => {
    return (<span className="ml-auto">{arg.dayNumberText.replace("일", "")}</span>
    );
  };

  const handleFollowButtonClick = (ownerId: string) => {
    router.push(`/follow?userId=${ownerId}`);
  };

  const handleDateClick = (arg: { dateStr: string }) => {
    const clickedDate: CalendarDate | undefined = monthly?.find(
        (calendarDate) => calendarDate.date === arg.dateStr
    );

    if (!clickedDate && isCalendarOwner) {
      const [yearStr, monthStr, dayStr] = arg.dateStr.split("-");

      const year = parseInt(yearStr, 10);
      const month = parseInt(monthStr, 10);
      const day = parseInt(dayStr, 10);

      router.push(`/calendar/record?year=${year}&month=${month}&day=${day}`);
    } else if (clickedDate && isCalendarOwner) {
      router.push(`/calendar/${clickedDate.id}`);
    } else if (clickedDate && !isCalendarOwner) {
      router.push(`/calendar/${clickedDate.id}?readOnly=true`);
    }
  };

  return (
    <div className="flex flex-col w-full px-10 justify-center items-center">
      <div className="flex justify-end mt-4 mb-4" style={{width: "min(90vh, calc(100vw - 18rem))"}}>
        <h2 className="text-xl text-[#393D3F]">
          {calendarOwner?.name ?? "나"}의 캘린더📆
        </h2>
        <div className="flex space-x-4 ml-4">
          <button
            className="text-lg text-[#393D3F] bg-[#C8B6FF] rounded-lg px-2"
            onClick={() => handleFollowButtonClick(calendarOwner!.id)}
          >
            {followerCount} 팔로워
          </button>
          <button
            className="text-lg text-[#393D3F] bg-[#C8B6FF] rounded-lg px-2"
            onClick={() => handleFollowButtonClick(calendarOwner!.id)}
          >
            {followingCount} 팔로잉
          </button>
        </div>
      </div>
      <div
        style={{
          width: "min(90vh, calc(100vw - 18rem))",
          height: "min(90vh, calc(100vw - 18rem))",
        }}
      >
        {isCalendarOwner !== null && (
          <FullCalendar
            locale="ko"
            height="100%"
            contentHeight="100%"
            plugins={[dayGridPlugin, interactionPlugin]}
            headerToolbar={{
              left: "prevYear,prev",
              center: "title",
              right: "next,nextYear",
            }}
            initialView="dayGridMonth"
            editable={false}
            selectable={false}
            selectMirror={true}
            dayCellContent={handleDayCellContent}
            datesSet={handleDateChange}
            dateClick={handleDateClick}
            dayMaxEvents={true}
            events={events}
            eventDidMount={handleEventDidMount}
            dayCellDidMount={(arg) => handleDayCellDidMount(arg, isCalendarOwner)}
            stickyHeaderDates={true}
            validRange={{
              end: today,
            }}
            showNonCurrentDates={false}
          />
        )}
      </div>
    </div>
  );
};

export default Calendar;