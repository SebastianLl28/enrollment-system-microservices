import { Controller, useFieldArray, useForm } from "react-hook-form";
import Select from "react-select";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Plus, Trash2 } from "lucide-react";
import type { Course, CourseFormValues } from "../types/Course";
import { useGetCareers } from "@/features/career/hooks/useQuery";

interface CourseFormProps {
  onSubmit: (values: CourseFormValues) => void;
  defaultValues?: Course | null;
}

const CourseForm = ({ onSubmit, defaultValues }: CourseFormProps) => {
  const isEditing = !!defaultValues;

  const {
    handleSubmit,
    register,
    control,
    formState: { errors },
  } = useForm<CourseFormValues>({
    defaultValues: defaultValues
      ? {
          code: defaultValues.code,
          name: defaultValues.name,
          description: defaultValues.description ?? "",
          credits: defaultValues.credits,
          careers: defaultValues.careers.map((assignment) => ({
            careerId: assignment.careerId,
            semesterLevel: assignment.semesterLevel,
          })),
          active: defaultValues.active,
        }
      : {
          code: "",
          name: "",
          description: "",
          credits: 3,
          careers: [{ careerId: null, semesterLevel: 1 }],
        },
  });

  const { fields, append, remove } = useFieldArray({
    control,
    name: "careers",
  });

  const { data: careers, isPending: isLoadingCareers } = useGetCareers({ includeInactive: false });

  const careerOptions =
    careers?.map((career) => ({
      value: career.id,
      label: career.name,
    })) ?? [];

  return (
    <form
      className="space-y-4"
      onSubmit={handleSubmit(onSubmit)}
      id="course-form"
    >
      <div className="space-y-2">
        <Label htmlFor="code">Código</Label>
        <Input
          id="code"
          {...register("code", { required: "Requerido" })}
          placeholder="CS101"
        />
        {errors.code && (
          <p className="text-sm text-red-600">{errors.code.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="name">Nombre</Label>
        <Input
          id="name"
          {...register("name", { required: "Requerido" })}
          placeholder="Introducción a la Programación"
        />
        {errors.name && (
          <p className="text-sm text-red-600">{errors.name.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="description">Descripción</Label>
        <textarea
          id="description"
          {...register("description")}
          className="w-full rounded-md border border-gray-300 p-2 text-sm outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500"
          placeholder="Breve resumen del curso..."
          rows={3}
        />
      </div>

      <div className="space-y-2">
        <Label htmlFor="credits">Créditos</Label>
        <Input
          id="credits"
          type="number"
          min={1}
          {...register("credits", {
            required: "Requerido",
            valueAsNumber: true,
            min: { value: 1, message: "Debe ser mayor a 0" },
          })}
        />
        {errors.credits && (
          <p className="text-sm text-red-600">{errors.credits.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <div className="flex items-center justify-between">
          <Label>Carreras (malla curricular)</Label>
          <Button
            type="button"
            size="sm"
            variant="outline"
            onClick={() => append({ careerId: null, semesterLevel: 1 })}
          >
            <Plus className="h-4 w-4" />
            Agregar carrera
          </Button>
        </div>

        <div className="flex items-center gap-2">
          <span className="flex-1 text-xs font-medium text-gray-500">
            Carrera
          </span>
          <span className="w-28 text-xs font-medium text-gray-500">
            Ciclo
          </span>
          <span className="w-9" />
        </div>

        {fields.map((field, index) => (
          <div key={field.id} className="flex items-start gap-2">
            <div className="flex-1 space-y-1">
              <Controller
                control={control}
                name={`careers.${index}.careerId`}
                rules={{ required: "Selecciona una carrera" }}
                render={({ field: selectField }) => (
                  <Select
                    inputId={`careers-${index}-careerId`}
                    className="w-full"
                    options={careerOptions}
                    isLoading={isLoadingCareers}
                    placeholder="Selecciona una carrera"
                    value={
                      careerOptions.find(
                        (option) => option.value === selectField.value
                      ) ?? null
                    }
                    onChange={(option) =>
                      selectField.onChange(option?.value ?? null)
                    }
                    isClearable
                    classNamePrefix="react-select"
                  />
                )}
              />
              {errors.careers?.[index]?.careerId && (
                <p className="text-sm text-red-600">
                  {errors.careers[index]?.careerId?.message}
                </p>
              )}
            </div>

            <div className="w-28 space-y-1">
              <Input
                type="number"
                min={1}
                placeholder="Ciclo"
                {...register(`careers.${index}.semesterLevel`, {
                  required: "Requerido",
                  valueAsNumber: true,
                  min: { value: 1, message: "Debe ser mayor a 0" },
                })}
              />
              {errors.careers?.[index]?.semesterLevel && (
                <p className="text-sm text-red-600">
                  {errors.careers[index]?.semesterLevel?.message}
                </p>
              )}
            </div>

            <Button
              type="button"
              size="icon"
              variant="ghost"
              disabled={fields.length === 1}
              onClick={() => remove(index)}
            >
              <Trash2 className="h-4 w-4 text-red-500" />
            </Button>
          </div>
        ))}
        <p className="text-xs text-gray-500">
          El ciclo es el semestre de la malla en que se dicta el curso en esa
          carrera (puede variar entre carreras). Debe haber al menos una
          carrera asignada.
        </p>
      </div>

      {isEditing && (
        <div className="flex items-center gap-2">
          <input
            id="active"
            type="checkbox"
            {...register("active")}
            className="h-4 w-4 rounded border-gray-300"
          />
          <Label htmlFor="active">Activo</Label>
        </div>
      )}
    </form>
  );
};

export default CourseForm;
