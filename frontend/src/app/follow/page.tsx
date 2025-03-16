"use client"

import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useRouter, useSearchParams } from "next/navigation";
import "@/components/style/follow.css";

const FollowPage = () => {
  const [activeTab, setActiveTab] = useState("following");
  const { id } = useParams<{ id: string }>(); 
  const [users, setUsers] = useState([]);
  const router = useRouter();
  const searchParams = useSearchParams(); 
  const userId = searchParams.get("userId");

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        if (!userId) return;
        const token = localStorage.getItem("accessToken");

        const response = await axios.get(`http://localhost:8080/api/v1/follows/${activeTab}/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
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
  }, [activeTab, userId]);

  const toggleFollow = async (e: React.MouseEvent, userId: string, isFollowing: boolean) => {
    e.stopPropagation();

    try {
      const token = localStorage.getItem("accessToken");

      if (isFollowing) {
        await axios.delete(`http://localhost:8080/api/v1/follows/${userId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
      } else {
        await axios.post(
          `http://localhost:8080/api/v1/follows/${userId}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
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

  const handleUserClick = (userId: string) => {
    router.push(`/calendar?userId=${userId}`); // userId 쿼리 파라미터 추가
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
            <div key={index} className="user-block" onClick={() => handleUserClick(user.user.id)}>
              <span className="user-text">{user.user.name}</span>
              <button 
                className="follow-button"
                onClick={(e) => toggleFollow(e, user.user.id, user.followState)}>
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