import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import CareerForm from "./CareerForm";
import type { CareerFormValues } from "../types/Career";

interface CareerDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: CareerFormValues) => void;
  isSubmitting?: boolean;
}

const CareerDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  isSubmitting = false,
}: CareerDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Crear carrera</DialogTitle>
          <DialogDescription>
            Completa los datos para registrar una carrera.
          </DialogDescription>
        </DialogHeader>

        <CareerForm onSubmit={onSubmit} />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="career-form" disabled={isSubmitting}>
            {isSubmitting ? "Creando..." : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default CareerDialog;
