import { ROUTE_PATHS } from "@/app/route/path";
import { useHasPermission } from "@/features/auth/hooks/usePermissions";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { DataTable } from "@/components/common/table/DataTable";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { useCallback, useState } from "react";
import CareerOfferingDialog from "../components/CareerOfferingDialog";
import type { CareerOfferingFormValues } from "../components/CareerOfferingForm";
import type { CareerOfferingResponse } from "../types/response";
import { useGetAllCareerOfferings } from "../hooks/useQuery";
import { useCareerOfferingColumns } from "@/config/columns";
import {
  usePostCareerOffering,
  usePutCareerOffering,
} from "../hooks/useMutation";

const CareerOfferingPage = () => {
  const canCreate = useHasPermission("UI_VIEW", "CREATE");
  const canEdit = useHasPermission("UI_VIEW", "UPDATE");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingOffering, setEditingOffering] =
    useState<CareerOfferingResponse | null>(null);
  const {
    data: careerOfferings,
    isRefetching,
    refetch,
    isPending,
  } = useGetAllCareerOfferings();
  const { mutate: createCareerOffering, isPending: isCreating } =
    usePostCareerOffering();
  const { mutate: updateCareerOffering, isPending: isUpdating } =
    usePutCareerOffering();

  const handleCreate = useCallback(() => {
    setEditingOffering(null);
    setDialogOpen(true);
  }, []);

  const handleEdit = useCallback((offering: CareerOfferingResponse) => {
    setEditingOffering(offering);
    setDialogOpen(true);
  }, []);

  const closeDialog = useCallback(() => {
    setDialogOpen(false);
    setEditingOffering(null);
  }, []);

  const columns = useCareerOfferingColumns(handleEdit, canEdit);

  const defaultFormValues: Partial<CareerOfferingFormValues> | null =
    editingOffering
      ? {
          careerId: editingOffering.career.id,
          termId: editingOffering.term.id,
          capacity: editingOffering.capacity,
          price: editingOffering.price ?? undefined,
          active: editingOffering.active,
        }
      : null;

  const onSubmit = (values: CareerOfferingFormValues) => {
    if (editingOffering) {
      updateCareerOffering(
        {
          id: editingOffering.id,
          careerOffering: {
            careerId: values.careerId,
            termId: values.termId,
            capacity: values.capacity,
            price: values.price,
            active: values.active ?? editingOffering.active,
          },
        },
        { onSuccess: closeDialog }
      );
    } else {
      createCareerOffering(
        {
          careerId: values.careerId,
          termId: values.termId,
          capacity: values.capacity,
          price: values.price,
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
            { label: "Carreras en Vigencia" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">
              Carreras en Vigencia
            </p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Lista de Carreras en Vigencia
            </h1>
            <p className="text-sm text-gray-600">
              Gestiona las carreras ofertadas por periodo (precio y vacantes)
            </p>
          </div>
          {canCreate && (
            <Button onClick={handleCreate} disabled={isCreating || isUpdating}>
              Crear carrera en vigencia
            </Button>
          )}
        </div>

        <Card>
          <DataTable
            data={careerOfferings || []}
            columns={columns}
            isLoading={isPending || isRefetching}
            onRetry={() => refetch()}
            emptyMessage="No hay carreras en vigencia registradas."
          />
        </Card>

        <CareerOfferingDialog
          dialogOpen={dialogOpen}
          setDialogOpen={closeDialog}
          onSubmit={onSubmit}
          defaultFormValues={defaultFormValues}
          isEditing={editingOffering !== null}
          isSubmitting={isCreating || isUpdating}
        />
      </div>
    </div>
  );
};

export default CareerOfferingPage;
