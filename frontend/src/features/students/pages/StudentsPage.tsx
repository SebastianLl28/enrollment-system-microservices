import { useCallback, useMemo, useState } from "react";
import { useHasPermission } from "@/features/auth/hooks/usePermissions";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import { useGetStudents } from "../hooks/useQuery";
import { useStudentColumns } from "@/config/columns";
import StudentDialog from "./StudentDialog";
import StudentDetailDialog from "./StudentDetailDialog";
import type {
  CreateStudentPayload,
  Student,
  StudentFormValues,
  UpdateStudentPayload,
} from "../types/Student";
import { usePostStudent, usePutStudent } from "../hooks/useMutation";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";

const StudentsPage = () => {
  const canCreate = useHasPermission("STUDENT", "CREATE");
  const canEdit = useHasPermission("STUDENT", "UPDATE");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingStudent, setEditingStudent] = useState<Student | null>(null);
  const [detailStudent, setDetailStudent] = useState<Student | null>(null);

  const { data, isPending, isError, error, refetch, isRefetching } =
    useGetStudents();

  const students = useMemo(() => data ?? [], [data]);

  const { mutate: createStudent, isPending: isCreating } = usePostStudent();
  const { mutate: updateStudent, isPending: isUpdating } = usePutStudent();

  const handleCreate = useCallback(() => {
    setEditingStudent(null);
    setDialogOpen(true);
  }, []);

  const handleEdit = useCallback((student: Student) => {
    setEditingStudent(student);
    setDialogOpen(true);
  }, []);

  const handleView = useCallback((student: Student) => {
    setDetailStudent(student);
  }, []);

  const closeDialog = useCallback(() => {
    setDialogOpen(false);
    setEditingStudent(null);
  }, []);

  const onSubmit = (values: StudentFormValues) => {
    if (editingStudent) {
      const payload: UpdateStudentPayload = {
        name: values.name,
        lastName: values.lastName,
        email: values.email,
        documentNumber: values.documentNumber,
        phoneNumber: values.phoneNumber,
        birthDate: values.birthDate,
        address: values.address,
        active: values.active ?? editingStudent.active,
      };
      updateStudent(
        { id: editingStudent.id, student: payload },
        { onSuccess: closeDialog }
      );
    } else {
      const payload: CreateStudentPayload = {
        name: values.name,
        lastName: values.lastName,
        email: values.email,
        documentNumber: values.documentNumber,
        phoneNumber: values.phoneNumber,
        birthDate: values.birthDate,
        address: values.address,
      };
      createStudent(payload, { onSuccess: closeDialog });
    }
  };

  const columns = useStudentColumns(handleView, handleEdit, canEdit);

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
          {canCreate && (
            <Button onClick={handleCreate} disabled={isCreating || isUpdating}>
              Crear estudiante
            </Button>
          )}
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
          setDialogOpen={closeDialog}
          onSubmit={onSubmit}
          editingStudent={editingStudent}
          isSubmitting={isCreating || isUpdating}
        />

        <StudentDetailDialog
          student={detailStudent}
          open={detailStudent !== null}
          onOpenChange={(open) => {
            if (!open) setDetailStudent(null);
          }}
        />
      </div>
    </div>
  );
};

export default StudentsPage;
