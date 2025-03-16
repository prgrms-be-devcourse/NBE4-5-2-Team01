"use client"

import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams } from "next/navigation";
import "@/components/style/follow.css";

const FollowPage = () => {
  const [activeTab, setActiveTab] = useState("following");
  const { id } = useParams<{ id: string }>(); 
  const [users, setUsers] = useState([]);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/v1/follows/${activeTab}/jsaidfjsailfiesaf`,
        {
          headers: {
            Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1Z2x6bXRtMjFtem5yd2IxMDdzb2s0cWkwIiwic3BvdGlmeVRva2VuIjoiQlFDbld0Qm5ISTVZYTZRRHFqSjNRdUJ5SDB6dGpOZDlzNWU5TlRPYW10bzJrSjY0cWluN1VJN3lvRXRFRnUtTzJvdm5PN29UcmpXOVBQaFk3cEM4bUFBaXc0UmJTRjlfMFU5NVNnVlNvaS1oYWs2UHJjVVF2YWkwVmNpU3dyZDFUOHB3SU00dzV0cUY3dFpQRi03SllHc1Y1Y2c1c0QwWWtQeTNIZnc1clQ0Qk1sUjcyU2thRjdva0JOX2h4UDB3ejRta2VHT1Zqd2hyMjZraV9WTnEzcFFnZ3loUVp0TjY5aUYyejBUVG9ZUXQyRHFvYUEiLCJpYXQiOjE3NDIxMjE0NzMsImV4cCI6MTc0MjEyNTA3M30.s7ULbcz0RRockDit1NTkNyYwjh_861VdSQCgc_WhMrY`,
            "Content-Type": "application/json",
          },
        }
        );
        setUsers(response.data);
        console.log(users);
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    };

    fetchUsers();
  }, [activeTab, id]);

  const toggleFollow = async (userId: string, isFollowing: boolean) => {
    try {
      if (isFollowing) {
        await axios.delete(`http://localhost:8080/api/v1/follows/${userId}`, {
          headers: {
            Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1Z2x6bXRtMjFtem5yd2IxMDdzb2s0cWkwIiwic3BvdGlmeVRva2VuIjoiQlFDbld0Qm5ISTVZYTZRRHFqSjNRdUJ5SDB6dGpOZDlzNWU5TlRPYW10bzJrSjY0cWluN1VJN3lvRXRFRnUtTzJvdm5PN29UcmpXOVBQaFk3cEM4bUFBaXc0UmJTRjlfMFU5NVNnVlNvaS1oYWs2UHJjVVF2YWkwVmNpU3dyZDFUOHB3SU00dzV0cUY3dFpQRi03SllHc1Y1Y2c1c0QwWWtQeTNIZnc1clQ0Qk1sUjcyU2thRjdva0JOX2h4UDB3ejRta2VHT1Zqd2hyMjZraV9WTnEzcFFnZ3loUVp0TjY5aUYyejBUVG9ZUXQyRHFvYUEiLCJpYXQiOjE3NDIxMjE0NzMsImV4cCI6MTc0MjEyNTA3M30.s7ULbcz0RRockDit1NTkNyYwjh_861VdSQCgc_WhMrY`,
          },
        });
      } else {
        await axios.post(
          `http://localhost:8080/api/v1/follows/${userId}`,
          {},
          {
            headers: {
              Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1Z2x6bXRtMjFtem5yd2IxMDdzb2s0cWkwIiwic3BvdGlmeVRva2VuIjoiQlFDbld0Qm5ISTVZYTZRRHFqSjNRdUJ5SDB6dGpOZDlzNWU5TlRPYW10bzJrSjY0cWluN1VJN3lvRXRFRnUtTzJvdm5PN29UcmpXOVBQaFk3cEM4bUFBaXc0UmJTRjlfMFU5NVNnVlNvaS1oYWs2UHJjVVF2YWkwVmNpU3dyZDFUOHB3SU00dzV0cUY3dFpQRi03SllHc1Y1Y2c1c0QwWWtQeTNIZnc1clQ0Qk1sUjcyU2thRjdva0JOX2h4UDB3ejRta2VHT1Zqd2hyMjZraV9WTnEzcFFnZ3loUVp0TjY5aUYyejBUVG9ZUXQyRHFvYUEiLCJpYXQiOjE3NDIxMjE0NzMsImV4cCI6MTc0MjEyNTA3M30.s7ULbcz0RRockDit1NTkNyYwjh_861VdSQCgc_WhMrY`,
              "Content-Type": "application/json",
            },
          }
        );
      }

      setUsers((prevUsers) =>
        prevUsers.map((user) =>
          user.user.id === userId ? { ...user, followState: !isFollowing } : user
        )
      );
    } catch (error) {
      console.error("Error updating follow status:", error);
    }
  };


  return (
    <div className="flex h-screen bg-white">
      <div className="side-bar"></div>

      <div className="tab-bar">
        <div className="tab">
          <button
            className={`tab-button ${activeTab === "following" ? "active" : ""}`}
            onClick={() => setActiveTab("following")}
          >
            팔로잉
          </button>
          <button
            className={`tab-button ${activeTab === "follower" ? "active" : ""}`}
            onClick={() => setActiveTab("follower")}
          >
            팔로워
          </button>
        </div>

        <div className="list">
          {users.map((user, index) => (
            <div key={index} className="user-block">
              <span className="user-text">{user.user.name}</span>
              <button 
                className="follow-button"
                onClick={() => toggleFollow(user.user.id, user.followState)}>
                  {user.followState ? "팔로잉" : "팔로우"}
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default FollowPage;