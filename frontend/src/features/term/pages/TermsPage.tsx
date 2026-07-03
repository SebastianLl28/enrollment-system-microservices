import { useTermColumns } from "@/config/columns";
import { useCallback, useMemo, useState } from "react";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import TermDialog from "../components/TermDialog";
import TermDetailDialog from "../components/TermDetailDialog";
import type { TermRequest } from "../types/request";
import type { TermResponse } from "../types/response";
import { useGetTerms } from "../hooks/useQuery";
import { usePostTerm, usePutTerm } from "../hooks/useMutation";
import { useHasPermission } from "@/features/auth/hooks/usePermissions";

const TermsPage = () => {
  const canCreate = useHasPermission("UI_VIEW", "CREATE");
  const canEdit = useHasPermission("UI_VIEW", "UPDATE");

  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingTerm, setEditingTerm] = useState<TermResponse | null>(null);
  const [detailTerm, setDetailTerm] = useState<TermResponse | null>(null);

  const { data, isPending, isRefetching, error, refetch, isError } =
    useGetTerms();

  const { mutate: createTerm, isPending: isCreating } = usePostTerm();
  const { mutate: updateTerm, isPending: isUpdating } = usePutTerm();

  const terms = useMemo(() => data ?? [], [data]);

  const handleCreate = useCallback(() => {
    setEditingTerm(null);
    setDialogOpen(true);
  }, []);

  const handleEdit = useCallback((term: TermResponse) => {
    setEditingTerm(term);
    setDialogOpen(true);
  }, []);

  const handleView = useCallback((term: TermResponse) => {
    setDetailTerm(term);
  }, []);

  const closeDialog = useCallback(() => {
    setDialogOpen(false);
    setEditingTerm(null);
  }, []);

  const defaultFormValues: TermRequest | null = editingTerm
    ? {
        code: editingTerm.code,
        startDate: editingTerm.startDate,
        endDate: editingTerm.endDate,
        active: editingTerm.active,
      }
    : null;

  const onSubmit = (values: TermRequest) => {
    if (editingTerm) {
      updateTerm(
        { id: editingTerm.id, term: values },
        { onSuccess: closeDialog }
      );
    } else {
      createTerm(values, { onSuccess: closeDialog });
    }
  };

  const columns = useTermColumns(handleView, handleEdit, canEdit);

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-5xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Vigencias" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">Vigencia</p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Lista de vigencias académicas
            </h1>
            <p className="text-sm text-gray-600">
              Gestiona las vigencias académicas. No pueden existir dos vigencias
              en el mismo rango de fechas.
            </p>
          </div>
          {canCreate && <Button onClick={handleCreate}>Crear vigencia</Button>}
        </div>

        <Card>
          <DataTable
            data={terms}
            columns={columns}
            isLoading={isPending || isRefetching}
            error={isError ? (error as Error) : null}
            onRetry={() => refetch()}
            emptyMessage="No hay vigencias registradas."
          />
        </Card>

        <TermDialog
          dialogOpen={dialogOpen}
          setDialogOpen={closeDialog}
          editingTerm={editingTerm}
          onSubmit={onSubmit}
          defaultFormValues={defaultFormValues}
          isSubmitting={isCreating || isUpdating}
        />

        <TermDetailDialog
          term={detailTerm}
          open={detailTerm !== null}
          onOpenChange={(open) => {
            if (!open) setDetailTerm(null);
          }}
        />
      </div>
    </div>
  );
};

export default TermsPage;
