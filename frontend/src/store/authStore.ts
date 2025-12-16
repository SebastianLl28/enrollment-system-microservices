import type { UiView } from "@/features/auth/services/authService";
import { create } from "zustand";

type AuthState = {
  user: { userId: number; username: string } | null;
  permissions: string[];
  uiViews: UiView[];
  setAuth: (payload: {
    userId: number;
    username: string;
    permissions: string[];
    uiViews: UiView[];
  }) => void;
  clearAuth: () => void;
};

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  permissions: [],
  uiViews: [],
  setAuth: ({ userId, username, permissions, uiViews }) =>
    set({ user: { userId, username }, permissions, uiViews }),
  clearAuth: () => set({ user: null, permissions: [], uiViews: [] }),
}));
