// OAuth2RedirectHandler.tsx
import { ROUTE_PATHS } from "@/app/route/path";
import { LOCAL_STORAGE_TOKEN_KEY } from "@/config/constants";
import { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export const OAuth2RedirectHandler = () => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const token = params.get("token");

    if (!token) {
      navigate(ROUTE_PATHS.login, { replace: true });
      return;
    }

    localStorage.setItem(LOCAL_STORAGE_TOKEN_KEY, token);

    navigate(ROUTE_PATHS.dashboard, { replace: true });
  }, [location.search, navigate]);

  return <div>Redirigiendo...</div>;
};
