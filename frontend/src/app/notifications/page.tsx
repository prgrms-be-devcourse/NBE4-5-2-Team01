"use client";

import { useEffect, useState } from "react";
import axios from "axios";
import styles from "@/components/style/notification.module.css"; // CSS 모듈을 사용하여 스타일 적용

interface NotificationDto {
  id: number;
  userId: number;
  message: string;
  notificationTime: string; // LocalTime을 string 형식으로 받으므로 string으로 처리
}

const Notifications = () => {
  const [notifications, setNotifications] = useState<NotificationDto[]>([]);

  // 생성 폼 상태
  const [newUserId, setNewUserId] = useState<number>(0);
  const [newMessage, setNewMessage] = useState<string>("");
  const [newTime, setNewTime] = useState<string>("");

  // 수정 폼 상태 (현재 편집중인 항목)
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editMessage, setEditMessage] = useState<string>("");
  const [editTime, setEditTime] = useState<string>("");

  // 알림 목록 불러오기
  const fetchNotifications = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/api/v1/notifications"
      );
      console.log(response.data); // 응답 확인
      // 응답이 배열이면 그대로 사용, 아니면 적절한 필드를 사용
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

  // 알림 생성 함수
  const handleCreate = async () => {
    try {
      const payload = {
        userId: newUserId,
        message: newMessage,
        notificationTime: newTime,
      };
      await axios.post(
        "http://localhost:8080/api/v1/notifications/create",
        payload
      );
      alert("알림이 생성되었습니다.");
      // 폼 초기화 후 목록 갱신
      setNewUserId(0);
      setNewMessage("");
      setNewTime("");
      fetchNotifications();
    } catch (error) {
      console.error("Failed to create notification", error);
      alert("알림 생성에 실패했습니다.");
    }
  };

  // 수정 시작: 해당 알림의 데이터를 편집 상태로 옮김
  const startEditing = (notification: NotificationDto) => {
    setEditingId(notification.id);
    setEditMessage(notification.message);
    setEditTime(notification.notificationTime);
  };

  // 수정 취소
  const cancelEditing = () => {
    setEditingId(null);
    setEditMessage("");
    setEditTime("");
  };

  // 알림 수정 함수
  const handleUpdate = async (id: number) => {
    try {
      const payload = {
        message: editMessage,
        notificationTime: editTime,
      };
      await axios.put(
        `http://localhost:8080/api/v1/notifications/${id}/modify`,
        payload
      );
      alert("알림이 수정되었습니다.");
      cancelEditing();
      fetchNotifications();
    } catch (error) {
      console.error("Failed to update notification", error);
      alert("알림 수정에 실패했습니다.");
    }
  };

  // 알림 삭제 함수
  const handleDelete = async (id: number) => {
    try {
      await axios.delete(`http://localhost:8080/api/v1/notifications/${id}`);
      alert("알림이 삭제되었습니다.");
      fetchNotifications();
    } catch (error) {
      console.error("Failed to delete notification", error);
      alert("알림 삭제에 실패했습니다.");
    }
  };

  return (
    <div className={styles.container}>
      {/* 알림 생성 폼 */}
      <h2 className={styles.heading}>알림 생성</h2>
      <div className={styles.form}>
        <div className={styles.inputGroup}>
          <label>사용자 ID</label>
          <input
            className={styles.input}
            type="number"
            value={newUserId || ""}
            onChange={(e) => setNewUserId(Number(e.target.value))}
          />
        </div>
        <div className={styles.inputGroup}>
          <label>메시지</label>
          <input
            className={styles.input}
            type="text"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
          />
        </div>
        <div className={styles.inputGroup}>
          <label>알림 시간 (HH:mm)</label>
          <input
            className={styles.input}
            type="time"
            value={newTime}
            onChange={(e) => setNewTime(e.target.value)}
          />
        </div>
        <button className={styles.button} onClick={handleCreate}>
          알림 생성
        </button>
      </div>

      {/* 알림 목록 */}
      <h2 className={styles.heading}>알림 목록</h2>
      {notifications.length > 0 ? (
        notifications.map((notification) => (
          <div key={notification.id} className={styles.notificationCard}>
            {editingId === notification.id ? (
              // 수정 중인 경우, 편집 폼 보여주기
              <div className={styles.editForm}>
                <div className={styles.inputGroup}>
                  <label>메시지</label>
                  <input
                    className={styles.input}
                    type="text"
                    value={editMessage}
                    onChange={(e) => setEditMessage(e.target.value)}
                  />
                </div>
                <div className={styles.inputGroup}>
                  <label>알림 시간 (HH:mm)</label>
                  <input
                    className={styles.input}
                    type="time"
                    value={editTime}
                    onChange={(e) => setEditTime(e.target.value)}
                  />
                </div>
                <div className={styles.editButtons}>
                  <button
                    className={styles.button}
                    onClick={() => handleUpdate(notification.id)}
                  >
                    저장
                  </button>
                  <button className={styles.button} onClick={cancelEditing}>
                    취소
                  </button>
                </div>
              </div>
            ) : (
              // 기본 상태: 알림 정보와 수정/삭제 버튼
              <div className={styles.notificationDetails}>
                <h4>{notification.message}</h4>
                <p>{notification.notificationTime}</p>
                <div className={styles.buttons}>
                  <button
                    className={styles.button}
                    onClick={() => startEditing(notification)}
                  >
                    수정
                  </button>
                  <button
                    className={styles.button}
                    onClick={() => handleDelete(notification.id)}
                  >
                    삭제
                  </button>
                </div>
              </div>
            )}
          </div>
        ))
      ) : (
        <p>알림이 없습니다.</p>
      )}
    </div>
  );
};

export default Notifications;
