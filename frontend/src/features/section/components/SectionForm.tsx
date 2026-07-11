import { Controller, useForm } from "react-hook-form";
import type { SectionRequest } from "../types/request";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import Select from "react-select";
import { useGetCourses } from "@/features/course/hooks/useQuery";
import { useGetAllCareerOfferings } from "@/features/career-offering/hooks/useQuery";
import { useGetAllClassrooms } from "@/features/classroom/hooks/useQuery";

export type SectionFormValues = SectionRequest & {
  active?: boolean;
};

interface SectionFormProps {
  onSubmit: (values: SectionFormValues) => void;
  defaultFormValues?: Partial<SectionFormValues> | null;
  isEditing?: boolean;
}

const SectionForm = ({
  onSubmit,
  defaultFormValues = null,
  isEditing = false,
}: SectionFormProps) => {
  const {
    register,
    handleSubmit,
    control,
    watch,
    setValue,
    formState: { errors },
  } = useForm<SectionFormValues>({
    defaultValues: defaultFormValues ?? { active: true },
  });

  const { data: courses, isPending: isLoadingCourses } = useGetCourses();
  const { data: careerOfferings, isPending: isLoadingCareerOfferings } =
    useGetAllCareerOfferings();
  const { data: classrooms, isPending: isLoadingClassrooms } =
    useGetAllClassrooms();

  const selectedCourseId = watch("courseId");

  const courseOptions =
    courses?.map((course) => ({
      value: course.id,
      label: `${course.code} - ${course.name}`,
    })) ?? [];

  // Solo periodos donde alguna carrera con el curso en su malla está ofertada
  // (misma regla que valida el backend).
  const selectedCourseCareerIds =
    courses
      ?.find((course) => course.id === selectedCourseId)
      ?.careers.map((assignment) => assignment.careerId) ?? [];

  const termOptions = (careerOfferings ?? [])
    .filter(
      (offering) =>
        offering.active && selectedCourseCareerIds.includes(offering.career.id)
    )
    .reduce<{ value: number; label: string }[]>((options, offering) => {
      if (!options.some((option) => option.value === offering.term.id)) {
        options.push({ value: offering.term.id, label: offering.term.code });
      }
      return options;
    }, []);

  const classroomOptions =
    classrooms?.map((classroom) => ({
      value: classroom.id,
      label: `${classroom.code}${classroom.name ? ` - ${classroom.name}` : ""}${
        classroom.virtual ? " (Virtual)" : ""
      }`,
    })) ?? [];

  return (
    <form
      id="section-form"
      onSubmit={handleSubmit(onSubmit)}
      className="space-y-4"
    >
      <div className="space-y-2">
        <Label htmlFor="courseId">Curso</Label>
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
              onChange={(option) => {
                field.onChange(option?.value ?? null);
                // Los periodos disponibles dependen del curso elegido.
                setValue("termId", null as unknown as number);
              }}
              isClearable
              classNamePrefix="react-select"
            />
          )}
        />
        {errors.courseId && (
          <p className="text-sm text-red-600">{errors.courseId.message}</p>
        )}
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
              isLoading={isLoadingCareerOfferings}
              isDisabled={!selectedCourseId}
              placeholder={
                selectedCourseId
                  ? termOptions.length > 0
                    ? "Selecciona un periodo"
                    : "El curso no tiene carreras ofertadas en ningún periodo"
                  : "Selecciona primero un curso"
              }
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
        {errors.termId && (
          <p className="text-sm text-red-600">{errors.termId.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="classroomId">Aula</Label>
        <Controller
          control={control}
          name="classroomId"
          rules={{ required: "Selecciona un aula" }}
          render={({ field }) => (
            <Select
              inputId="classroomId"
              className="w-full"
              options={classroomOptions}
              isLoading={isLoadingClassrooms}
              placeholder="Selecciona un aula"
              value={
                classroomOptions.find(
                  (option) => option.value === field.value
                ) ?? null
              }
              onChange={(option) => field.onChange(option?.value ?? null)}
              isClearable
              classNamePrefix="react-select"
            />
          )}
        />
        {errors.classroomId && (
          <p className="text-sm text-red-600">{errors.classroomId.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="sectionCode">Código de sección</Label>
        <Input
          id="sectionCode"
          {...register("sectionCode", { required: "Requerido" })}
          placeholder="A"
        />
        {errors.sectionCode && (
          <p className="text-sm text-red-600">{errors.sectionCode.message}</p>
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

export default SectionForm;
