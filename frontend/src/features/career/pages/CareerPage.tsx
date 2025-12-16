import { useCallback, useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import { useGetCareers } from "../hooks/useQuery";
import { useCareerColumns } from "@/config/columns";
import CareerDialog from "./CareerDialog";
import type {
  Career,
  CareerFormValues,
  CreateCareerPayload,
} from "../types/Career";
import { usePostCareer } from "../hooks/useMutation";
import { toast } from "sonner";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";

const CareerPage = () => {
  const [dialogOpen, setDialogOpen] = useState(false);

  const { data, isPending, isError, error, refetch, isRefetching } =
    useGetCareers();

  const careers = useMemo(() => data ?? [], [data]);

  const handleView = useCallback((career: Career) => {
    console.log("Ver carrera", career);
  }, []);

  const { mutate: createCareer, isPending: isCreating } = usePostCareer();

  const onSubmit = (values: CareerFormValues) => {
    if (!values.facultyId) {
      toast.error("Selecciona una facultad");
      return;
    }

    const payload: CreateCareerPayload = {
      ...values,
      facultyId: values.facultyId,
    };

    createCareer(payload, {
      onSuccess: () => {
        setDialogOpen(false);
      },
      onError: () => {
        toast.error("No se pudo crear la carrera");
      },
    });
  };

  const columns = useCareerColumns(handleView);

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
          <Button onClick={() => setDialogOpen(true)} disabled={isCreating}>
            Crear carrera
          </Button>
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
          setDialogOpen={setDialogOpen}
          onSubmit={onSubmit}
          isSubmitting={isCreating}
        />
      </div>
    </div>
  );
};

export default CareerPage;
