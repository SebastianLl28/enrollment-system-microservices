import { useCallback, useMemo, useState } from "react";
import { useHasPermission } from "@/features/auth/hooks/usePermissions";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { useGetFaculties } from "../hooks/useQuery";
import type { Faculty, FacultyFormValues } from "../types/Faculty";
import FacultyDialog from "./FacultyDialog";
import FacultyDetailDialog from "./FacultyDetailDialog";
import { useFacultyColumns } from "@/config/columns";
import { usePostFaculty, usePutFaculty } from "../hooks/useMutation";
import { DataTable } from "@/components/common/table/DataTable";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";

const FacultiesPage = () => {
  const canCreate = useHasPermission("UI_VIEW", "CREATE");
  const canEdit = useHasPermission("UI_VIEW", "UPDATE");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingFaculty, setEditingFaculty] = useState<Faculty | null>(null);
  const [detailFaculty, setDetailFaculty] = useState<Faculty | null>(null);

  const { data, isPending, isError, error, refetch, isRefetching } =
    useGetFaculties({ includeInactive: true });

  const faculties = useMemo(() => data ?? [], [data]);

  const handleCreate = useCallback(() => {
    setEditingFaculty(null);
    setDialogOpen(true);
  }, []);

  const handleEdit = useCallback((faculty: Faculty) => {
    setEditingFaculty(faculty);
    setDialogOpen(true);
  }, []);

  const handleView = useCallback((faculty: Faculty) => {
    setDetailFaculty(faculty);
  }, []);

  const { mutate: createFaculty, isPending: isCreating } = usePostFaculty();
  const { mutate: updateFaculty, isPending: isUpdating } = usePutFaculty();

  const closeDialog = useCallback(() => {
    setDialogOpen(false);
    setEditingFaculty(null);
  }, []);

  const onSubmit = (values: FacultyFormValues) => {
    if (editingFaculty) {
      updateFaculty({ id: editingFaculty.id, faculty: values }, { onSuccess: closeDialog });
    } else {
      createFaculty(values, { onSuccess: closeDialog });
    }
  };

  const columns = useFacultyColumns(handleEdit, handleView, canEdit);

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-5xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Facultades" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">Facultad</p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Lista de facultades
            </h1>
            <p className="text-sm text-gray-600">
              Gestiona las facultades. Las acciones actualmente realizan
              console.log para que luego conectes tus servicios.
            </p>
          </div>
          {canCreate && (
            <Button onClick={handleCreate} disabled={isCreating || isUpdating}>
              Crear facultad
            </Button>
          )}
        </div>

        <Card>
          <DataTable
            data={faculties}
            columns={columns}
            isLoading={isPending || isRefetching}
            error={isError ? (error as Error) : null}
            onRetry={() => refetch()}
            emptyMessage="No hay facultades registradas."
          />
        </Card>
        <FacultyDialog
          dialogOpen={dialogOpen}
          setDialogOpen={setDialogOpen}
          editingFaculty={editingFaculty}
          onSubmit={onSubmit}
          defaultFormValues={editingFaculty}
        />
        <FacultyDetailDialog
          faculty={detailFaculty}
          open={detailFaculty !== null}
          onOpenChange={(open) => { if (!open) setDetailFaculty(null); }}
        />
      </div>
    </div>
  );
};

export default FacultiesPage;
