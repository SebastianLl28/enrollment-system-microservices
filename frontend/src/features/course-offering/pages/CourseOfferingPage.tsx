import { ROUTE_PATHS } from "@/app/route/path";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { DataTable } from "@/components/common/table/DataTable";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { useState } from "react";
import CourseOfferingDialog from "../components/CourseOfferingDialog";
import type { CourseOfferingRequest } from "../types/request";
import { useGetAllCourseOfferings } from "../hooks/useQuery";
import { useCourseOfferingColumns } from "@/config/columns";
import { usePostCourseOffering } from "../hooks/useMutation";

const CourseOfferingPage = () => {
  const [dialogOpen, setDialogOpen] = useState(false);
  const {
    data: courseOfferings,
    isRefetching,
    refetch,
    isPending,
  } = useGetAllCourseOfferings();
  const { mutate: createCourseOffering, isPending: isCreating } =
    usePostCourseOffering();

  const columns = useCourseOfferingColumns();

  const onSubmit = (courseOffering: CourseOfferingRequest) => {
    // console.log(courseOffering);
    createCourseOffering(courseOffering);
    setDialogOpen(false);
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-6xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Cursos en Vigencia" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">
              Cursos en Vigencia
            </p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Lista de Cursos en Vigencia
            </h1>
            <p className="text-sm text-gray-600">
              Gestiona los cursos en vigencia
            </p>
          </div>
          <Button onClick={() => setDialogOpen(true)} disabled={isCreating}>
            Crear curso en vigencia
          </Button>
        </div>

        <Card>
          <DataTable
            data={courseOfferings || []}
            columns={columns}
            isLoading={isPending || isRefetching}
            onRetry={() => refetch()}
            emptyMessage="No hay cursos en vigencia registradas."
          />
        </Card>

        <CourseOfferingDialog
          dialogOpen={dialogOpen}
          setDialogOpen={setDialogOpen}
          onSubmit={onSubmit}
          isSubmitting={isCreating}
        />
      </div>
    </div>
  );
};

export default CourseOfferingPage;
