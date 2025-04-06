export interface User {
    id: string;
    name: string;
    nickName: string;
    profileImg: string;
    calendarVisibility: "PUBLIC" | "FOLLOWER_ONLY" | "PRIVATE";
}