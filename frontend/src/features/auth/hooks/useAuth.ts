import { LOCAL_STORAGE_TOKEN_KEY } from "@/config/constants";
import { AUTH_VALIDATE_QUERY } from "@/config/keys";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { getValidateToken } from "../services/authService";
import { useEffect } from "react";
import { useAuthStore } from "@/store/authStore";

export const useAuth = () => {
  const queryClient = useQueryClient();
  const token = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);

  const setAuth = useAuthStore((s) => s.setAuth);
  const clearAuth = useAuthStore((s) => s.clearAuth);

  const query = useQuery({
    queryKey: AUTH_VALIDATE_QUERY,
    queryFn: getValidateToken,
    enabled: Boolean(token),
    staleTime: 5 * 60 * 1000,
    retry: false,
  });

  useEffect(() => {
    if (!token) {
      clearAuth();
      return;
    }

    if (query.data?.valid) {
      setAuth({
        userId: query.data.userId,
        username: query.data.username,
        permissions: query.data.permissions,
        uiViews: query.data.uiViews,
      });
    }

    // si el backend dice inválido, limpias
    if (query.data && !query.data.valid) {
      clearAuth();
    }
  }, [token, query.data, setAuth, clearAuth]);

  const logout = () => {
    localStorage.removeItem(LOCAL_STORAGE_TOKEN_KEY);
    queryClient.removeQueries({ queryKey: AUTH_VALIDATE_QUERY });
    clearAuth();
  };

  return {
    ...query,
    logout,
    token,
    user: query.data?.username ?? null,
    isAuthenticated: Boolean(token && query.data?.valid),
    isChecking:
      Boolean(token) &&
      (query.isPending || query.isFetching || query.isRefetching),
  };
};
