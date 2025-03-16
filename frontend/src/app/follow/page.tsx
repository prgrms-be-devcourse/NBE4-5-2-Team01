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
            Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1Z2x6bXRtMjFtem5yd2IxMDdzb2s0cWkwIiwic3BvdGlmeVRva2VuIjoiQlFENl9rWmNVTER5Z2VLQ2JjUjNpTmpCa2RCLUwyek1kTTlYMGlVNjg1anRvTDJQSHlJSGRjSFVnbWpwNURyM0tuVGhrekpyLUdONlVZT05tNkE4ZEF4d2JELXdGNndJMFFVd1Q5SEZBVHlJdlc2ck5aNUJDVzdUSUNQSmlVdUw0dWQ1WnJrZ1BkQ3JLS1BoajBlbjlGYTRCZERpWkJlaWRvOW1kazNuWDBLWjUxbHlkdmYzT1g0M2ZsXzFOUjhmN253Wk5vTzZrTVZYeWNZcG1UZDNCd0xRRERKa2ZGNXR6QVJ0U0kxRHdFRV9qMWFuOFEiLCJpYXQiOjE3NDIwOTY5MjIsImV4cCI6MTc0MjEwMDUyMn0.pGUyMGb0FMOXv-M_2Nwg0qMmkbIN0J8u_IL1c6eIjbM`,
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
              <button className="follow-button">{user.followState ? "팔로잉" : "팔로우"}</button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default FollowPage;