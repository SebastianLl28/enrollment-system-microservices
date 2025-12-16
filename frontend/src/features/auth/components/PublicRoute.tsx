// import { ROUTE_PATHS } from "@/app/route/path";
// import { useAuth } from "../hooks/useAuth";
import { Outlet } from "react-router-dom";

const PublicRoute = () => {
  // const { isAuthenticated, isChecking } = useAuth();

  // if (isChecking) {
  //   return (
  //     <div className="flex min-h-screen items-center justify-center text-sm text-gray-600">
  //       Validando sesión...
  //     </div>
  //   );
  // }

  // if (isAuthenticated) {
  //   return <Navigate to={ROUTE_PATHS.dashboard} replace />;
  // }

  return <Outlet />;
};

export default PublicRoute;
