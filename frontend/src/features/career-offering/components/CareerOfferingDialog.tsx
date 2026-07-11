import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import CareerOfferingForm, {
  type CareerOfferingFormValues,
} from "./CareerOfferingForm";

interface CareerOfferingDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: CareerOfferingFormValues) => void;
  defaultFormValues?: Partial<CareerOfferingFormValues> | null;
  isEditing?: boolean;
  isSubmitting?: boolean;
}

const CareerOfferingDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  defaultFormValues = null,
  isEditing = false,
  isSubmitting = false,
}: CareerOfferingDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            {isEditing ? "Editar carrera en vigencia" : "Carreras en Vigencia"}
          </DialogTitle>
          <DialogDescription>
            {isEditing
              ? "Actualiza los datos de la carrera en vigencia (por ejemplo, el precio de matrícula)."
              : "Completa los datos para ofertar una carrera en un periodo."}
          </DialogDescription>
        </DialogHeader>

        <CareerOfferingForm
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
            form="career-offering-form"
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

export default CareerOfferingDialog;
