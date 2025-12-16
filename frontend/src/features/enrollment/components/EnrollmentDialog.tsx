import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import type { EnrollmentRequest } from "../types/request";
import EnrollmentForm from "./EnrollmentForm";

interface EnrollmentDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: EnrollmentRequest) => void;
  isSubmitting?: boolean;
}

const EnrollmentDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  isSubmitting,
}: EnrollmentDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Cursos en Vigencia</DialogTitle>
          <DialogDescription>
            Completa los datos para registrar un curso en vigencia.
          </DialogDescription>
        </DialogHeader>

        <EnrollmentForm onSubmit={onSubmit} />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="enrollment-form" disabled={isSubmitting}>
            {isSubmitting ? "Creando..." : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default EnrollmentDialog;
