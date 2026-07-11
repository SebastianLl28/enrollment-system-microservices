import { useForm } from "react-hook-form";
import type { ClassroomRequest } from "../types/request";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";

export type ClassroomFormValues = ClassroomRequest & {
  active?: boolean;
};

interface ClassroomFormProps {
  onSubmit: (values: ClassroomFormValues) => void;
  defaultFormValues?: Partial<ClassroomFormValues> | null;
  isEditing?: boolean;
}

const ClassroomForm = ({
  onSubmit,
  defaultFormValues = null,
  isEditing = false,
}: ClassroomFormProps) => {
  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: { errors },
  } = useForm<ClassroomFormValues>({
    defaultValues: defaultFormValues ?? { virtual: false, active: true },
  });

  const isVirtual = watch("virtual");

  return (
    <form
      id="classroom-form"
      onSubmit={handleSubmit(onSubmit)}
      className="space-y-4"
    >
      <div className="space-y-2">
        <Label htmlFor="code">Código</Label>
        <Input
          id="code"
          {...register("code", { required: "Requerido" })}
          placeholder="A-301"
        />
        {errors.code && (
          <p className="text-sm text-red-600">{errors.code.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="name">Nombre (opcional)</Label>
        <Input
          id="name"
          {...register("name")}
          placeholder="Laboratorio de Cómputo 3"
        />
      </div>

      <div className="flex items-center gap-2">
        <input
          id="virtual"
          type="checkbox"
          className="h-4 w-4 rounded border-gray-300"
          {...register("virtual", {
            onChange: (e) => {
              // Un aula virtual no tiene capacidad física.
              if (e.target.checked) setValue("capacity", null);
            },
          })}
        />
        <Label htmlFor="virtual" className="m-0!">
          Aula virtual (sin límite de capacidad)
        </Label>
      </div>

      <div className="space-y-2">
        <Label htmlFor="capacity">Capacidad</Label>
        <Input
          id="capacity"
          type="number"
          min={1}
          disabled={isVirtual}
          placeholder={isVirtual ? "Sin límite" : "30"}
          {...register("capacity", {
            valueAsNumber: true,
            validate: (value) =>
              isVirtual ||
              (value != null && !Number.isNaN(value) && value > 0) ||
              "Un aula física requiere capacidad mayor a 0",
          })}
        />
        {errors.capacity && (
          <p className="text-sm text-red-600">{errors.capacity.message}</p>
        )}
      </div>

      {isEditing && (
        <div className="flex items-center gap-2">
          <input
            id="active"
            type="checkbox"
            className="h-4 w-4 rounded border-gray-300"
            {...register("active")}
          />
          <Label htmlFor="active" className="m-0!">
            Activo
          </Label>
        </div>
      )}
    </form>
  );
};

export default ClassroomForm;
