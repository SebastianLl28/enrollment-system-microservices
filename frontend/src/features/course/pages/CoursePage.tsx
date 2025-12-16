import { useCallback, useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { DataTable } from "@/components/common/table/DataTable";
import { useGetCourses } from "../hooks/useQuery";
import { useCourseColumns } from "@/config/columns";
import CourseDialog from "./CourseDialog";
import type {
  Course,
  CourseFormValues,
  CreateCoursePayload,
} from "../types/Course";
import { usePostCourse } from "../hooks/useMutation";
import { toast } from "sonner";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { ROUTE_PATHS } from "@/app/route/path";

const CoursePage = () => {
  const [dialogOpen, setDialogOpen] = useState(false);

  const { data, isPending, isError, error, refetch, isRefetching } =
    useGetCourses();

  const courses = useMemo(() => data ?? [], [data]);

  const handleView = useCallback((course: Course) => {
    console.log("Ver curso", course);
  }, []);

  const { mutate: createCourse, isPending: isCreating } = usePostCourse();

  const onSubmit = (values: CourseFormValues) => {
    if (!values.careerId) {
      toast.error("Selecciona una carrera");
      return;
    }

    const payload: CreateCoursePayload = {
      ...values,
      careerId: values.careerId,
    };

    createCourse(payload, {
      onSuccess: () => {
        setDialogOpen(false);
      },
      onError: () => {
        toast.error("No se pudo crear el curso");
      },
    });
  };

  const columns = useCourseColumns(handleView);

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
              Gestiona los cursos, asigna carrera, créditos y semestre.
            </p>
          </div>
          <Button onClick={() => setDialogOpen(true)} disabled={isCreating}>
            Crear curso
          </Button>
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
          setDialogOpen={setDialogOpen}
          onSubmit={onSubmit}
          isSubmitting={isCreating}
        />
      </div>
    </div>
  );
};

export default CoursePage;
