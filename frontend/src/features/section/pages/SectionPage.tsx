import { ROUTE_PATHS } from "@/app/route/path";
import { useHasPermission } from "@/features/auth/hooks/usePermissions";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { DataTable } from "@/components/common/table/DataTable";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { useCallback, useState } from "react";
import SectionDialog from "../components/SectionDialog";
import type { SectionFormValues } from "../components/SectionForm";
import type { SectionResponse } from "../types/response";
import { useGetAllSections } from "../hooks/useQuery";
import { useSectionColumns } from "@/config/columns";
import { usePostSection, usePutSection } from "../hooks/useMutation";

const SectionPage = () => {
  const canCreate = useHasPermission("UI_VIEW", "CREATE");
  const canEdit = useHasPermission("UI_VIEW", "UPDATE");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingSection, setEditingSection] = useState<SectionResponse | null>(
    null
  );
  const {
    data: sections,
    isRefetching,
    refetch,
    isPending,
  } = useGetAllSections();
  const { mutate: createSection, isPending: isCreating } = usePostSection();
  const { mutate: updateSection, isPending: isUpdating } = usePutSection();

  const handleCreate = useCallback(() => {
    setEditingSection(null);
    setDialogOpen(true);
  }, []);

  const handleEdit = useCallback((section: SectionResponse) => {
    setEditingSection(section);
    setDialogOpen(true);
  }, []);

  const closeDialog = useCallback(() => {
    setDialogOpen(false);
    setEditingSection(null);
  }, []);

  const columns = useSectionColumns(handleEdit, canEdit);

  const defaultFormValues: Partial<SectionFormValues> | null = editingSection
    ? {
        courseId: editingSection.course.id,
        termId: editingSection.term.id,
        classroomId: editingSection.classroom.id,
        sectionCode: editingSection.sectionCode,
        active: editingSection.active,
      }
    : null;

  const onSubmit = (values: SectionFormValues) => {
    if (editingSection) {
      updateSection(
        {
          id: editingSection.id,
          section: {
            courseId: values.courseId,
            termId: values.termId,
            classroomId: values.classroomId,
            sectionCode: values.sectionCode,
            active: values.active ?? editingSection.active,
          },
        },
        { onSuccess: closeDialog }
      );
    } else {
      createSection(
        {
          courseId: values.courseId,
          termId: values.termId,
          classroomId: values.classroomId,
          sectionCode: values.sectionCode,
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
            { label: "Secciones" },
          ]}
        />

        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-blue-600">Secciones</p>
            <h1 className="text-2xl font-semibold text-gray-900">
              Lista de Secciones
            </h1>
            <p className="text-sm text-gray-600">
              Asigna cursos a aulas (físicas o virtuales) por periodo
            </p>
          </div>
          {canCreate && (
            <Button onClick={handleCreate} disabled={isCreating || isUpdating}>
              Crear sección
            </Button>
          )}
        </div>

        <Card>
          <DataTable
            data={sections || []}
            columns={columns}
            isLoading={isPending || isRefetching}
            onRetry={() => refetch()}
            emptyMessage="No hay secciones registradas."
          />
        </Card>

        <SectionDialog
          dialogOpen={dialogOpen}
          setDialogOpen={closeDialog}
          onSubmit={onSubmit}
          defaultFormValues={defaultFormValues}
          isEditing={!!editingSection}
          isSubmitting={isCreating || isUpdating}
        />
      </div>
    </div>
  );
};

export default SectionPage;
