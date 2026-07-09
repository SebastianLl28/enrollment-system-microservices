import { ROUTE_PATHS } from "@/app/route/path";
import { useHasPermission } from "@/features/auth/hooks/usePermissions";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { DataTable } from "@/components/common/table/DataTable";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { useCallback, useState } from "react";
import CourseOfferingDialog from "../components/CourseOfferingDialog";
import type { CourseOfferingFormValues } from "../components/CourseOfferingForm";
import type { CourseOfferingResponse } from "../types/response";
import { useGetAllCourseOfferings } from "../hooks/useQuery";
import { useCourseOfferingColumns } from "@/config/columns";
import {
  usePostCourseOffering,
  usePutCourseOffering,
} from "../hooks/useMutation";

const CourseOfferingPage = () => {
  const canCreate = useHasPermission("UI_VIEW", "CREATE");
  const canEdit = useHasPermission("UI_VIEW", "UPDATE");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingOffering, setEditingOffering] =
    useState<CourseOfferingResponse | null>(null);
  const {
    data: courseOfferings,
    isRefetching,
    refetch,
    isPending,
  } = useGetAllCourseOfferings();
  const { mutate: createCourseOffering, isPending: isCreating } =
    usePostCourseOffering();
  const { mutate: updateCourseOffering, isPending: isUpdating } =
    usePutCourseOffering();

  const handleCreate = useCallback(() => {
    setEditingOffering(null);
    setDialogOpen(true);
  }, []);

  const handleEdit = useCallback((offering: CourseOfferingResponse) => {
    setEditingOffering(offering);
    setDialogOpen(true);
  }, []);

  const closeDialog = useCallback(() => {
    setDialogOpen(false);
    setEditingOffering(null);
  }, []);

  const columns = useCourseOfferingColumns(handleEdit, canEdit);

  const defaultFormValues: Partial<CourseOfferingFormValues> | null =
    editingOffering
      ? {
          courseId: editingOffering.course.id,
          termId: editingOffering.term.id,
          sectionCode: editingOffering.section,
          capacity: editingOffering.capacity,
          price: editingOffering.price ?? undefined,
          active: editingOffering.active,
        }
      : null;

  const onSubmit = (values: CourseOfferingFormValues) => {
    if (editingOffering) {
      updateCourseOffering(
        {
          id: editingOffering.id,
          courseOffering: {
            courseId: values.courseId,
            termId: values.termId,
            sectionCode: values.sectionCode,
            capacity: values.capacity,
            price: values.price,
            active: values.active ?? editingOffering.active,
          },
        },
        { onSuccess: closeDialog }
      );
    } else {
      createCourseOffering(
        {
          courseId: values.courseId,
          termId: values.termId,
          sectionCode: values.sectionCode,
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
          {canCreate && (
            <Button onClick={handleCreate} disabled={isCreating || isUpdating}>
              Crear curso en vigencia
            </Button>
          )}
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

export default CourseOfferingPage;
