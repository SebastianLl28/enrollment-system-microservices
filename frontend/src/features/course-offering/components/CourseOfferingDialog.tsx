import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import CourseOfferingForm, {
  type CourseOfferingFormValues,
} from "./CourseOfferingForm";

interface CourseDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: CourseOfferingFormValues) => void;
  defaultFormValues?: Partial<CourseOfferingFormValues> | null;
  isEditing?: boolean;
  isSubmitting?: boolean;
}

const CourseOfferingDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  defaultFormValues = null,
  isEditing = false,
  isSubmitting = false,
}: CourseDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            {isEditing ? "Editar curso en vigencia" : "Cursos en Vigencia"}
          </DialogTitle>
          <DialogDescription>
            {isEditing
              ? "Actualiza los datos del curso en vigencia (por ejemplo, el precio de inscripción)."
              : "Completa los datos para registrar un curso en vigencia."}
          </DialogDescription>
        </DialogHeader>

        <CourseOfferingForm
          onSubmit={onSubmit}
          defaultFormValues={defaultFormValues}
          isEditing={isEditing}
        />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button
            type="submit"
            form="course-offering-form"
            disabled={isSubmitting}
          >
            {isSubmitting
              ? isEditing
                ? "Guardando..."
                : "Creando..."
              : isEditing
              ? "Guardar cambios"
              : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default CourseOfferingDialog;
