import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import CourseForm from "./CourseForm";
import type { CourseFormValues } from "../types/Course";

interface CourseDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: CourseFormValues) => void;
  isSubmitting?: boolean;
}

const CourseDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  isSubmitting = false,
}: CourseDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Crear curso</DialogTitle>
          <DialogDescription>
            Completa los datos para registrar un curso y asignarlo a una
            carrera.
          </DialogDescription>
        </DialogHeader>

        <CourseForm onSubmit={onSubmit} />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="course-form" disabled={isSubmitting}>
            {isSubmitting ? "Creando..." : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default CourseDialog;
