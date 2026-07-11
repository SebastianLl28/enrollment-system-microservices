import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import type { Course } from "../types/Course";

interface CourseDetailDialogProps {
  course: Course | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

const CourseDetailDialog = ({
  course,
  open,
  onOpenChange,
}: CourseDetailDialogProps) => {
  if (!course) return null;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-lg">
        <DialogHeader>
          <DialogTitle>Detalle del curso</DialogTitle>
        </DialogHeader>

        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-x-4 gap-y-3 text-sm">
            <span className="font-medium text-gray-500">ID</span>
            <span>{course.id}</span>

            <span className="font-medium text-gray-500">Código</span>
            <span className="font-mono">{course.code}</span>

            <span className="font-medium text-gray-500">Nombre</span>
            <span>{course.name}</span>

            <span className="font-medium text-gray-500">Descripción</span>
            <span>{course.description ?? "—"}</span>

            <span className="font-medium text-gray-500">Créditos</span>
            <span>{course.credits}</span>

            <span className="font-medium text-gray-500">Estado</span>
            <Badge variant={course.active ? "success" : "destructive"}>
              {course.active ? "Activo" : "Inactivo"}
            </Badge>
          </div>

          <div className="space-y-2">
            <p className="text-sm font-medium text-gray-500">
              Carreras en las que se dicta ({course.careers.length})
            </p>
            <ul className="space-y-1">
              {course.careers.map((assignment) => (
                <li
                  key={assignment.careerId}
                  className="flex items-center justify-between rounded-md bg-gray-50 px-3 py-1.5 text-sm"
                >
                  <span>{assignment.careerName}</span>
                  <Badge variant="default">
                    Ciclo {assignment.semesterLevel}
                  </Badge>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default CourseDetailDialog;
