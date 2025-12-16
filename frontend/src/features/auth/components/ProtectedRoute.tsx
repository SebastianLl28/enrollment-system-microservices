import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { ROUTE_PATHS } from "@/app/route/path";
import { useRouteAccess } from "../hooks/useRouteAccess";
// import { useRouteAccess } from "@/hooks/useRouteAccess";

const ProtectedRoute = () => {
  const { isAuthenticated, isChecking } = useAuth();
  const location = useLocation();
  const { hasAccess } = useRouteAccess(location.pathname);

  // Estado 1: Verificando autenticación
  if (isChecking) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div className="text-center">
          <div className="mb-4 h-8 w-8 animate-spin rounded-full border-4 border-blue-500 border-t-transparent mx-auto"></div>
          <p className="text-sm text-gray-600">Validando sesión...</p>
        </div>
      </div>
    );
  }

  // Estado 2: No autenticado
  if (!isAuthenticated) {
    return (
      <Navigate to={ROUTE_PATHS.login} state={{ from: location }} replace />
    );
  }

  // Estado 3: Autenticado pero sin acceso a esta ruta
  if (!hasAccess && location.pathname !== ROUTE_PATHS.dashboard) {
    return (
      <Navigate
        to={ROUTE_PATHS.dashboard}
        state={{
          from: location,
          message: "No tienes permisos para acceder a esta sección",
        }}
        replace
      />
    );
  }

  // Estado 4: Acceso concedido
  return <Outlet />;
};

export default ProtectedRoute;
