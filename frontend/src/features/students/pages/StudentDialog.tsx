import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import StudentForm from "./StudentForm";
import type { StudentFormValues } from "../types/Student";

interface StudentDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: StudentFormValues) => void;
  isSubmitting?: boolean;
}

const StudentDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  isSubmitting = false,
}: StudentDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Crear estudiante</DialogTitle>
          <DialogDescription>
            Completa los datos del estudiante para registrarlo.
          </DialogDescription>
        </DialogHeader>

        <StudentForm onSubmit={onSubmit} />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="student-form" disabled={isSubmitting}>
            {isSubmitting ? "Creando..." : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default StudentDialog;
