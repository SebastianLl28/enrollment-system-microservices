import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import CareerForm from "./CareerForm";
import type { Career, CareerFormValues } from "../types/Career";

interface CareerDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: CareerFormValues) => void;
  editingCareer?: Career | null;
  isSubmitting?: boolean;
}

const CareerDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  editingCareer = null,
  isSubmitting = false,
}: CareerDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            {editingCareer ? "Editar carrera" : "Crear carrera"}
          </DialogTitle>
        </DialogHeader>

        <CareerForm onSubmit={onSubmit} defaultValues={editingCareer} />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="career-form" disabled={isSubmitting}>
            {isSubmitting
              ? editingCareer
                ? "Guardando..."
                : "Creando..."
              : editingCareer
              ? "Guardar cambios"
              : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default CareerDialog;
