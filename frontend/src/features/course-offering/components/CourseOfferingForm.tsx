import { Controller, useForm } from "react-hook-form";
import type { CourseOfferingRequest } from "../types/request";
import { Label } from "@/components/ui/label";
import Select from "react-select";
import { useGetCourses } from "@/features/course/hooks/useQuery";
import { useGetTerms } from "@/features/term/hooks/useQuery";

interface CourseOfferingFormProps {
  onSubmit: (values: CourseOfferingRequest) => void;
}

const CourseOfferingForm = ({ onSubmit }: CourseOfferingFormProps) => {
  const { register, handleSubmit, control } = useForm<CourseOfferingRequest>();

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
    </form>
  );
};

export default CourseOfferingForm;
