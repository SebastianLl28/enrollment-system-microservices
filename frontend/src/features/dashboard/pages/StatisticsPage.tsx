import { ROUTE_PATHS } from "@/app/route/path";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import DashboardStats from "../components/DashboardStats";

const StatisticsPage = () => {
  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-6xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Estadísticas" },
          ]}
        />

        <div>
          <p className="text-sm font-medium text-blue-600">Estadísticas</p>
          <h1 className="text-2xl font-semibold text-gray-900">
            Estadísticas del sistema
          </h1>
          <p className="text-sm text-gray-600">
            Indicadores del flujo de inscripciones, estudiantes y periodos
          </p>
        </div>

        <DashboardStats />
      </div>
    </div>
  );
};

export default StatisticsPage;
