import { DASHBOARD_ENDPOINT } from "@/config/endpoints";
import { apiClient } from "@/config/apiClient";
import type { DashboardStatsResponse } from "../types/response";

export const getDashboardStats = async () => {
  return await apiClient
    .get<DashboardStatsResponse>(DASHBOARD_ENDPOINT.stats)
    .then((res) => res.data);
};
