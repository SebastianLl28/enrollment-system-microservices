import { ROUTE_PATHS } from "@/app/route/path";
import { useHasPermission } from "@/features/auth/hooks/usePermissions";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { DataTable } from "@/components/common/table/DataTable";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { useCallback, useState } from "react";
import ClassroomDialog from "../components/ClassroomDialog";
import type { ClassroomFormValues } from "../components/ClassroomForm";
import type { ClassroomResponse } from "../types/response";
import { useGetAllClassrooms } from "../hooks/useQuery";
import { useClassroomColumns } from "@/config/columns";
import { usePostClassroom, usePutClassroom } from "../hooks/useMutation";

const ClassroomPage = () => {
  const canCreate = useHasPermission("UI_VIEW", "CREATE");
  const canEdit = useHasPermission("UI_VIEW", "UPDATE");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingClassroom, setEditingClassroom] =
    useState<ClassroomResponse | null>(null);
  const {
    data: classrooms,
    isRefetching,
    refetch,
    isPending,
  } = useGetAllClassrooms();
  const { mutate: createClassroom, isPending: isCreating } = usePostClassroom();
  const { mutate: updateClassroom, isPending: isUpdating } = usePutClassroom();

  const handleCreate = useCallback(() => {
    setEditingClassroom(null);
    setDialogOpen(true);
  }, []);

  const handleEdit = useCallback((classroom: ClassroomResponse) => {
    setEditingClassroom(classroom);
    setDialogOpen(true);
  }, []);

  const closeDialog = useCallback(() => {
    setDialogOpen(false);
    setEditingClassroom(null);
  }, []);

  const columns = useClassroomColumns(handleEdit, canEdit);

  const defaultFormValues: Partial<ClassroomFormValues> | null =
    editingClassroom
      ? {
          code: editingClassroom.code,
          name: editingClassroom.name ?? "",
          capacity: editingClassroom.capacity,
          virtual: editingClassroom.virtual,
          active: editingClassroom.active,
        }
      : null;

  const onSubmit = (values: ClassroomFormValues) => {
    const capacity = values.virtual ? null : values.capacity;
    if (editingClassroom) {
      updateClassroom(
        {
          id: editingClassroom.id,
          classroom: {
            code: values.code,
            name: values.name,
            capacity,
            virtual: values.virtual,
            active: values.active ?? editingClassroom.active,
          },
        },
        { onSuccess: closeDialog }
      );
    } else {
      createClassroom(
        {
          code: values.code,
          name: values.name,
          capacity,
          virtual: values.virtual,
        },
        { onSuccess: closeDialog }
      );
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-6xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Aulas" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">Aulas</p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Lista de Aulas
            </h1>
            <p className="text-sm text-gray-600">
              Gestiona las aulas físicas y virtuales donde se dictan las
              secciones
            </p>
          </div>
          {canCreate && (
            <Button onClick={handleCreate} disabled={isCreating || isUpdating}>
              Crear aula
            </Button>
          )}
        </div>

        <Card>
          <DataTable
            data={classrooms || []}
            columns={columns}
            isLoading={isPending || isRefetching}
            onRetry={() => refetch()}
            emptyMessage="No hay aulas registradas."
          />
        </Card>

        <ClassroomDialog
          dialogOpen={dialogOpen}
          setDialogOpen={closeDialog}
          onSubmit={onSubmit}
          defaultFormValues={defaultFormValues}
          isEditing={!!editingClassroom}
          isSubmitting={isCreating || isUpdating}
        />
      </div>
    </div>
  );
};

export default ClassroomPage;
