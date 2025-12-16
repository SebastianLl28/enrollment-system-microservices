import { Controller, useForm } from "react-hook-form";
import type { EnrollmentRequest } from "../types/request";
import { Label } from "@radix-ui/react-label";
import Select from "react-select";
import { useGetStudents } from "@/features/students/hooks/useQuery";
import { useGetAllCourseOfferings } from "@/features/course-offering/hooks/useQuery";

interface EnrollmentFormProps {
  onSubmit: (data: EnrollmentRequest) => void;
}

const EnrollmentForm = ({ onSubmit }: EnrollmentFormProps) => {
  const { handleSubmit, control } = useForm<EnrollmentRequest>();
  const { data: students, isPending: isLoadingStudents } = useGetStudents();
  const { data: courseOfferings, isPending: isLoadingCourseOfferings } =
    useGetAllCourseOfferings();

  const studentOptions =
    students?.map((student) => ({
      value: student.id,
      label: `${student.name} ${student.lastName}`,
    })) ?? [];

  const courseOfferingOptions =
    courseOfferings?.map((offering) => ({
      value: offering.id,
      label: `${offering.term.code} - ${offering.course.name}`,
    })) ?? [];

  return (
    <form
      id="enrollment-form"
      onSubmit={handleSubmit(onSubmit)}
      className="space-y-4"
    >
      <div className="space-y-2">
        <Label htmlFor="studentId">Estudiante</Label>
        <Controller
          control={control}
          name="studentId"
          rules={{ required: "Selecciona un estudiante" }}
          render={({ field }) => (
            <Select
              inputId="studentId"
              className="w-full"
              options={studentOptions}
              isLoading={isLoadingStudents}
              placeholder="Selecciona un estudiante"
              value={
                studentOptions.find((option) => option.value === field.value) ??
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
          name="courseOfferingId"
          rules={{ required: "Selecciona un periodo" }}
          render={({ field }) => (
            <Select
              inputId="termId"
              className="w-full"
              options={courseOfferingOptions}
              isLoading={isLoadingCourseOfferings}
              placeholder="Selecciona un Curso en Vigencia"
              value={
                courseOfferingOptions.find(
                  (option) => option.value === field.value
                ) ?? null
              }
              onChange={(option) => field.onChange(option?.value ?? null)}
              isClearable
              classNamePrefix="react-select"
            />
          )}
        />
      </div>
    </form>
  );
};

export default EnrollmentForm;
