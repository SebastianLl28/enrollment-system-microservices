import { useMemo, useState } from "react";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";
import UserRoleDialog from "../components/UserRoleDialog";
import type { UserRbacResponse } from "../types/response";
import { useGetUsers, useGetRoles } from "../hooks/useQuery";
import { toast } from "sonner";
import { useRbacUserColumns } from "@/config/columns";
import { useAssignRoles } from "../hooks/useMutation";

const UsersRbacPage = () => {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState<UserRbacResponse | null>(
    null
  );

  const {
    data: users,
    isPending,
    isRefetching,
    isError,
    error,
    refetch,
  } = useGetUsers();
  const { data: roles = [] } = useGetRoles();

  const handleAssignRoles = (user: UserRbacResponse) => {
    setSelectedUser(user);
    setDialogOpen(true);
  };

  const { mutate: assignRoles } = useAssignRoles();

  const handleRoleAssignment = (userId: number, roleIds: number[]) => {
    assignRoles({ userId, roleIds });
    toast.success("Roles asignados exitosamente");
    refetch();
  };

  const columns = useRbacUserColumns(handleAssignRoles);

  const usersList = useMemo(() => users ?? [], [users]);

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-7xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Administración RBAC" },
            { label: "Usuarios" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">
              Administración RBAC
            </p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Gestión de Usuarios
            </h1>
            <p className="text-sm text-gray-600">
              Asigna y gestiona roles de los usuarios del sistema.
            </p>
          </div>
        </div>

        <Card>
          <DataTable
            data={usersList}
            columns={columns}
            isLoading={isPending || isRefetching}
            error={isError ? (error as Error) : null}
            onRetry={refetch}
            emptyMessage="No hay usuarios registrados."
          />
        </Card>

        <UserRoleDialog
          open={dialogOpen}
          onOpenChange={setDialogOpen}
          user={selectedUser}
          roles={roles}
          onAssign={handleRoleAssignment}
        />
      </div>
    </div>
  );
};

export default UsersRbacPage;
