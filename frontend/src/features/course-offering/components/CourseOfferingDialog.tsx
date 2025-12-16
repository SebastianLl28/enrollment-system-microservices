import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import type { CourseOfferingRequest } from "../types/request";
import CourseOfferingForm from "./CourseOfferingForm";

interface CourseDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: CourseOfferingRequest) => void;
  isSubmitting?: boolean;
}

const CourseOfferingDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  isSubmitting = false,
}: CourseDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Cursos en Vigencia</DialogTitle>
          <DialogDescription>
            Completa los datos para registrar un curso en vigencia.
          </DialogDescription>
        </DialogHeader>

        <CourseOfferingForm onSubmit={onSubmit} />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button
            type="submit"
            form="course-offering-form"
            disabled={isSubmitting}
          >
            {isSubmitting ? "Creando..." : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default CourseOfferingDialog;
