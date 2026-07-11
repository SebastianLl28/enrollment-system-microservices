import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import type { Career } from "../types/Career";

interface CareerDetailDialogProps {
  career: Career | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

const CareerDetailDialog = ({
  career,
  open,
  onOpenChange,
}: CareerDetailDialogProps) => {
  if (!career) return null;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-lg">
        <DialogHeader>
          <DialogTitle>Detalle de carrera</DialogTitle>
        </DialogHeader>

        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-x-4 gap-y-3 text-sm">
            <span className="font-medium text-gray-500">ID</span>
            <span>{career.id}</span>

            <span className="font-medium text-gray-500">Nombre</span>
            <span>{career.name}</span>

            <span className="font-medium text-gray-500">Facultad</span>
            <span>{career.faculty.name}</span>

            <span className="font-medium text-gray-500">Descripción</span>
            <span>{career.description ?? "—"}</span>

            <span className="font-medium text-gray-500">Grado otorgado</span>
            <span>{career.degreeAwarded}</span>

            <span className="font-medium text-gray-500">Duración</span>
            <span>{career.semesterLength} semestres</span>

            <span className="font-medium text-gray-500">Fecha de registro</span>
            <span>{new Date(career.registrationDate).toLocaleDateString()}</span>

            <span className="font-medium text-gray-500">Estado</span>
            <Badge variant={career.active ? "success" : "destructive"}>
              {career.active ? "Activo" : "Inactivo"}
            </Badge>
          </div>

          {career.courseList && career.courseList.length > 0 && (
            <div className="space-y-2">
              <p className="text-sm font-medium text-gray-500">
                Cursos ({career.courseList.length})
              </p>
              <ul className="space-y-1">
                {career.courseList.map((course) => (
                  <li
                    key={course.id}
                    className="flex items-center justify-between rounded-md bg-gray-50 px-3 py-1.5 text-sm"
                  >
                    <span>
                      <span className="font-mono text-xs text-gray-400 mr-2">
                        {course.code}
                      </span>
                      {course.name}
                    </span>
                    <span className="flex items-center gap-1">
                      <Badge variant="default" className="text-xs">
                        Ciclo {course.semesterLevel}
                      </Badge>
                      <Badge
                        variant={course.active ? "success" : "destructive"}
                        className="text-xs"
                      >
                        {course.active ? "Activo" : "Inactivo"}
                      </Badge>
                    </span>
                  </li>
                ))}
              </ul>
            </div>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default CareerDetailDialog;
