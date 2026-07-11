import { Controller, useForm } from "react-hook-form";
import type { EnrollmentRequest } from "../types/request";
import { Label } from "@radix-ui/react-label";
import Select from "react-select";
import { useGetStudents } from "@/features/students/hooks/useQuery";
import { useGetAllCareerOfferings } from "@/features/career-offering/hooks/useQuery";

interface EnrollmentFormProps {
  onSubmit: (data: EnrollmentRequest) => void;
}

const EnrollmentForm = ({ onSubmit }: EnrollmentFormProps) => {
  const { handleSubmit, control } = useForm<EnrollmentRequest>();
  const { data: students, isPending: isLoadingStudents } = useGetStudents();
  const { data: careerOfferings, isPending: isLoadingCareerOfferings } =
    useGetAllCareerOfferings();

  const studentOptions =
    students?.map((student) => ({
      value: student.id,
      label: `${student.name} ${student.lastName}`,
    })) ?? [];

  const careerOfferingOptions =
    careerOfferings?.map((offering) => ({
      value: offering.id,
      label: `${offering.term.code} - ${offering.career.name}${
        offering.price != null ? ` (S/ ${offering.price.toFixed(2)})` : ""
      }`,
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
        <Label htmlFor="careerOfferingId">Carrera en Vigencia</Label>
        <Controller
          control={control}
          name="careerOfferingId"
          rules={{ required: "Selecciona una carrera en vigencia" }}
          render={({ field }) => (
            <Select
              inputId="careerOfferingId"
              className="w-full"
              options={careerOfferingOptions}
              isLoading={isLoadingCareerOfferings}
              placeholder="Selecciona una Carrera en Vigencia"
              value={
                careerOfferingOptions.find(
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
