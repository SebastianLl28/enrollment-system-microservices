import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import type { Faculty } from "@/features/faculty/types/Faculty";
import type { Career } from "@/features/career/types/Career";
import type { Course } from "@/features/course/types/Course";
import type { Student } from "@/features/students/types/Student";
import type { CellContext, ColumnDef } from "@tanstack/react-table";
import { useMemo } from "react";
import type { TermResponse } from "@/features/term/types/response";
import type { CareerOfferingResponse } from "@/features/career-offering/types/response";
import type { ClassroomResponse } from "@/features/classroom/types/response";
import type { SectionResponse } from "@/features/section/types/response";
import type { EnrollmentResponse } from "@/features/enrollment/types/response";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
  CheckCircle2,
  MoreHorizontal,
  Pencil,
  Shield,
  Trash2,
  XCircle,
} from "lucide-react";
import type {
  PermissionResponse,
  RoleResponse,
  UserRbacResponse,
} from "@/features/rbac/types/response";

export const useFacultyColumns = (
  handleEdit: (faculty: Faculty) => void,
  handleView: (faculty: Faculty) => void,
  canWrite = true
): ColumnDef<Faculty>[] =>
  useMemo(
    () => [
      {
        header: "ID",
        accessorKey: "id",
      },
      {
        header: "Nombre",
        accessorKey: "name",
      },
      {
        header: "Ubicación",
        accessorKey: "location",
      },
      {
        header: "Decano",
        accessorKey: "dean",
      },
      {
        header: "Estado",
        accessorKey: "active",
        cell: ({ getValue }: CellContext<Faculty, unknown>) => {
          const active = getValue() as boolean;
          return (
            <Badge variant={active ? "success" : "destructive"}>
              {active ? "Activo" : "Inactivo"}
            </Badge>
          );
        },
      },
      {
        header: "Acciones",
        cell: ({ row }) => {
          const faculty = row.original;
          return (
            <div className="flex items-center gap-2">
              <Button
                size="sm"
                variant="secondary"
                onClick={() => handleView(faculty)}
              >
                Ver
              </Button>
              {canWrite && (
                <Button
                  size="sm"
                  variant="outline"
                  onClick={() => handleEdit(faculty)}
                >
                  Editar
                </Button>
              )}
            </div>
          );
        },
      },
    ],
    [handleEdit, handleView, canWrite]
  );

export const useCareerColumns = (
  handleView: (career: Career) => void,
  handleEdit: (career: Career) => void,
  canWrite = true
): ColumnDef<Career>[] =>
  useMemo(
    () => [
      {
        header: "ID",
        accessorKey: "id",
      },
      {
        header: "Nombre",
        accessorKey: "name",
      },
      {
        header: "Facultad",
        accessorFn: (row) => row.faculty.name,
      },
      {
        header: "Grado",
        accessorKey: "degreeAwarded",
      },
      {
        header: "Semestres",
        accessorKey: "semesterLength",
      },
      {
        header: "Cursos",
        cell: ({ row }) => row.original.courseList?.length ?? 0,
      },
      {
        header: "Estado",
        accessorKey: "active",
        cell: ({ getValue }: CellContext<Career, unknown>) => {
          const active = getValue() as boolean;
          return (
            <Badge variant={active ? "success" : "destructive"}>
              {active ? "Activo" : "Inactivo"}
            </Badge>
          );
        },
      },
      {
        header: "Acciones",
        cell: ({ row }) => {
          const career = row.original;
          return (
            <div className="flex items-center gap-2">
              <Button
                size="sm"
                variant="secondary"
                onClick={() => handleView(career)}
              >
                Ver
              </Button>
              {canWrite && (
                <Button
                  size="sm"
                  variant="outline"
                  onClick={() => handleEdit(career)}
                >
                  Editar
                </Button>
              )}
            </div>
          );
        },
      },
    ],
    [handleView, handleEdit, canWrite]
  );

export const useStudentColumns = (
  handleView: (student: Student) => void,
  handleEdit: (student: Student) => void,
  canWrite = true
): ColumnDef<Student>[] =>
  useMemo(
    () => [
      {
        header: "ID",
        accessorKey: "id",
      },
      {
        header: "Nombre",
        cell: ({ row }) => `${row.original.name} ${row.original.lastName}`,
      },
      {
        header: "Email",
        accessorKey: "email",
      },
      {
        header: "Documento",
        accessorKey: "documentNumber",
      },
      {
        header: "Teléfono",
        accessorKey: "phoneNumber",
      },
      {
        header: "Nacimiento",
        accessorKey: "birthDate",
      },
      {
        header: "Estado",
        accessorKey: "active",
        cell: ({ getValue }: CellContext<Student, unknown>) => {
          const active = getValue() as boolean;
          return (
            <Badge variant={active ? "success" : "destructive"}>
              {active ? "Activo" : "Inactivo"}
            </Badge>
          );
        },
      },
      {
        header: "Acciones",
        cell: ({ row }) => {
          const student = row.original;
          return (
            <div className="flex items-center gap-2">
              <Button
                size="sm"
                variant="secondary"
                onClick={() => handleView(student)}
              >
                Ver
              </Button>
              {canWrite && (
                <Button
                  size="sm"
                  variant="outline"
                  onClick={() => handleEdit(student)}
                >
                  Editar
                </Button>
              )}
            </div>
          );
        },
      },
    ],
    [handleView, handleEdit, canWrite]
  );

export const useCourseColumns = (
  handleView: (course: Course) => void,
  handleEdit: (course: Course) => void,
  canWrite = true
): ColumnDef<Course>[] =>
  useMemo(
    () => [
      {
        header: "ID",
        accessorKey: "id",
      },
      {
        header: "Código",
        accessorKey: "code",
      },
      {
        header: "Nombre",
        accessorKey: "name",
      },
      {
        header: "Créditos",
        accessorKey: "credits",
      },
      {
        header: "Carreras",
        cell: ({ row }) => {
          const careers = row.original.careers ?? [];
          if (careers.length === 0) return "—";
          return (
            <div className="flex flex-wrap gap-1">
              {careers.map((assignment) => (
                <Badge key={assignment.careerId} variant="default">
                  {assignment.careerName} · C{assignment.semesterLevel}
                </Badge>
              ))}
            </div>
          );
        },
      },
      {
        header: "Estado",
        accessorKey: "active",
        cell: ({ getValue }: CellContext<Course, unknown>) => {
          const active = getValue() as boolean;
          return (
            <Badge variant={active ? "success" : "destructive"}>
              {active ? "Activo" : "Inactivo"}
            </Badge>
          );
        },
      },
      {
        header: "Acciones",
        cell: ({ row }) => {
          const course = row.original;
          return (
            <div className="flex items-center gap-2">
              <Button
                size="sm"
                variant="secondary"
                onClick={() => handleView(course)}
              >
                Ver
              </Button>
              {canWrite && (
                <Button
                  size="sm"
                  variant="outline"
                  onClick={() => handleEdit(course)}
                >
                  Editar
                </Button>
              )}
            </div>
          );
        },
      },
    ],
    [handleView, handleEdit, canWrite]
  );

export const useTermColumns = (
  handleView: (term: TermResponse) => void,
  handleEdit: (term: TermResponse) => void,
  canWrite = true
): ColumnDef<TermResponse>[] =>
  useMemo(
    () => [
      {
        header: "Código",
        accessorKey: "code",
      },
      {
        header: "Fecha de inicio",
        accessorKey: "startDate",
      },
      {
        header: "Fecha de fin",
        accessorKey: "endDate",
      },
      {
        header: "Activo",
        accessorKey: "active",
        cell: ({ getValue }: CellContext<TermResponse, unknown>) => {
          const active = getValue() as boolean;
          return (
            <Badge variant={active ? "success" : "destructive"}>
              {active ? "Activo" : "Inactivo"}
            </Badge>
          );
        },
      },
      {
        header: "Acciones",
        cell: ({ row }) => {
          const term = row.original;
          return (
            <div className="flex items-center gap-2">
              <Button
                size="sm"
                variant="secondary"
                onClick={() => handleView(term)}
              >
                Ver
              </Button>
              {canWrite && (
                <Button
                  size="sm"
                  variant="outline"
                  onClick={() => handleEdit(term)}
                >
                  Editar
                </Button>
              )}
            </div>
          );
        },
      },
    ],
    [handleView, handleEdit, canWrite]
  );

export const useClassroomColumns = (
  handleEdit?: (classroom: ClassroomResponse) => void,
  canEdit = false
): ColumnDef<ClassroomResponse>[] =>
    useMemo(
      () => [
        {
          header: "Código",
          accessorKey: "code",
        },
        {
          header: "Nombre",
          cell: ({ row }: CellContext<ClassroomResponse, unknown>) =>
            row.original.name || "—",
        },
        {
          header: "Tipo",
          accessorKey: "virtual",
          cell: ({ getValue }: CellContext<ClassroomResponse, unknown>) => {
            const virtual = getValue() as boolean;
            return (
              <Badge variant={virtual ? "default" : "secondary"}>
                {virtual ? "Virtual" : "Física"}
              </Badge>
            );
          },
        },
        {
          header: "Capacidad",
          accessorKey: "capacity",
          cell: ({ row }: CellContext<ClassroomResponse, unknown>) =>
            row.original.virtual ? "Sin límite" : row.original.capacity,
        },
        {
          header: "Activo",
          accessorKey: "active",
          cell: ({ getValue }: CellContext<ClassroomResponse, unknown>) => {
            const active = getValue() as boolean;
            return (
              <Badge variant={active ? "success" : "destructive"}>
                {active ? "Activo" : "Inactivo"}
              </Badge>
            );
          },
        },
        ...(canEdit && handleEdit
          ? [
              {
                header: "Acciones",
                id: "actions",
                cell: ({ row }: CellContext<ClassroomResponse, unknown>) => (
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => handleEdit(row.original)}
                  >
                    Editar
                  </Button>
                ),
              } satisfies ColumnDef<ClassroomResponse>,
            ]
          : []),
      ],
      [handleEdit, canEdit]
    );

export const useSectionColumns = (
  handleEdit?: (section: SectionResponse) => void,
  canEdit = false
): ColumnDef<SectionResponse>[] =>
    useMemo(
      () => [
        {
          header: "Curso",
          cell: ({ row }: CellContext<SectionResponse, unknown>) => {
            const course = row.original.course;
            return `${course.code} - ${course.name}`;
          },
        },
        {
          header: "Periodo",
          cell: ({ row }: CellContext<SectionResponse, unknown>) =>
            row.original.term.code,
        },
        {
          header: "Sección",
          accessorKey: "sectionCode",
        },
        {
          header: "Aula",
          cell: ({ row }: CellContext<SectionResponse, unknown>) => {
            const classroom = row.original.classroom;
            return (
              <span className="flex items-center gap-1">
                {classroom.code}
                {classroom.virtual && <Badge variant="default">Virtual</Badge>}
              </span>
            );
          },
        },
        {
          header: "Capacidad",
          cell: ({ row }: CellContext<SectionResponse, unknown>) =>
            row.original.classroom.virtual
              ? "Sin límite"
              : row.original.classroom.capacity,
        },
        {
          header: "Activo",
          accessorKey: "active",
          cell: ({ getValue }: CellContext<SectionResponse, unknown>) => {
            const active = getValue() as boolean;
            return (
              <Badge variant={active ? "success" : "destructive"}>
                {active ? "Activo" : "Inactivo"}
              </Badge>
            );
          },
        },
        ...(canEdit && handleEdit
          ? [
              {
                header: "Acciones",
                id: "actions",
                cell: ({ row }: CellContext<SectionResponse, unknown>) => (
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => handleEdit(row.original)}
                  >
                    Editar
                  </Button>
                ),
              } satisfies ColumnDef<SectionResponse>,
            ]
          : []),
      ],
      [handleEdit, canEdit]
    );

export const useCareerOfferingColumns = (
  handleEdit?: (offering: CareerOfferingResponse) => void,
  canEdit = false
): ColumnDef<CareerOfferingResponse>[] =>
    useMemo(
      () => [
        {
          header: "Carrera",
          accessor: "career",
          cell: ({ row }: CellContext<CareerOfferingResponse, unknown>) => {
            const career = row.original.career;
            return career.name;
          },
        },
        {
          header: "Periodo",
          accessor: "term",
          cell: ({ row }: CellContext<CareerOfferingResponse, unknown>) => {
            const term = row.original.term;
            return `${term.code}`;
          },
        },
        {
          header: "Capacidad",
          accessorKey: "capacity",
        },
        {
          header: "Matriculados",
          accessorKey: "enrolledCount",
        },
        {
          header: "Precio",
          accessorKey: "price",
          cell: ({
            getValue,
          }: CellContext<CareerOfferingResponse, unknown>) => {
            const price = getValue() as number | null;
            return price != null ? `S/ ${price.toFixed(2)}` : "—";
          },
        },
        {
          header: "Creación",
          accessorKey: "createdAt",
          cell: ({
            getValue,
          }: CellContext<CareerOfferingResponse, unknown>) => {
            const date = new Date(getValue() as string);
            return date.toLocaleDateString();
          },
        },
        {
          header: "Activo",
          accessorKey: "active",
          cell: ({
            getValue,
          }: CellContext<CareerOfferingResponse, unknown>) => {
            const active = getValue() as boolean;
            return (
              <Badge variant={active ? "success" : "destructive"}>
                {active ? "Activo" : "Inactivo"}
              </Badge>
            );
          },
        },
        ...(canEdit && handleEdit
          ? [
              {
                header: "Acciones",
                id: "actions",
                cell: ({ row }: CellContext<CareerOfferingResponse, unknown>) => (
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => handleEdit(row.original)}
                  >
                    Editar
                  </Button>
                ),
              } satisfies ColumnDef<CareerOfferingResponse>,
            ]
          : []),
      ],
      [handleEdit, canEdit]
    );

export const useEnrollmentColumns = (
  handleDetail: (enrollment: EnrollmentResponse) => void
): ColumnDef<EnrollmentResponse>[] =>
  useMemo(
    () => [
      {
        header: "Estudiante",
        accessor: "student",
        cell: ({ row }: CellContext<EnrollmentResponse, unknown>) => {
          const student = row.original.student;
          return `${student.firstName} ${student.lastName}`;
        },
      },
      {
        header: "Carrera",
        accessor: "careerOffering",
        cell: ({ row }: CellContext<EnrollmentResponse, unknown>) => {
          const careerOffering = row.original.careerOffering;
          return careerOffering.career.name;
        },
      },
      {
        header: "Periodo",
        cell: ({ row }: CellContext<EnrollmentResponse, unknown>) => {
          return row.original.careerOffering.term.code;
        },
      },
      {
        header: "Fecha de inscripción",
        accessorKey: "enrollmentDate",
        cell: ({ getValue }: CellContext<EnrollmentResponse, unknown>) => {
          const date = new Date(getValue() as string);
          return date.toLocaleDateString();
        },
      },
      {
        header: "Fecha de baja",
        accessorKey: "unenrollmentDate",
        cell: ({ getValue }: CellContext<EnrollmentResponse, unknown>) => {
          const dateValue = getValue() as string | undefined;
          if (!dateValue) return "N/A";
          const date = new Date(dateValue);
          return date.toLocaleDateString();
        },
      },
      {
        header: "Estado",
        accessorKey: "status",
        cell: ({ getValue }: CellContext<EnrollmentResponse, unknown>) => {
          const status = getValue() as string;
          let variant: "default" | "success" | "warning" | "destructive" =
            "default";
          switch (status) {
            case "PENDING":
              variant = "warning";
              break;
            case "PAID":
              variant = "success";
              break;
            case "CANCELLED":
              variant = "destructive";
              break;
            case "COMPLETED":
              variant = "default";
              break;
          }
          return (
            <Badge variant={variant}>
              {status.charAt(0) + status.slice(1).toLowerCase()}
            </Badge>
          );
        },
      },
      {
        header: "Ver Detalle",
        cell: ({ row }) => {
          const enrollment = row.original;
          return (
            <div className="flex items-center gap-2">
              <Button
                size="sm"
                variant="secondary"
                onClick={() => handleDetail(enrollment)}
              >
                Detalle
              </Button>
            </div>
          );
        },
      },
    ],
    [handleDetail]
  );

export const useRbacRoleColumns = (
  handleEdit: (role: RoleResponse) => void,
  handleDelete: (id: number) => void,
  canManage = true
): ColumnDef<RoleResponse>[] =>
  useMemo(
    () => [
      {
        header: "ID",
        accessorKey: "id",
        cell: ({ getValue }) => (
          <span className="font-mono text-xs text-gray-500">
            #{getValue() as number}
          </span>
        ),
      },
      {
        header: "Nombre",
        accessorKey: "name",
        cell: ({ getValue }) => (
          <span className="font-semibold">{getValue() as string}</span>
        ),
      },
      {
        header: "Descripción",
        accessorKey: "description",
      },
      {
        header: "Permisos",
        accessorKey: "permissionIds",
        cell: ({ getValue }) => {
          const count = (getValue() as number[]).length;
          return (
            <Badge variant="outline">
              {count} {count === 1 ? "permiso" : "permisos"}
            </Badge>
          );
        },
      },
      {
        header: "Vistas",
        accessorKey: "viewCodes",
        cell: ({ getValue }) => {
          const count = (getValue() as string[]).length;
          return (
            <Badge variant="outline">
              {count} {count === 1 ? "vista" : "vistas"}
            </Badge>
          );
        },
      },
      {
        header: "Acciones",
        id: "actions",
        cell: ({ row }) =>
          canManage ? (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="ghost" size="sm">
                  <MoreHorizontal className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                <DropdownMenuItem onClick={() => handleEdit(row.original)}>
                  <Pencil className="mr-2 h-4 w-4" />
                  Editar
                </DropdownMenuItem>
                <DropdownMenuItem
                  onClick={() => handleDelete(row.original.id)}
                  className="text-red-600"
                >
                  <Trash2 className="mr-2 h-4 w-4" />
                  Eliminar
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : null,
      },
    ],
    [handleEdit, handleDelete, canManage]
  );

export const useRbacUserColumns = (
  handleAssignRoles: (user: UserRbacResponse) => void,
  canManage = true
): ColumnDef<UserRbacResponse>[] =>
  useMemo(
    () => [
      {
        header: "ID",
        accessorKey: "id",
        cell: ({ getValue }) => (
          <span className="font-mono text-xs text-gray-500">
            #{getValue() as number}
          </span>
        ),
      },
      {
        header: "Usuario",
        accessorKey: "username",
        cell: ({ row }) => (
          <div>
            <p className="font-semibold">{row.original.username}</p>
            <p className="text-xs text-gray-600">{row.original.email}</p>
          </div>
        ),
      },
      {
        header: "Nombre completo",
        accessorKey: "fullName",
      },
      {
        header: "2FA",
        accessorKey: "twoFactorEnabled",
        cell: ({ getValue }) => {
          const enabled = getValue() as boolean;
          return (
            <div className="flex items-center gap-1">
              {enabled ? (
                <>
                  <CheckCircle2 className="h-4 w-4 text-green-600" />
                  <span className="text-xs text-green-600">Activo</span>
                </>
              ) : (
                <>
                  <XCircle className="h-4 w-4 text-gray-400" />
                  <span className="text-xs text-gray-500">Inactivo</span>
                </>
              )}
            </div>
          );
        },
      },
      {
        header: "Permisos",
        accessorKey: "permissions",
        cell: ({ getValue }) => {
          const count = (getValue() as string[]).length;
          return (
            <Badge variant={count > 0 ? "default" : "outline"}>
              {count} {count === 1 ? "permiso" : "permisos"}
            </Badge>
          );
        },
      },
      {
        header: "Vistas",
        accessorKey: "uiViews",
        cell: ({ getValue }) => {
          const count = (getValue() as unknown[]).length;
          return (
            <Badge variant={count > 0 ? "default" : "outline"}>
              {count} {count === 1 ? "vista" : "vistas"}
            </Badge>
          );
        },
      },
      {
        header: "Acciones",
        id: "actions",
        cell: ({ row }) =>
          canManage ? (
            <Button
              variant="outline"
              size="sm"
              onClick={() => handleAssignRoles(row.original)}
            >
              <Shield className="mr-2 h-4 w-4" />
              Gestionar roles
            </Button>
          ) : null,
      },
    ],
    [handleAssignRoles, canManage]
  );

export const useRbacPermissionColumns = (): ColumnDef<PermissionResponse>[] =>
  useMemo(
    () => [
      {
        header: "ID",
        accessorKey: "id",
        cell: ({ getValue }) => (
          <span className="font-mono text-xs text-gray-500">
            #{getValue() as number}
          </span>
        ),
      },
      {
        header: "Recurso",
        accessorKey: "resource",
        cell: ({ getValue }) => (
          <Badge variant="outline">{getValue() as string}</Badge>
        ),
      },
      {
        header: "Operación",
        accessorKey: "operation",
        cell: ({ getValue }) => {
          const op = getValue() as string;
          const colors = {
            READ: "bg-blue-100 text-blue-700",
            CREATE: "bg-green-100 text-green-700",
            UPDATE: "bg-yellow-100 text-yellow-700",
            DELETE: "bg-red-100 text-red-700",
          };
          return (
            <Badge className={colors[op as keyof typeof colors]}>{op}</Badge>
          );
        },
      },
      {
        header: "Scope",
        accessorKey: "scope",
        cell: ({ getValue }) => {
          const scope = getValue() as string;
          return (
            <Badge variant={scope === "ALL" ? "default" : "secondary"}>
              {scope}
            </Badge>
          );
        },
      },
      {
        header: "Formato completo",
        id: "fullPermission",
        cell: ({ row }) => (
          <code className="text-xs bg-gray-100 px-2 py-1 rounded">
            {row.original.resource}:{row.original.operation}:
            {row.original.scope}
          </code>
        ),
      },
      {
        header: "Descripción",
        accessorKey: "description",
      },
    ],
    []
  );
