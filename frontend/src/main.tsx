import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import "./index.css";
import QueryProvider from "./app/provider/react-query";
import bearerInterceptor from "./app/interceptors/bearerInterceptor";
import { router } from "./app/route/routes";
import ToastProvider from "./app/provider/ToastProvider";

bearerInterceptor();

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryProvider>
      <ToastProvider>
        <RouterProvider router={router} />
      </ToastProvider>
    </QueryProvider>
  </StrictMode>
);
