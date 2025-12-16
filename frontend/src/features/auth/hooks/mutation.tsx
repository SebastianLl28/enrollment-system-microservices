// features/auth/hooks/mutation.ts
import { AUTH_LOGIN_MUTATION, AUTH_VALIDATE_QUERY } from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  postLogin,
  registerUser,
  verifyTwoFactorCode,
  type RegisterCommand,
  type VerifyTwoFactorCommand,
} from "../services/authService";
import {
  LOCAL_STORAGE_TOKEN_KEY,
  LOCAL_STORAGE_TEMP_TOKEN_KEY,
} from "@/config/constants";

export const useLoginMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: AUTH_LOGIN_MUTATION,
    mutationFn: postLogin,
    onSuccess: ({ token, twoFactorRequired }) => {
      if (twoFactorRequired) {
        // Guardar el token temporal para el 2FA
        localStorage.setItem(LOCAL_STORAGE_TEMP_TOKEN_KEY, token);
      } else {
        // Login normal sin 2FA
        localStorage.setItem(LOCAL_STORAGE_TOKEN_KEY, token);
        queryClient.invalidateQueries({ queryKey: AUTH_VALIDATE_QUERY });
      }
    },
  });
};

export const useVerifyTwoFactorMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: VerifyTwoFactorCommand) => verifyTwoFactorCode(data),
    onSuccess: ({ accessToken }) => {
      // Limpiar el token temporal
      localStorage.removeItem(LOCAL_STORAGE_TEMP_TOKEN_KEY);
      // Guardar el access token
      localStorage.setItem(LOCAL_STORAGE_TOKEN_KEY, accessToken);
      queryClient.invalidateQueries({ queryKey: AUTH_VALIDATE_QUERY });
    },
  });
};

export const usePostRegister = () => {
  return useMutation({
    mutationFn: (data: RegisterCommand) => registerUser(data),
  });
};
