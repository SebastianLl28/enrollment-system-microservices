import { useCallback, useMemo, useState } from "react";
import { useHasPermission } from "@/features/auth/hooks/usePermissions";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import { useGetCourses } from "../hooks/useQuery";
import { useCourseColumns } from "@/config/columns";
import CourseDialog from "./CourseDialog";
import CourseDetailDialog from "./CourseDetailDialog";
import type {
  Course,
  CourseFormValues,
  CreateCoursePayload,
  UpdateCoursePayload,
} from "../types/Course";
import { usePostCourse, usePutCourse } from "../hooks/useMutation";
import { toast } from "sonner";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";

const CoursePage = () => {
  const canCreate = useHasPermission("UI_VIEW", "CREATE");
  const canEdit = useHasPermission("UI_VIEW", "UPDATE");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingCourse, setEditingCourse] = useState<Course | null>(null);
  const [detailCourse, setDetailCourse] = useState<Course | null>(null);

  const { data, isPending, isError, error, refetch, isRefetching } =
    useGetCourses();

  const courses = useMemo(() => data ?? [], [data]);

  const { mutate: createCourse, isPending: isCreating } = usePostCourse();
  const { mutate: updateCourse, isPending: isUpdating } = usePutCourse();

  const handleCreate = useCallback(() => {
    setEditingCourse(null);
    setDialogOpen(true);
  }, []);

  const handleEdit = useCallback((course: Course) => {
    setEditingCourse(course);
    setDialogOpen(true);
  }, []);

  const handleView = useCallback((course: Course) => {
    setDetailCourse(course);
  }, []);

  const closeDialog = useCallback(() => {
    setDialogOpen(false);
    setEditingCourse(null);
  }, []);

  const onSubmit = (values: CourseFormValues) => {
    if (values.careers.some((assignment) => assignment.careerId == null)) {
      toast.error("Selecciona una carrera en cada asignación");
      return;
    }
    const careers = values.careers.map((assignment) => ({
      careerId: assignment.careerId as number,
      semesterLevel: assignment.semesterLevel,
    }));

    if (editingCourse) {
      const payload: UpdateCoursePayload = {
        code: values.code,
        name: values.name,
        description: values.description,
        credits: values.credits,
        careers,
        active: values.active ?? editingCourse.active,
      };
      updateCourse(
        { id: editingCourse.id, course: payload },
        { onSuccess: closeDialog }
      );
    } else {
      const payload: CreateCoursePayload = {
        code: values.code,
        name: values.name,
        description: values.description,
        credits: values.credits,
        careers,
      };
      createCourse(payload, { onSuccess: closeDialog });
    }
  };

  const columns = useCourseColumns(handleView, handleEdit, canEdit);

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-6xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Cursos" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">Cursos</p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Lista de cursos
            </h1>
            <p className="text-sm text-gray-600">
              Gestiona los cursos y su malla curricular (carreras y ciclos).
            </p>
          </div>
          {canCreate && (
            <Button onClick={handleCreate} disabled={isCreating || isUpdating}>
              Crear curso
            </Button>
          )}
        </div>

        <Card>
          <DataTable
            data={courses}
            columns={columns}
            isLoading={isPending || isRefetching}
            error={isError ? (error as Error) : null}
            onRetry={() => refetch()}
            emptyMessage="No hay cursos registrados."
          />
        </Card>

        <CourseDialog
          dialogOpen={dialogOpen}
          setDialogOpen={closeDialog}
          onSubmit={onSubmit}
          editingCourse={editingCourse}
          isSubmitting={isCreating || isUpdating}
        />

        <CourseDetailDialog
          course={detailCourse}
          open={detailCourse !== null}
          onOpenChange={(open) => { if (!open) setDetailCourse(null); }}
        />
      </div>
    </div>
  );
};

export default CoursePage;
