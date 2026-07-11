import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { useHasPermission } from "@/features/auth/hooks/usePermissions";
import {
  Users,
  BookUp,
  CalendarDays,
  ListTodo,
  type LucideIcon,
} from "lucide-react";
import {
  ResponsiveContainer,
  PieChart,
  Pie,
  Legend,
  Tooltip,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
} from "recharts";
import { useGetDashboardStats } from "../hooks/useQuery";

const STATUS_META: Record<string, { label: string; color: string }> = {
  PENDING: { label: "Pendiente", color: "#eab308" },
  PAID: { label: "Pagado", color: "#22c55e" },
  COMPLETED: { label: "Completado", color: "#3b82f6" },
  CANCELLED: { label: "Cancelado", color: "#ef4444" },
};

interface KpiCardProps {
  title: string;
  value: string;
  detail: string;
  icon: LucideIcon;
}

const KpiCard = ({ title, value, detail, icon: Icon }: KpiCardProps) => (
  <Card className="gap-2 py-4">
    <CardHeader className="flex flex-row items-center justify-between px-4">
      <CardTitle className="text-sm font-medium text-gray-500">
        {title}
      </CardTitle>
      <div className="rounded-md bg-blue-50 p-2 text-blue-600">
        <Icon className="h-4 w-4" />
      </div>
    </CardHeader>
    <CardContent className="px-4">
      <p className="text-2xl font-bold text-gray-900">{value}</p>
      <p className="text-xs text-gray-500">{detail}</p>
    </CardContent>
  </Card>
);

const DashboardStats = () => {
  const canView = useHasPermission("UI_VIEW", "READ");
  const { data: stats, isLoading, isError } = useGetDashboardStats({
    enabled: canView,
  });

  if (!canView) return null;

  if (isLoading) {
    return (
      <p className="text-sm text-gray-500">Cargando estadísticas…</p>
    );
  }

  if (isError || !stats) {
    return (
      <p className="text-sm text-gray-500">
        No se pudieron cargar las estadísticas.
      </p>
    );
  }

  const statusData = stats.enrollmentsByStatus.map((item) => ({
    name: STATUS_META[item.label]?.label ?? item.label,
    value: item.count,
    fill: STATUS_META[item.label]?.color ?? "#94a3b8",
  }));

  const termData = stats.enrollmentsByTerm.map((item) => ({
    name: item.label,
    Inscripciones: item.count,
  }));

  const topCareersData = stats.topCareers.map((item) => ({
    name: item.label,
    Inscripciones: item.count,
  }));

  return (
    <div className="space-y-4">
      {/* KPIs */}
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <KpiCard
          title="Estudiantes"
          value={String(stats.activeStudents)}
          detail={`${stats.totalStudents} registrados en total`}
          icon={Users}
        />
        <KpiCard
          title="Inscripciones"
          value={String(stats.totalEnrollments)}
          detail="Acumuladas en todos los periodos"
          icon={BookUp}
        />
        <KpiCard
          title="Periodo vigente"
          value={stats.currentTerm?.code ?? "—"}
          detail={
            stats.currentTerm
              ? `${stats.currentTermEnrollments} inscripciones en este periodo`
              : "Sin periodo activo hoy"
          }
          icon={CalendarDays}
        />
        <KpiCard
          title="Carreras en vigencia"
          value={String(stats.activeCareerOfferings)}
          detail={`${stats.totalCourses} cursos en el catálogo`}
          icon={ListTodo}
        />
      </div>

      {/* Gráficos */}
      <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-base">
              Estados de las inscripciones
            </CardTitle>
          </CardHeader>
          <CardContent>
            {statusData.length === 0 ? (
              <p className="py-10 text-center text-sm text-gray-500">
                Aún no hay inscripciones registradas.
              </p>
            ) : (
              <ResponsiveContainer width="100%" height={260}>
                <PieChart>
                  <Pie
                    data={statusData}
                    dataKey="value"
                    nameKey="name"
                    innerRadius={60}
                    outerRadius={90}
                    paddingAngle={2}
                  />
                  <Tooltip />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-base">
              Inscripciones por periodo
            </CardTitle>
          </CardHeader>
          <CardContent>
            {termData.length === 0 ? (
              <p className="py-10 text-center text-sm text-gray-500">
                Aún no hay inscripciones registradas.
              </p>
            ) : (
              <ResponsiveContainer width="100%" height={260}>
                <BarChart data={termData}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} />
                  <XAxis dataKey="name" tick={{ fontSize: 12 }} />
                  <YAxis allowDecimals={false} tick={{ fontSize: 12 }} />
                  <Tooltip />
                  <Bar
                    dataKey="Inscripciones"
                    fill="#3b82f6"
                    radius={[4, 4, 0, 0]}
                  />
                </BarChart>
              </ResponsiveContainer>
            )}
          </CardContent>
        </Card>
      </div>

      {topCareersData.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle className="text-base">
              Carreras con más matrículas
            </CardTitle>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer
              width="100%"
              height={40 * topCareersData.length + 40}
            >
              <BarChart data={topCareersData} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" horizontal={false} />
                <XAxis
                  type="number"
                  allowDecimals={false}
                  tick={{ fontSize: 12 }}
                />
                <YAxis
                  type="category"
                  dataKey="name"
                  width={180}
                  tick={{ fontSize: 12 }}
                />
                <Tooltip />
                <Bar
                  dataKey="Inscripciones"
                  fill="#22c55e"
                  radius={[0, 4, 4, 0]}
                  barSize={20}
                />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      )}
    </div>
  );
};

export default DashboardStats;
