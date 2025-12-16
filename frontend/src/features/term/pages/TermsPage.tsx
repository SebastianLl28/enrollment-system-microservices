import { useTermColumns } from "@/config/columns";
import { useMemo, useState } from "react";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import TermDialog from "../components/TermDialog";
import type { TermRequest } from "../types/request";
import type { TermResponse } from "../types/response";
import { useGetTerms } from "../hooks/useQuery";
import { usePostTerm } from "../hooks/useMutation";

const TermsPage = () => {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingTerm] = useState<TermResponse | null>(null);

  const { data, isPending, isRefetching, error, refetch, isError } =
    useGetTerms();

  const { mutate: createTerm } = usePostTerm();

  const columns = useTermColumns();

  const terms = useMemo(() => data ?? [], [data]);

  const handleCreate = () => {
    setDialogOpen(true);
  };

  const onSubmit = (values: TermRequest) => {
    createTerm(values);
    setDialogOpen(false);
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-5xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Vige" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">Vigencia</p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Lista de vigencias académicas
            </h1>
            <p className="text-sm text-gray-600">
              Gestiona las vigencias académicas. Las acciones actualmente
              realizan console.log para que luego conectes tus servicios.
            </p>
          </div>
          <Button onClick={handleCreate}>Crear vigencia</Button>
        </div>

        <Card>
          <DataTable
            data={terms}
            columns={columns}
            isLoading={isPending || isRefetching}
            error={isError ? (error as Error) : null}
            onRetry={() => refetch()}
            emptyMessage="No hay facultades registradas."
          />
        </Card>
        <TermDialog
          dialogOpen={dialogOpen}
          setDialogOpen={setDialogOpen}
          editingTerm={editingTerm}
          onSubmit={onSubmit}
          defaultFormValues={editingTerm}
        />
      </div>
    </div>
  );
};

export default TermsPage;
