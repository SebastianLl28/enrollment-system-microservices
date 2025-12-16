import { apiClient } from "@/config/apiClient";
import { AUTH_ENDPOINT } from "@/config/endpoints";

interface ProfileData {
  id: string;
  username: string;
  email: string;
  fullName: string;
  twoFactorEnabled: boolean;
  hasPassword: boolean;
}

interface TwoFactorInitResponse {
  secret: string;
  otpauthUrl: string;
}

interface TwoFactorConfirmCommand {
  code: string;
}

interface TwoFactorConfirmResponse {
  twoFactorEnabled: boolean;
}

export const getProfile = async () => {
  return await apiClient
    .get<ProfileData>(AUTH_ENDPOINT.profile)
    .then((res) => res.data);
};

export const initTwoFactor = async () => {
  return await apiClient
    .post<TwoFactorInitResponse>(AUTH_ENDPOINT.twoFactorInit, {})
    .then((res) => res.data);
};

export const confirmTwoFactor = async (code: string) => {
  return await apiClient
    .post<TwoFactorConfirmResponse>(AUTH_ENDPOINT.twoFactorConfirm, {
      code,
    } as TwoFactorConfirmCommand)
    .then((res) => res.data);
};
