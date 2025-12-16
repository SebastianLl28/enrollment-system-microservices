import { useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";
import RoleDialog from "../components/RoleDialog";
import type { RoleResponse } from "../types/response";
import type { CreateRoleCommand } from "../types/request";
import { useGetRoles, useGetPermissions, useGetViews } from "../hooks/useQuery";
import {
  useCreateRole,
  useUpdateRole,
  useDeleteRole,
} from "../hooks/useMutation";
import { useRbacRoleColumns } from "@/config/columns";

const RolesPage = () => {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingRole, setEditingRole] = useState<RoleResponse | null>(null);

  const {
    data: roles,
    isPending,
    isRefetching,
    isError,
    error,
    refetch,
  } = useGetRoles();
  const { data: permissions = [] } = useGetPermissions();
  const { data: views = [] } = useGetViews();

  const { mutate: createRole } = useCreateRole();
  const { mutate: updateRole } = useUpdateRole();
  const { mutate: deleteRole } = useDeleteRole();

  const handleCreate = () => {
    setEditingRole(null);
    setDialogOpen(true);
  };

  const handleEdit = (role: RoleResponse) => {
    setEditingRole(role);
    setDialogOpen(true);
  };

  const handleDelete = (id: number) => {
    if (confirm("¿Estás seguro de eliminar este rol?")) {
      deleteRole(id);
    }
  };

  const onSubmit = (values: CreateRoleCommand) => {
    if (editingRole) {
      updateRole({
        id: editingRole.id,
        data: { ...values, id: editingRole.id },
      });
    } else {
      createRole(values);
    }
    setDialogOpen(false);
  };

  const columns = useRbacRoleColumns(handleEdit, handleDelete);

  const rolesList = useMemo(() => roles ?? [], [roles]);

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-7xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Administración RBAC" },
            { label: "Roles" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">
              Administración RBAC
            </p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Gestión de Roles
            </h1>
            <p className="text-sm text-gray-600">
              Crea y configura roles asignando permisos y vistas del sistema.
            </p>
          </div>
          <Button onClick={handleCreate}>Crear rol</Button>
        </div>

        <Card>
          <DataTable
            data={rolesList}
            columns={columns}
            isLoading={isPending || isRefetching}
            error={isError ? (error as Error) : null}
            onRetry={refetch}
            emptyMessage="No hay roles registrados."
          />
        </Card>

        <RoleDialog
          open={dialogOpen}
          onOpenChange={setDialogOpen}
          editingRole={editingRole}
          onSubmit={onSubmit}
          permissions={permissions}
          views={views}
        />
      </div>
    </div>
  );
};

export default RolesPage;
