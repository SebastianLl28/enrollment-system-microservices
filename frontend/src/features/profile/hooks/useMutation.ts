// features/profile/hooks/useMutation.ts
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { initTwoFactor, confirmTwoFactor } from "../services/profileService";
import { PROFILE_QUERY } from "@/config/keys";

export const useInitTwoFactor = () => {
  return useMutation({
    mutationFn: initTwoFactor,
  });
};

export const useConfirmTwoFactor = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: confirmTwoFactor,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: PROFILE_QUERY });
    },
  });
};
