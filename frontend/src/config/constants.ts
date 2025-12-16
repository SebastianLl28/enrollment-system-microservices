import { AUTH_BASE_URL } from "./env";

export const LOCAL_STORAGE_TOKEN_KEY = "token";
export const LOCAL_STORAGE_TEMP_TOKEN_KEY = "tempToken";

export const OAUTH2_GITHUB_REDIRECT_URI = `${AUTH_BASE_URL}/oauth2/authorization/github`;
export const OAUTH2_GOOGLE_REDIRECT_URI = `${AUTH_BASE_URL}/oauth2/authorization/google`;
