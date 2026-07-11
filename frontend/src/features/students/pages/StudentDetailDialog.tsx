import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import type { Student } from "../types/Student";
import { useGetEnrollment } from "@/features/enrollment/hooks/useQuery";
import { useHasPermission } from "@/features/auth/hooks/usePermissions";

const STATUS_LABEL: Record<string, string> = {
  PENDING: "Pendiente",
  PAID: "Pagado",
  COMPLETED: "Completado",
  CANCELLED: "Cancelado",
};

const STATUS_VARIANT: Record<
  string,
  "default" | "success" | "destructive" | "secondary"
> = {
  PENDING: "secondary",
  PAID: "default",
  COMPLETED: "success",
  CANCELLED: "destructive",
};

interface StudentDetailDialogProps {
  student: Student | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

const StudentDetailDialog = ({
  student,
  open,
  onOpenChange,
}: StudentDetailDialogProps) => {
  const canReadEnrollments = useHasPermission("ENROLLMENT", "READ");

  // Solo consulta si el usuario puede leer inscripciones y hay un estudiante seleccionado
  const { data: enrollmentsPage, isPending: isLoadingEnrollments } =
    useGetEnrollment(
      {
        studentId: student?.id ?? null,
        termId: null,
        careerId: null,
        page: 0,
        size: 100,
      },
      { enabled: canReadEnrollments && student != null }
    );

  if (!student) return null;

  const enrollments = enrollmentsPage?.content;

  const activeEnrollments =
    enrollments?.filter(
      (e) => e.status === "PENDING" || e.status === "PAID" || e.status === "COMPLETED"
    ) ?? [];

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl max-h-[85vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>
            {student.name} {student.lastName}
          </DialogTitle>
        </DialogHeader>

        <div className="space-y-6">
          {/* Datos personales */}
          <div>
            <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-gray-400">
              Datos personales
            </p>
            <div className="grid grid-cols-2 gap-x-4 gap-y-2 text-sm">
              <span className="font-medium text-gray-500">ID</span>
              <span>{student.id}</span>

              <span className="font-medium text-gray-500">Documento</span>
              <span>{student.documentNumber}</span>

              <span className="font-medium text-gray-500">Email</span>
              <span>{student.email}</span>

              <span className="font-medium text-gray-500">Teléfono</span>
              <span>{student.phoneNumber ?? "—"}</span>

              <span className="font-medium text-gray-500">Nacimiento</span>
              <span>{student.birthDate}</span>

              <span className="font-medium text-gray-500">Dirección</span>
              <span>{student.address ?? "—"}</span>

              <span className="font-medium text-gray-500">Estado</span>
              <Badge variant={student.active ? "success" : "destructive"}>
                {student.active ? "Activo" : "Inactivo"}
              </Badge>
            </div>
          </div>

          {/* Resumen de cursos */}
          {canReadEnrollments && (
          <div className="flex gap-4">
            <div className="flex-1 rounded-lg bg-blue-50 p-3 text-center">
              <p className="text-2xl font-bold text-blue-600">
                {isLoadingEnrollments ? "…" : (enrollmentsPage?.totalElements ?? 0)}
              </p>
              <p className="text-xs text-gray-500">Total inscripciones</p>
            </div>
            <div className="flex-1 rounded-lg bg-green-50 p-3 text-center">
              <p className="text-2xl font-bold text-green-600">
                {isLoadingEnrollments ? "…" : activeEnrollments.length}
              </p>
              <p className="text-xs text-gray-500">Matrículas activas</p>
            </div>
          </div>
          )}

          {/* Historial de inscripciones */}
          {canReadEnrollments && (
          <div>
            <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-gray-400">
              Historial de matrículas
            </p>

            {isLoadingEnrollments ? (
              <p className="text-sm text-gray-400">Cargando historial…</p>
            ) : !enrollments || enrollments.length === 0 ? (
              <p className="text-sm text-gray-400">
                Sin inscripciones registradas.
              </p>
            ) : (
              <div className="space-y-2">
                {enrollments.map((enrollment) => (
                  <div
                    key={enrollment.id}
                    className="flex items-center justify-between rounded-md border border-gray-100 bg-gray-50 px-3 py-2 text-sm"
                  >
                    <div>
                      <p className="font-medium">
                        {enrollment.careerOffering.career.name}
                      </p>
                      <p className="text-xs text-gray-500">
                        {enrollment.careerOffering.term.code}
                        {enrollment.careerOffering.price != null &&
                          ` · S/ ${enrollment.careerOffering.price.toFixed(2)}`}
                      </p>
                    </div>
                    <div className="flex flex-col items-end gap-1">
                      <Badge variant={STATUS_VARIANT[enrollment.status] ?? "default"}>
                        {STATUS_LABEL[enrollment.status] ?? enrollment.status}
                      </Badge>
                      <span className="text-xs text-gray-400">
                        {new Date(enrollment.enrollmentDate).toLocaleDateString(
                          "es-PE"
                        )}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default StudentDetailDialog;
