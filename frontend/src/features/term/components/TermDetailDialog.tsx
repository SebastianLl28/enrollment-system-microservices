import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import type { TermResponse } from "../types/response";

interface TermDetailDialogProps {
  term: TermResponse | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

const TermDetailDialog = ({
  term,
  open,
  onOpenChange,
}: TermDetailDialogProps) => {
  if (!term) return null;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle>Detalle de la vigencia</DialogTitle>
        </DialogHeader>

        <div className="grid grid-cols-2 gap-x-4 gap-y-3 text-sm">
          <span className="font-medium text-gray-500">ID</span>
          <span>{term.id}</span>

          <span className="font-medium text-gray-500">Código</span>
          <span className="font-mono">{term.code}</span>

          <span className="font-medium text-gray-500">Fecha de inicio</span>
          <span>{term.startDate}</span>

          <span className="font-medium text-gray-500">Fecha de fin</span>
          <span>{term.endDate}</span>

          <span className="font-medium text-gray-500">Estado</span>
          <Badge variant={term.active ? "success" : "destructive"}>
            {term.active ? "Activo" : "Inactivo"}
          </Badge>

          <span className="font-medium text-gray-500">Creación</span>
          <span>
            {term.createdAt
              ? new Date(term.createdAt).toLocaleDateString("es-PE")
              : "—"}
          </span>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default TermDetailDialog;
