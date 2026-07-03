import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import type { Faculty } from "../types/Faculty";

interface FacultyDetailDialogProps {
  faculty: Faculty | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

const FacultyDetailDialog = ({
  faculty,
  open,
  onOpenChange,
}: FacultyDetailDialogProps) => {
  if (!faculty) return null;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Detalle de facultad</DialogTitle>
        </DialogHeader>

        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-x-4 gap-y-3 text-sm">
            <span className="font-medium text-gray-500">ID</span>
            <span>{faculty.id}</span>

            <span className="font-medium text-gray-500">Nombre</span>
            <span>{faculty.name}</span>

            <span className="font-medium text-gray-500">Ubicación</span>
            <span>{faculty.location}</span>

            <span className="font-medium text-gray-500">Decano</span>
            <span>{faculty.dean}</span>

            <span className="font-medium text-gray-500">Estado</span>
            <Badge variant={faculty.active ? "success" : "destructive"}>
              {faculty.active ? "Activo" : "Inactivo"}
            </Badge>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default FacultyDetailDialog;
