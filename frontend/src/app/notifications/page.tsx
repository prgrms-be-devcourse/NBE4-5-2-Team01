"use client";

import { useEffect, useState } from "react";
import axios from "axios";
import Link from "next/link"; // Next.js Link 컴포넌트를 사용하여 페이지 이동
import styles from "@/components/style/notification.module.css";
import Image from "next/image";

interface NotificationDto {
  id: number;
  userId: number;
  message: string;
  notificationTime: string;
  isRead: boolean;
}

const Notifications = () => {
  const [notifications, setNotifications] = useState<NotificationDto[]>([]);

  // 알림 목록 불러오기
  const fetchNotifications = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/api/v1/notifications"
      );
      if (Array.isArray(response.data)) {
        setNotifications(response.data);
      } else if (response.data.notifications) {
        setNotifications(response.data.notifications);
      }
    } catch (error) {
      console.error("Failed to fetch notifications", error);
    }
  };

  useEffect(() => {
    fetchNotifications();
  }, []);

  // 알림 읽음 처리
  const markAsRead = async (id: number) => {
    try {
      await axios.put(`http://localhost:8080/api/v1/notifications/${id}/read`);
      setNotifications((prev) =>
        prev.map((notification) =>
          notification.id === id
            ? { ...notification, isRead: true }
            : notification
        )
      );
      alert("알림이 읽음 처리되었습니다.");
    } catch (error) {
      console.error("Failed to mark notification as read", error);
      alert("알림 읽음 처리에 실패했습니다.");
    }
  };

  return (
    <div className="max-w-2xl mx-auto p-5 bg-gray-100 rounded-lg">
      <h2 className="text-2xl text-center font-semibold mb-5 text-gray-800 flex-grow">
        알림 목록
      </h2>
      <div className="flex justify-end mb-5 -mt-13">
        <Link href="/notifications/settings">
          <button className="px-4 py-2 text-sm bg-blue-500 text-white rounded-md hover:bg-blue-600">
            <Image
              src="/setting.svg"
              alt="설정 아이콘"
              width={24}
              height={24}
            />
            Setting
          </button>
        </Link>
      </div>

      {notifications.length > 0 ? (
        notifications
          .filter((notification) => !notification.isRead)
          .map((notification) => (
            <div
              key={notification.id}
              className="p-4 mb-5 bg-white rounded-lg shadow-md"
            >
              <div className="flex justify-between items-center">
                <div>
                  <h4 className="text-lg font-medium text-gray-800">
                    {notification.message}
                  </h4>
                  <p className="text-sm text-gray-600">
                    {notification.notificationTime}
                  </p>
                </div>
                <div className="flex gap-2">
                  <button
                    className="px-4 py-2 text-sm bg-green-500 text-white rounded-md hover:bg-green-600"
                    onClick={() => markAsRead(notification.id)}
                  >
                    읽음 처리
                  </button>
                </div>
              </div>
            </div>
          ))
      ) : (
        <p>알림이 없습니다.</p>
      )}
    </div>
  );
};

export default Notifications;
