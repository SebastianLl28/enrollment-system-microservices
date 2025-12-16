import { Navigate } from "react-router-dom";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { ROUTE_PATHS } from "@/app/route/path";
import type { ReactNode } from "react";
import { useRouteAccess } from "../hooks/useRouteAccess";

interface RBACRouteProps {
  children: ReactNode;
  path: string;
  fallbackPath?: string;
}

export const RBACRoute = ({
  children,
  path,
  fallbackPath = ROUTE_PATHS.dashboard,
}: RBACRouteProps) => {
  const { isAuthenticated, isChecking } = useAuth();
  const { hasAccess } = useRouteAccess(path);

  if (isChecking) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div className="text-center">
          <div className="mb-4 h-8 w-8 animate-spin rounded-full border-4 border-blue-500 border-t-transparent mx-auto"></div>
          <p className="text-sm text-gray-600">Cargando...</p>
        </div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to={ROUTE_PATHS.login} replace />;
  }

  if (!hasAccess) {
    return <Navigate to={fallbackPath} replace />;
  }

  return <>{children}</>;
};
