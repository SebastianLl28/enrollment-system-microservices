import { Controller, useForm } from "react-hook-form";
import type { CourseOfferingRequest } from "../types/request";
import { Label } from "@/components/ui/label";
import Select from "react-select";
import { useGetCourses } from "@/features/course/hooks/useQuery";
import { useGetTerms } from "@/features/term/hooks/useQuery";

export type CourseOfferingFormValues = CourseOfferingRequest & {
  active?: boolean;
};

interface CourseOfferingFormProps {
  onSubmit: (values: CourseOfferingFormValues) => void;
  defaultFormValues?: Partial<CourseOfferingFormValues> | null;
  isEditing?: boolean;
}

const CourseOfferingForm = ({
  onSubmit,
  defaultFormValues = null,
  isEditing = false,
}: CourseOfferingFormProps) => {
  const { register, handleSubmit, control } = useForm<CourseOfferingFormValues>({
    defaultValues: defaultFormValues ?? { active: true },
  });

  const { data: courses, isLoading: isLoadingCourses } = useGetCourses();
  const { data: terms, isLoading: isLoadingTerms } = useGetTerms();

  const courseOptions =
    courses?.map((course) => ({
      value: course.id,
      label: course.name,
    })) ?? [];

  const termOptions =
    terms?.map((term) => ({
      value: term.id,
      label: term.code,
    })) ?? [];

  return (
    <form
      id="course-offering-form"
      onSubmit={handleSubmit(onSubmit)}
      className="space-y-4"
    >
      <div className="space-y-2">
        <Label htmlFor="careerId">Carrera</Label>
        <Controller
          control={control}
          name="courseId"
          rules={{ required: "Selecciona un curso" }}
          render={({ field }) => (
            <Select
              inputId="courseId"
              className="w-full"
              options={courseOptions}
              isLoading={isLoadingCourses}
              placeholder="Selecciona un curso"
              value={
                courseOptions.find((option) => option.value === field.value) ??
                null
              }
              onChange={(option) => field.onChange(option?.value ?? null)}
              isClearable
              classNamePrefix="react-select"
            />
          )}
        />
      </div>
      <div className="space-y-2">
        <Label htmlFor="termId">Periodo</Label>
        <Controller
          control={control}
          name="termId"
          rules={{ required: "Selecciona un periodo" }}
          render={({ field }) => (
            <Select
              inputId="termId"
              className="w-full"
              options={termOptions}
              isLoading={isLoadingTerms}
              placeholder="Selecciona un periodo"
              value={
                termOptions.find((option) => option.value === field.value) ??
                null
              }
              onChange={(option) => field.onChange(option?.value ?? null)}
              isClearable
              classNamePrefix="react-select"
            />
          )}
        />
      </div>
      <div className="space-y-2">
        <Label htmlFor="sectionCode">Código de sección</Label>
        <input
          type="text"
          id="sectionCode"
          className="w-full px-3 py-2 border border-gray-300 rounded-md"
          {...register("sectionCode", { required: "Requerido" })}
          placeholder="A001"
        />
      </div>
      <div className="space-y-2">
        <Label htmlFor="capacity">Capacidad</Label>
        <input
          type="number"
          id="capacity"
          className="w-full px-3 py-2 border border-gray-300 rounded-md"
          {...register("capacity", {
            required: "Requerido",
            valueAsNumber: true,
          })}
          placeholder="30"
        />
      </div>
      <div className="space-y-2">
        <Label htmlFor="price">Precio de inscripción (S/)</Label>
        <input
          type="number"
          id="price"
          step="0.01"
          min="0"
          className="w-full px-3 py-2 border border-gray-300 rounded-md"
          {...register("price", {
            required: "Requerido",
            valueAsNumber: true,
            min: { value: 0, message: "El precio no puede ser negativo" },
          })}
          placeholder="150.00"
        />
      </div>
      {isEditing && (
        <div className="flex items-center gap-3">
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

export default CourseOfferingForm;
