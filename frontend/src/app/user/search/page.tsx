"use client"

import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useRouter, useSearchParams } from "next/navigation";
import { getCookie } from "../../utils/cookie";
import "@/components/style/search.css";
import { useFormState } from "react-dom";

const SerachPage = () => {
  const [activeTab, setActiveTab] = useState("following");
  const [nickName, setNickName] = useState("");
  const { id } = useParams<{ id: string }>(); 
  const [users, setUsers] = useState([]);
  const router = useRouter();

  const fetchUsers = async () => {
    try {
      const token = getCookie("accessToken");
      console.log(token)

      const response = await axios.get(`http://localhost:8080/api/v1/user/search?q=${nickName}`,
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

  useEffect(() => {
    fetchUsers();
  }, [activeTab]);

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      fetchUsers();
    }
  };

  const toggleFollow = async (e: React.MouseEvent, userId: string, isFollowing: boolean) => {
    e.stopPropagation();

    try {
      const token = getCookie("accessToken");
      console.log(token);
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
          user.user.id === userId
            ? { ...user, isFollowing: !isFollowing }
            : user
        )
      );
    } catch (error) {
      console.error("Error updating follow status:", error);
    }
  };

  const handleUserClick = (userId: string) => {
    router.push(`/calendar?userId=${userId}`);
  };

  return (
    <div className="flex h-screen bg-white">
      <div className="tab-bar">
        <div className="search-bar">
          <input
            type="text"
            placeholder="검색..."
            className="search-input"
            onChange={(e) => setNickName(e.target.value)}
            onKeyDown={handleKeyDown}
          />
        </div>

        <div className="list">
          {users.map((user, index) => {
            const isFollowing = user.isFollowing;
            const isFollower = user.isFollower;
            let buttonText = "팔로우";

            if (isFollowing) {
              buttonText = "팔로잉";
            } else if (isFollower) {
              buttonText = "맞팔로우";
            }

            return (
              <div key={index} className="user-block" onClick={() => handleUserClick(user.user.id)}>
                <span className="user-text">{user.user.name}</span>
                <button 
                  className="follow-button"
                  onClick={(e) => toggleFollow(e, user.user.id, isFollowing)}
                >
                  {buttonText}
                </button>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default SerachPage;