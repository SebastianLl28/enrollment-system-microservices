import { useQuery } from "@tanstack/react-query";
import { DASHBOARD_STATS_QUERY } from "@/config/keys";
import { getDashboardStats } from "../services/DashboardService";

export const useGetDashboardStats = (options?: { enabled?: boolean }) => {
  return useQuery({
    queryKey: DASHBOARD_STATS_QUERY,
    queryFn: getDashboardStats,
    enabled: options?.enabled ?? true,
  });
};
