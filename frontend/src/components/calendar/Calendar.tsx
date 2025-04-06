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
import { apiClient } from "@/lib/api/apiClient";
import { handleDayCellDidMount, handleEventDidMount } from "@/components/calendar/eventHandlers";

const Calendar: React.FC = () => {
  const [monthly, setMonthly] = useState<CalendarDate[]>([]);
  const [currentYear, setCurrentYear] = useState<number>(new Date().getFullYear());
  const [currentMonth, setCurrentMonth] = useState<number>(new Date().getMonth() + 1);
  const [user, setUser] = useState<User | null>(null);
  const [followingCount, setFollowingCount] = useState(0);
  const [followerCount, setFollowerCount] = useState(0);
  const [ownerId, setOwnerId] = useState<string | null>(null);
  const [isCalendarOwner, setIsCalendarOwner] = useState<boolean>(false);
  const [today, setToday] = useState(new Date());

  const router = useRouter();
  const params = useSearchParams();

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

  useEffect(() => {
    const fetchOwnerId = async () => {
      const userId = params.get("userId");
      let currentOwnerId: string | null = null;

      // 현재 로그인한 사용자의 정보 조회
      const response = await apiClient.get("/user/byCookie");
      const currentUser: User = response.data.data();

      // 파라미터가 없거나 현재 로그인한 유저와 동일하다면 현재 로그인한 유저가 캘린더 오너
      if (!userId || userId === currentUser.id) {
        setUser(currentUser);
        currentOwnerId = currentUser.id;
        setIsCalendarOwner(true);
      } else { // 아니라면 userId를 가진 유저와 맞팔인지 확인
        const response = await apiClient.get(`/follows/check/${userId}`)
        const isMutualFollowing: boolean = response.data.data();

        if (isMutualFollowing) { // 맞팔이라면 userId를 가진 유저가 캘린더 오너
          const response = await apiClient.get(`/user/${userId}`);
          const fetchedUser: User = response.data.data();

          setUser(fetchedUser);
          currentOwnerId = fetchedUser.id;
          setIsCalendarOwner(false);
        } else { // 아니라면 캘린더 조회 권한 없음
          alert("캘린더를 조회할 권한이 없습니다.");
          router.push("/calendar");
          return;
        }
      }

      if (currentOwnerId) {
        setOwnerId(currentOwnerId);
        fetchFollowCount(currentOwnerId);
      }
    };

    fetchOwnerId();
  }, [params]);

  useEffect(() => {
    if (ownerId) {
      fetchCalendarData(currentYear, currentMonth);
    }
  }, [ownerId, currentYear, currentMonth]);

  const handleDateChange = (arg: DatesSetArg) => {
    setCurrentYear(arg.view.currentStart.getFullYear());
    setCurrentMonth(arg.view.currentStart.getMonth() + 1);
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

  const fetchFollowCount = async (userId: string | undefined) => {
    const response = await apiClient.get(`/follows/count/${userId}`);
    const data: FollowCount = response.data.data();

    setFollowerCount(data.followerCount);
    setFollowingCount(data.followingCount);
  };

  const fetchCalendarData = async (year: number, month: number) => {
    const headers: Record<string, string> = {
      ...(isCalendarOwner ? {} : { "Calendar-Owner-Id": ownerId! }),
    };

    const response = await apiClient.get(`/calendar?year=${year}&month=${month}`, { headers });
    const data: Monthly = response.data.data;

    setMonthly(data.monthly);
  };

  return (
      <div className="flex flex-col w-full px-10 justify-center items-center">
        <div className="flex justify-end mt-4 mb-4" style={{width: "min(90vh, calc(100vw - 18rem))"}}>
          <h2 className="text-xl text-[#393D3F]">
            {user?.name ?? "나"}의 캘린더📆
          </h2>
          <div className="flex space-x-4 ml-4">
            <button
                className="text-lg text-[#393D3F] bg-[#C8B6FF] rounded-lg px-2"
                onClick={() => handleFollowButtonClick(ownerId!)}
            >
              {followerCount} 팔로워
            </button>
            <button
                className="text-lg text-[#393D3F] bg-[#C8B6FF] rounded-lg px-2"
                onClick={() => handleFollowButtonClick(ownerId!)}
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
              dayCellDidMount={(arg) => handleDayCellDidMount(arg, monthly)}
              stickyHeaderDates={true}
              validRange={{
                end: today,
              }}
          />
        </div>
      </div>
  );
};

export default Calendar;
