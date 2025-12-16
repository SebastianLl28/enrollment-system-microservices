import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { ROUTE_PATHS } from "@/app/route/path";
import { useAuth } from "@/features/auth/hooks/useAuth";
import { Link, useNavigate } from "react-router-dom";
import {
  GraduationCap,
  Building,
  BookOpen,
  Users,
  Clock,
  ListTodo,
  BookUp,
  Shield,
} from "lucide-react";
import { useAuthStore } from "@/store/authStore";
import { useMemo } from "react";

const allModules = [
  {
    title: "Facultad",
    description: "Lista y gestión de facultades",
    path: ROUTE_PATHS.faculty,
    icon: Building,
    requiredViewCode: "FACULTY_LIST",
    requiredPermission: null,
  },
  {
    title: "Carreras",
    description: "Gestiona carreras y sus facultades",
    path: ROUTE_PATHS.career,
    icon: GraduationCap,
    requiredViewCode: "CAREER_LIST",
    requiredPermission: null,
  },
  {
    title: "Cursos",
    description: "Gestiona cursos y asigna carreras",
    path: ROUTE_PATHS.course,
    icon: BookOpen,
    requiredViewCode: "COURSE_LIST",
    requiredPermission: null,
  },
  {
    title: "Estudiantes",
    description: "Gestiona estudiantes y sus datos",
    path: ROUTE_PATHS.students,
    icon: Users,
    requiredViewCode: "STUDENT_LIST",
    requiredPermission: "STUDENT:READ",
  },
  {
    title: "Perfil",
    description: "Configura tu perfil y ajustes personales",
    path: ROUTE_PATHS.profile,
    icon: Users,
    requiredViewCode: "MY_PROFILE",
    requiredPermission: null,
  },
  {
    title: "Vigencias",
    description: "Gestiona las vigencias académicas",
    path: ROUTE_PATHS.term,
    icon: Clock,
    requiredViewCode: "TERM_LIST",
    requiredPermission: null,
  },
  {
    title: "Cursos en Vigencia",
    description: "Gestiona los cursos en vigencia",
    path: ROUTE_PATHS.courseOffering,
    icon: ListTodo,
    requiredViewCode: "COURSE_OFFERING_LIST",
    requiredPermission: null,
  },
  {
    title: "Inscripciones",
    description: "Gestiona las inscripciones de los estudiantes",
    path: ROUTE_PATHS.enrollment,
    icon: BookUp,
    requiredViewCode: "ENROLLMENT_ADMIN",
    requiredPermission: "ENROLLMENT:READ",
  },
  {
    title: "Gestión de Permisos",
    description: "Administra permisos del sistema",
    path: ROUTE_PATHS.rbacPermissions,
    icon: Shield,
    requiredViewCode: "RBAC_PERMISSIONS",
    requiredPermission: "UI_VIEW:UPDATE",
  },
  {
    title: "Gestión de Roles",
    description: "Administra roles y permisos",
    path: ROUTE_PATHS.rbacRoles,
    icon: Shield,
    requiredViewCode: "RBAC_ROLES",
    requiredPermission: "UI_VIEW:UPDATE",
  },
  {
    title: "Asignar Roles",
    description: "Asigna roles a usuarios",
    path: ROUTE_PATHS.rbacUsers,
    icon: Users,
    requiredViewCode: "RBAC_USERS",
    requiredPermission: "UI_VIEW:UPDATE",
  },
];

const DashboardPage = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const permissions = useAuthStore((state) => state.permissions);
  const uiViews = useAuthStore((state) => state.uiViews);

  const handleLogout = () => {
    logout();
    navigate(ROUTE_PATHS.login, { replace: true });
  };

  const visibleModules = useMemo(() => {
    return allModules.filter((module) => {
      const hasView = uiViews?.some(
        (view) => view.code === module.requiredViewCode && view.active
      );

      if (!hasView) {
        return false;
      }

      if (!module.requiredPermission) {
        return true;
      }

      const hasPermission = permissions?.some((perm) =>
        perm.startsWith(module.requiredPermission)
      );

      return hasPermission;
    });
  }, [uiViews, permissions]);

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-50 p-6">
      <div className="w-full max-w-4xl space-y-6 rounded-lg bg-white p-6 shadow md:max-w-5xl">
        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm text-gray-500">Zona protegida</p>
            <h1 className="text-2xl font-semibold text-gray-800">
              Bienvenido {user ? user : "usuario"}
            </h1>
          </div>
          <Button variant="outline" onClick={handleLogout}>
            Cerrar sesión
          </Button>
        </div>

        {/* Solo para debug - eliminar en producción */}
        <details className="rounded border bg-gray-50 p-3 text-xs">
          <summary className="cursor-pointer font-semibold text-gray-700">
            Debug: Permisos y Vistas (click para expandir)
          </summary>
          <div className="mt-2 space-y-2">
            <div>
              <p className="font-semibold">
                Permisos ({permissions?.length || 0}):
              </p>
              <pre className="overflow-auto rounded bg-white p-2 max-h-40">
                {JSON.stringify(permissions, null, 2)}
              </pre>
            </div>
            <div>
              <p className="font-semibold">
                Vistas UI ({uiViews?.length || 0}):
              </p>
              <pre className="overflow-auto rounded bg-white p-2 max-h-40">
                {JSON.stringify(uiViews, null, 2)}
              </pre>
            </div>
            <div className="mt-3 rounded bg-yellow-50 p-2 border border-yellow-200">
              <p className="font-semibold text-yellow-800">
                ℹ️ Lógica de filtrado:
              </p>
              <ul className="text-xs text-yellow-700 mt-1 space-y-1 list-disc list-inside">
                <li>
                  ✅ Módulos con vista + permiso válido (STUDENT, ENROLLMENT)
                </li>
                <li>✅ Módulos solo con vista (FACULTY, CAREER, etc.)</li>
                <li>❌ Módulos sin vista asignada</li>
              </ul>
            </div>
          </div>
        </details>

        <p className="text-gray-600">
          Esta vista está protegida. Usa los accesos rápidos para navegar a los
          módulos disponibles según tus permisos.
        </p>

        {visibleModules.length === 0 ? (
          <div className="rounded-lg border-2 border-dashed border-gray-300 p-8 text-center">
            <Users className="mx-auto h-12 w-12 text-gray-400" />
            <h3 className="mt-4 text-lg font-semibold text-gray-700">
              No tienes módulos disponibles
            </h3>
            <p className="mt-2 text-sm text-gray-500">
              Contacta al administrador para que te asigne los permisos
              necesarios.
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 md:grid-cols-3">
            {visibleModules.map((module) => (
              <Link key={module.path} to={module.path} className="group">
                <Card className="h-full justify-between transition hover:-translate-y-1 hover:shadow-lg">
                  <CardHeader className="flex flex-row items-center gap-3">
                    <div className="rounded-md bg-blue-50 p-2 text-blue-600">
                      <module.icon className="h-5 w-5" />
                    </div>
                    <div>
                      <CardTitle className="text-lg">{module.title}</CardTitle>
                      <CardDescription>{module.description}</CardDescription>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <p className="text-sm text-gray-500 group-hover:text-gray-700">
                      Ir a {module.title}
                    </p>
                  </CardContent>
                </Card>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default DashboardPage;
