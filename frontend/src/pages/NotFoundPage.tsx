import { Link } from "react-router-dom";
import { ROUTE_PATHS } from "@/app/route/path";

const NotFoundPage = () => {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gray-50 p-6 text-center">
      <h1 className="text-3xl font-semibold text-gray-800">404</h1>
      <p className="mt-2 text-gray-600">
        La página que buscas no está disponible.
      </p>
      <Link
        to={ROUTE_PATHS.root}
        className="mt-4 text-sm font-medium text-blue-600 underline"
      >
        Volver al inicio
      </Link>
    </div>
  );
};

export default NotFoundPage;
