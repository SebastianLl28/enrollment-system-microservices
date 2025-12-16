import { ROUTE_PATHS } from "@/app/route/path";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { Button } from "@/components/ui/button";
import { useState } from "react";
import { useGetEnrollment } from "../hooks/useQuery";
import EnrollmentDialog from "../components/EnrollmentDialog";
import type {
  EnrollmentRequest,
  EnrollmentRequestQuery,
} from "../types/request";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import { useEnrollmentColumns } from "@/config/columns";
import { usePostEnrollment } from "../hooks/useMutation";
import EnrollmentSearchForm from "../components/EnrollmentSearchForm";
import type { EnrollmentResponse } from "../types/response";
import EnrollmentDetailDialog from "../components/EnrollmentDetailDialog";

const EnrollmentsPage = () => {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogDetailOpen, setDialogDetailOpen] = useState(false);
  const [selectedEnrollment, setSelectedEnrollment] =
    useState<EnrollmentResponse | null>(null);

  const [query, setQuery] = useState<EnrollmentRequestQuery>({
    studentId: null,
    termId: null,
    courseId: null,
  });

  const handleDetail = (enrollment: EnrollmentResponse) => {
    setSelectedEnrollment(enrollment);
    setDialogDetailOpen(true);
  };

  const columns = useEnrollmentColumns(handleDetail);

  const { data: enrollments, refetch, isLoading } = useGetEnrollment(query);

  const { mutate: createEnrollment, isPending: isCreating } =
    usePostEnrollment(query);

  const onSubmit = (courseOffering: EnrollmentRequest) => {
    setDialogOpen(false);
    createEnrollment(courseOffering);
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-6xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Inscripciones" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">Inscripciones</p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Lista de Inscripciones
            </h1>
            <p className="text-sm text-gray-600">
              Gestiona las inscripciones de los estudiantes
            </p>
          </div>
          <Button onClick={() => setDialogOpen(true)} disabled={isCreating}>
            Crear inscripción
          </Button>
        </div>

        <div>
          <EnrollmentSearchForm query={query} setQuery={setQuery} />
        </div>

        <Card>
          <DataTable
            data={enrollments || []}
            columns={columns}
            isLoading={isLoading}
            onRetry={() => refetch()}
            emptyMessage="No hay cursos en vigencia registradas."
          />
        </Card>

        <EnrollmentDialog
          dialogOpen={dialogOpen}
          setDialogOpen={setDialogOpen}
          onSubmit={onSubmit}
          isSubmitting={isCreating}
        />

        <EnrollmentDetailDialog
          dialogOpen={dialogDetailOpen}
          setDialogOpen={setDialogDetailOpen}
          enrollment={selectedEnrollment}
          query={query}
        />
      </div>
    </div>
  );
};

export default EnrollmentsPage;
