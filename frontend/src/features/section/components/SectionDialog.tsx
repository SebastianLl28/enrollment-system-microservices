import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import SectionForm, { type SectionFormValues } from "./SectionForm";

interface SectionDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: SectionFormValues) => void;
  defaultFormValues?: Partial<SectionFormValues> | null;
  isEditing?: boolean;
  isSubmitting?: boolean;
}

const SectionDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  defaultFormValues = null,
  isEditing = false,
  isSubmitting = false,
}: SectionDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            {isEditing ? "Editar sección" : "Nueva sección"}
          </DialogTitle>
          <DialogDescription>
            {isEditing
              ? "Actualiza los datos de la sección."
              : "Asigna un curso a un aula en un periodo académico."}
          </DialogDescription>
        </DialogHeader>

        <SectionForm
          onSubmit={onSubmit}
          defaultFormValues={defaultFormValues}
          isEditing={isEditing}
        />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="section-form" disabled={isSubmitting}>
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

export default SectionDialog;
