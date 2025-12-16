import { apiClient } from "@/config/apiClient";
import { AUTH_ENDPOINT } from "@/config/endpoints";

export interface LoginResponse {
  token: string;
  username: string;
  twoFactorRequired: boolean;
}

interface LoginRequest {
  username: string;
  password: string;
}

interface ValidateTokenResponse {
  username: string;
  valid: boolean;
  userId: number;
  permissions: string[];
  uiViews: UiView[];
}

export interface UiView {
  code: string;
  route: string;
  label: string;
  module: string;
  sortOrder: number;
  active: boolean;
}

export interface RegisterCommand {
  username: string;
  password: string;
  email: string;
  fullName: string;
}

export interface RegisterResponse {
  username: string;
  message: string;
}

export const postLogin = async (data: LoginRequest) => {
  return await apiClient
    .post<LoginResponse>(AUTH_ENDPOINT.login, data)
    .then((res) => res.data);
};

export const getValidateToken = async () => {
  return await apiClient
    .get<ValidateTokenResponse>(AUTH_ENDPOINT.validateToken)
    .then((res) => res.data);
};

export const registerUser = async (command: RegisterCommand) => {
  return await apiClient
    .post<RegisterResponse>(AUTH_ENDPOINT.register, command)
    .then((res) => res.data);
};

export interface VerifyTwoFactorCommand {
  tempToken: string;
  code: string;
}

export interface VerifyTwoFactorResponse {
  accessToken: string;
  username: string;
}

export const verifyTwoFactorCode = async (command: VerifyTwoFactorCommand) => {
  return await apiClient
    .post<VerifyTwoFactorResponse>(AUTH_ENDPOINT.twoFactorVerify, command, {
      headers: {
        Authorization: `Bearer ${command.tempToken}`,
      },
    })
    .then((res) => res.data);
};
