import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import CourseForm from "./CourseForm";
import type { Course, CourseFormValues } from "../types/Course";

interface CourseDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: CourseFormValues) => void;
  editingCourse?: Course | null;
  isSubmitting?: boolean;
}

const CourseDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  editingCourse = null,
  isSubmitting = false,
}: CourseDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            {editingCourse ? "Editar curso" : "Crear curso"}
          </DialogTitle>
        </DialogHeader>

        <CourseForm onSubmit={onSubmit} defaultValues={editingCourse} />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="course-form" disabled={isSubmitting}>
            {isSubmitting
              ? editingCourse
                ? "Guardando..."
                : "Creando..."
              : editingCourse
              ? "Guardar cambios"
              : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default CourseDialog;
