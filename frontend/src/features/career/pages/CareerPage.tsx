import { useCallback, useMemo, useState } from "react";
import { useHasPermission } from "@/features/auth/hooks/usePermissions";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import { useGetCareers } from "../hooks/useQuery";
import { useCareerColumns } from "@/config/columns";
import CareerDialog from "./CareerDialog";
import CareerDetailDialog from "./CareerDetailDialog";
import type { Career, CareerFormValues, CreateCareerPayload, UpdateCareerPayload } from "../types/Career";
import { usePostCareer, usePutCareer } from "../hooks/useMutation";
import { toast } from "sonner";
import { getApiErrorMessage } from "@/lib/apiError";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";

const CareerPage = () => {
  const canCreate = useHasPermission("UI_VIEW", "CREATE");
  const canEdit = useHasPermission("UI_VIEW", "UPDATE");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingCareer, setEditingCareer] = useState<Career | null>(null);
  const [detailCareer, setDetailCareer] = useState<Career | null>(null);

  const { data, isPending, isError, error, refetch, isRefetching } =
    useGetCareers({ includeInactive: true });

  const careers = useMemo(() => data ?? [], [data]);

  const handleView = useCallback((career: Career) => {
    setDetailCareer(career);
  }, []);

  const handleEdit = useCallback((career: Career) => {
    setEditingCareer(career);
    setDialogOpen(true);
  }, []);

  const handleCreate = useCallback(() => {
    setEditingCareer(null);
    setDialogOpen(true);
  }, []);

  const { mutate: createCareer, isPending: isCreating } = usePostCareer();
  const { mutate: updateCareer, isPending: isUpdating } = usePutCareer();

  const onSubmit = (values: CareerFormValues) => {
    if (!values.facultyId) {
      toast.error("Selecciona una facultad");
      return;
    }

    if (editingCareer) {
      const payload: UpdateCareerPayload = {
        facultyId: values.facultyId,
        name: values.name,
        description: values.description,
        semesterLength: values.semesterLength,
        degreeAwarded: values.degreeAwarded,
        active: values.active ?? editingCareer.active,
      };
      updateCareer(
        { id: editingCareer.id, career: payload },
        {
          onSuccess: () => {
            setDialogOpen(false);
            setEditingCareer(null);
          },
          onError: (error) => {
            toast.error(getApiErrorMessage(error, "No se pudo actualizar la carrera"));
          },
        }
      );
    } else {
      const payload: CreateCareerPayload = {
        facultyId: values.facultyId,
        name: values.name,
        description: values.description,
        semesterLength: values.semesterLength,
        degreeAwarded: values.degreeAwarded,
      };
      createCareer(payload, {
        onSuccess: () => {
          setDialogOpen(false);
        },
        onError: (error) => {
          toast.error(getApiErrorMessage(error, "No se pudo crear la carrera"));
        },
      });
    }
  };

  const columns = useCareerColumns(handleView, handleEdit, canEdit);

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-6xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Carreras" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">Carreras</p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Lista de carreras
            </h1>
            <p className="text-sm text-gray-600">
              Gestiona las carreras, asignando facultades y registrando la
              duración y grado otorgado.
            </p>
          </div>
          {canCreate && (
            <Button onClick={handleCreate} disabled={isCreating || isUpdating}>
              Crear carrera
            </Button>
          )}
        </div>

        <Card>
          <DataTable
            data={careers}
            columns={columns}
            isLoading={isPending || isRefetching}
            error={isError ? (error as Error) : null}
            onRetry={() => refetch()}
            emptyMessage="No hay carreras registradas."
          />
        </Card>

        <CareerDialog
          dialogOpen={dialogOpen}
          setDialogOpen={(open) => {
            setDialogOpen(open);
            if (!open) setEditingCareer(null);
          }}
          onSubmit={onSubmit}
          editingCareer={editingCareer}
          isSubmitting={isCreating || isUpdating}
        />
        <CareerDetailDialog
          career={detailCareer}
          open={detailCareer !== null}
          onOpenChange={(open) => { if (!open) setDetailCareer(null); }}
        />
      </div>
    </div>
  );
};

export default CareerPage;
