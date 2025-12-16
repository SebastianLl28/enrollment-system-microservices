import { useMemo } from "react";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";
import { useGetPermissions } from "../hooks/useQuery";
import { useRbacPermissionColumns } from "@/config/columns";

const PermissionsPage = () => {
  const {
    data: permissions,
    isPending,
    isRefetching,
    isError,
    error,
    refetch,
  } = useGetPermissions();

  const columns = useRbacPermissionColumns();

  const permissionsList = useMemo(() => permissions ?? [], [permissions]);

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-7xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Administración RBAC" },
            { label: "Permisos" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">
              Administración RBAC
            </p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Catálogo de Permisos
            </h1>
            <p className="text-sm text-gray-600">
              Visualiza todos los permisos disponibles en el sistema.
            </p>
          </div>
        </div>

        <Card>
          <DataTable
            data={permissionsList}
            columns={columns}
            isLoading={isPending || isRefetching}
            error={isError ? (error as Error) : null}
            onRetry={refetch}
            emptyMessage="No hay permisos registrados."
          />
        </Card>
      </div>
    </div>
  );
};

export default PermissionsPage;
