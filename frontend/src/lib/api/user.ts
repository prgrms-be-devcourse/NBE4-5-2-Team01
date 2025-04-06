import { apiClient } from "@/lib/api/apiClient";

export async function fetchUser(url: string) {
    return await apiClient.get(url);
}