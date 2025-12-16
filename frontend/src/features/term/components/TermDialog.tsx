import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import TermForm from "./TermForm";
import type { TermRequest } from "../types/request";
import type { TermResponse } from "../types/response";

interface TermDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  editingTerm: TermResponse | null;
  onSubmit: (values: TermRequest) => void;
  defaultFormValues: TermRequest | null;
}

const TermDialog = ({
  dialogOpen,
  setDialogOpen,
  editingTerm,
  onSubmit,
  defaultFormValues,
}: TermDialogProps) => {
  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            {editingTerm ? "Editar vigencia" : "Crear vigencia"}
          </DialogTitle>
          <DialogDescription>
            Completa los datos de la vigencia. Este formulario solo hace
            console.log por ahora.
          </DialogDescription>
        </DialogHeader>

        <TermForm onSubmit={onSubmit} defaultFormValues={defaultFormValues} />

        <DialogFooter>
          <Button variant="outline" onClick={() => setDialogOpen(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="term-form">
            {editingTerm ? "Guardar cambios" : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default TermDialog;
