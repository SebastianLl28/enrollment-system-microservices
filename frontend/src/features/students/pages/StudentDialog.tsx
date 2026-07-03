import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import StudentForm from "./StudentForm";
import type { Student, StudentFormValues } from "../types/Student";

interface StudentDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: StudentFormValues) => void;
  editingStudent?: Student | null;
  isSubmitting?: boolean;
}

const StudentDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  editingStudent = null,
  isSubmitting = false,
}: StudentDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            {editingStudent ? "Editar estudiante" : "Crear estudiante"}
          </DialogTitle>
        </DialogHeader>

        <StudentForm onSubmit={onSubmit} defaultValues={editingStudent} />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="student-form" disabled={isSubmitting}>
            {isSubmitting
              ? editingStudent
                ? "Guardando..."
                : "Creando..."
              : editingStudent
              ? "Guardar cambios"
              : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default StudentDialog;
