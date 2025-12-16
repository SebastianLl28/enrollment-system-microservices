import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  User,
  BookOpen,
  Calendar,
  CheckCircle,
  XCircle,
  Clock,
  Award,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import Select from "react-select";
import type { EnrollmentResponse } from "../types/response";
import type { EnrollmentRequestQuery } from "../types/request";
import { usePutEnrollment } from "../hooks/useMutation";

interface EnrollmentDetailDialogProps {
  dialogOpen: boolean;
  setDialogOpen: (open: boolean) => void;
  enrollment: EnrollmentResponse | null;
  query: EnrollmentRequestQuery;
}

const EnrollmentDetailDialog = ({
  dialogOpen,
  setDialogOpen,
  enrollment,
}: EnrollmentDetailDialogProps) => {
  const [selectedStatus, setSelectedStatus] = useState<
    "PENDING" | "PAID" | "CANCELLED" | "COMPLETED"
  >("PENDING");
  const [isUpdating, setIsUpdating] = useState(false);
  const { mutate: updateEnrollment } = usePutEnrollment();

  if (!enrollment) return null;

  const statusConfig = {
    PENDING: {
      label: "Pendiente",
      color: "bg-yellow-100 text-yellow-800",
      icon: Clock,
    },
    PAID: {
      label: "Pagado",
      color: "bg-green-100 text-green-800",
      icon: CheckCircle,
    },
    CANCELLED: {
      label: "Cancelado",
      color: "bg-red-100 text-red-800",
      icon: XCircle,
    },
    COMPLETED: {
      label: "Completado",
      color: "bg-blue-100 text-blue-800",
      icon: Award,
    },
  };

  const currentStatus = statusConfig[enrollment.status];
  const StatusIcon = currentStatus.icon;

  const formatDate = (date: Date) => {
    return new Date(date).toLocaleDateString("es-ES", {
      year: "numeric",
      month: "long",
      day: "numeric",
    });
  };

  const handleStatusChange = async () => {
    if (!selectedStatus || selectedStatus === enrollment.status) return;

    setIsUpdating(true);

    updateEnrollment({
      id: enrollment.id,
      status: selectedStatus,
    });

    setDialogOpen(false);
    setIsUpdating(false);
  };

  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent className="max-w-3xl max-h-[85vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="text-xl">Detalles de Inscripción</DialogTitle>
        </DialogHeader>

        <div className="space-y-6 py-4">
          {/* Estado Actual */}
          <div>
            <label className="text-sm font-medium text-gray-700 mb-2 block">
              Estado Actual
            </label>
            <div
              className={`inline-flex items-center gap-2 px-4 py-2 rounded-full ${currentStatus.color}`}
            >
              <StatusIcon size={20} />
              <span className="font-medium">{currentStatus.label}</span>
            </div>
          </div>

          {/* Cambiar Estado */}
          <div className="p-4 bg-blue-50 border border-blue-200 rounded-lg">
            <h3 className="font-semibold text-gray-800 mb-3">Cambiar Estado</h3>
            <div className="space-y-3">
              <Select
                value={
                  selectedStatus
                    ? {
                        value: selectedStatus,
                        label: statusConfig[selectedStatus].label,
                      }
                    : null
                }
                onChange={(option) =>
                  setSelectedStatus(option?.value ?? "PENDING")
                }
                options={Object.entries(statusConfig).map(([key, config]) => ({
                  value: key as "PENDING" | "PAID" | "CANCELLED" | "COMPLETED",
                  label: `${config.label}${
                    key === enrollment.status ? " (actual)" : ""
                  }`,
                  isDisabled: key === enrollment.status,
                }))}
                placeholder="Seleccionar nuevo estado..."
                className="react-select-container"
                classNamePrefix="react-select"
              />

              <Button
                onClick={handleStatusChange}
                disabled={
                  !selectedStatus ||
                  selectedStatus === enrollment.status ||
                  isUpdating ||
                  enrollment.status === "CANCELLED"
                }
                className="w-full"
              >
                {isUpdating ? "Actualizando..." : "Actualizar Estado"}
              </Button>
            </div>
          </div>

          {/* Información del Estudiante */}
          <div className="p-4 bg-gray-50 rounded-lg">
            <div className="flex items-center gap-2 mb-3">
              <User className="text-blue-600" size={20} />
              <h3 className="font-semibold text-gray-800">
                Información del Estudiante
              </h3>
            </div>
            <div className="space-y-2 ml-7">
              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-600">ID:</span>
                <span className="text-sm font-medium text-gray-900">
                  {enrollment.student.id}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-600">Nombre:</span>
                <span className="text-sm font-medium text-gray-900">
                  {enrollment.student.firstName} {enrollment.student.lastName}
                </span>
              </div>
            </div>
          </div>

          {/* Información del Curso */}
          <div className="p-4 bg-gray-50 rounded-lg">
            <div className="flex items-center gap-2 mb-3">
              <BookOpen className="text-green-600" size={20} />
              <h3 className="font-semibold text-gray-800">
                Información del Curso
              </h3>
            </div>
            <div className="space-y-2 ml-7">
              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-600">Curso:</span>
                <span className="text-sm font-medium text-gray-900">
                  {enrollment.courseOffering.course.name}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-600">Sección:</span>
                <span className="text-sm font-medium text-gray-900">
                  {enrollment.courseOffering.section}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-600">Periodo:</span>
                <span className="text-sm font-medium text-gray-900">
                  {enrollment.courseOffering.term.code}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-600">Capacidad:</span>
                <span className="text-sm font-medium text-gray-900">
                  {enrollment.courseOffering.enrolledCount} /{" "}
                  {enrollment.courseOffering.capacity}
                </span>
              </div>
            </div>
          </div>

          {/* Fechas */}
          <div className="p-4 bg-gray-50 rounded-lg">
            <div className="flex items-center gap-2 mb-3">
              <Calendar className="text-purple-600" size={20} />
              <h3 className="font-semibold text-gray-800">Fechas</h3>
            </div>
            <div className="space-y-2 ml-7">
              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-600">
                  Fecha de inscripción:
                </span>
                <span className="text-sm font-medium text-gray-900">
                  {formatDate(enrollment.enrollmentDate)}
                </span>
              </div>
              {enrollment.unenrollmentDate && (
                <div className="flex items-center gap-2">
                  <span className="text-sm text-gray-600">Fecha de baja:</span>
                  <span className="text-sm font-medium text-gray-900">
                    {formatDate(enrollment.unenrollmentDate)}
                  </span>
                </div>
              )}
              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-600">Periodo:</span>
                <span className="text-sm font-medium text-gray-900">
                  {new Date(
                    enrollment.courseOffering.term.startDate
                  ).toLocaleDateString("es-ES")}{" "}
                  -{" "}
                  {new Date(
                    enrollment.courseOffering.term.endDate
                  ).toLocaleDateString("es-ES")}
                </span>
              </div>
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default EnrollmentDetailDialog;
