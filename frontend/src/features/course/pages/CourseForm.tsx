import { Controller, useForm } from "react-hook-form";
import Select from "react-select";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import type { CourseFormValues } from "../types/Course";
import { useGetCareers } from "@/features/career/hooks/useQuery";

interface CourseFormProps {
  onSubmit: (values: CourseFormValues) => void;
}

const CourseForm = ({ onSubmit }: CourseFormProps) => {
  const {
    handleSubmit,
    register,
    control,
    formState: { errors },
  } = useForm<CourseFormValues>({
    defaultValues: {
      careerId: null,
      code: "",
      name: "",
      description: "",
      credits: 3,
      semesterLevel: 1,
    },
  });

  const { data: careers, isPending: isLoadingCareers } = useGetCareers();

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
        <Label htmlFor="careerId">Carrera</Label>
        <Controller
          control={control}
          name="careerId"
          rules={{ required: "Selecciona una carrera" }}
          render={({ field }) => (
            <Select
              inputId="careerId"
              className="w-full"
              options={careerOptions}
              isLoading={isLoadingCareers}
              placeholder="Selecciona una carrera"
              value={
                careerOptions.find((option) => option.value === field.value) ??
                null
              }
              onChange={(option) => field.onChange(option?.value ?? null)}
              isClearable
              classNamePrefix="react-select"
            />
          )}
        />
        {errors.careerId && (
          <p className="text-sm text-red-600">{errors.careerId.message}</p>
        )}
      </div>

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

      <div className="grid grid-cols-2 gap-4">
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
          <Label htmlFor="semesterLevel">Semestre</Label>
          <Input
            id="semesterLevel"
            type="number"
            min={1}
            {...register("semesterLevel", {
              required: "Requerido",
              valueAsNumber: true,
              min: { value: 1, message: "Debe ser mayor a 0" },
            })}
          />
          {errors.semesterLevel && (
            <p className="text-sm text-red-600">
              {errors.semesterLevel.message}
            </p>
          )}
        </div>
      </div>
    </form>
  );
};

export default CourseForm;
