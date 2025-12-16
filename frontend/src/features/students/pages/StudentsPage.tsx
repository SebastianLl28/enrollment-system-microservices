import { useCallback, useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import { useGetStudents } from "../hooks/useQuery";
import { useStudentColumns } from "@/config/columns";
import StudentDialog from "./StudentDialog";
import type {
  CreateStudentPayload,
  Student,
  StudentFormValues,
} from "../types/Student";
import { usePostStudent } from "../hooks/useMutation";
import { toast } from "sonner";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";

const StudentsPage = () => {
  const [dialogOpen, setDialogOpen] = useState(false);

  const { data, isPending, isError, error, refetch, isRefetching } =
    useGetStudents();

  const students = useMemo(() => data ?? [], [data]);

  const handleView = useCallback((student: Student) => {
    console.log("Ver estudiante", student);
  }, []);

  const { mutate: createStudent, isPending: isCreating } = usePostStudent();

  const onSubmit = (values: StudentFormValues) => {
    const payload: CreateStudentPayload = {
      ...values,
    };

    createStudent(payload, {
      onSuccess: () => {
        setDialogOpen(false);
      },
      onError: () => {
        toast.error("No se pudo crear el estudiante");
      },
    });
  };

  const columns = useStudentColumns(handleView);

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-6xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Estudiantes" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">Estudiantes</p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Lista de estudiantes
            </h1>
            <p className="text-sm text-gray-600">
              Gestiona estudiantes y sus datos de contacto.
            </p>
          </div>
          <Button onClick={() => setDialogOpen(true)} disabled={isCreating}>
            Crear estudiante
          </Button>
        </div>

        <Card>
          <DataTable
            data={students}
            columns={columns}
            isLoading={isPending || isRefetching}
            error={isError ? (error as Error) : null}
            onRetry={() => refetch()}
            emptyMessage="No hay estudiantes registrados."
          />
        </Card>

        <StudentDialog
          dialogOpen={dialogOpen}
          setDialogOpen={setDialogOpen}
          onSubmit={onSubmit}
          isSubmitting={isCreating}
        />
      </div>
    </div>
  );
};

export default StudentsPage;
