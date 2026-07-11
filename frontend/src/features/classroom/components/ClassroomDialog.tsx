import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import ClassroomForm, { type ClassroomFormValues } from "./ClassroomForm";

interface ClassroomDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  onSubmit: (values: ClassroomFormValues) => void;
  defaultFormValues?: Partial<ClassroomFormValues> | null;
  isEditing?: boolean;
  isSubmitting?: boolean;
}

const ClassroomDialog = ({
  dialogOpen,
  setDialogOpen,
  onSubmit,
  defaultFormValues = null,
  isEditing = false,
  isSubmitting = false,
}: ClassroomDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{isEditing ? "Editar aula" : "Nueva aula"}</DialogTitle>
          <DialogDescription>
            {isEditing
              ? "Actualiza los datos del aula."
              : "Registra un aula física (con capacidad) o virtual."}
          </DialogDescription>
        </DialogHeader>

        <ClassroomForm
          onSubmit={onSubmit}
          defaultFormValues={defaultFormValues}
          isEditing={isEditing}
        />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="classroom-form" disabled={isSubmitting}>
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

export default ClassroomDialog;
