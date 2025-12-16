import { apiClient } from "@/config/apiClient";
import { LOCAL_STORAGE_TOKEN_KEY } from "@/config/constants";

const bearerInterceptor = () => {
  apiClient.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
      if (token) {
        config.headers["Authorization"] = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );
};

export default bearerInterceptor;
