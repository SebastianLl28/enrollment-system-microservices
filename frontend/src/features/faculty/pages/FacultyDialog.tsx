import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import type { Faculty, FacultyFormValues } from "../types/Faculty";
import { Button } from "@/components/ui/button";
import FacultyForm from "./FacultyForm";

interface FacultyDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  editingFaculty: Faculty | null;
  onSubmit: (values: FacultyFormValues) => void;
  defaultFormValues: Faculty | null;
}

const FacultyDialog = ({
  dialogOpen,
  setDialogOpen,
  editingFaculty,
  onSubmit,
  defaultFormValues,
}: FacultyDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            {editingFaculty ? "Editar facultad" : "Crear facultad"}
          </DialogTitle>
          <DialogDescription>
            Completa los datos de la facultad. Este formulario solo hace
            console.log por ahora.
          </DialogDescription>
        </DialogHeader>

        <FacultyForm
          onSubmit={onSubmit}
          defaultFormValues={defaultFormValues}
        />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="faculty-form">
            {editingFaculty ? "Guardar cambios" : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default FacultyDialog;
