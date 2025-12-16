import { Controller, useForm } from "react-hook-form";
import Select from "react-select";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import type { CareerFormValues } from "../types/Career";
import { useGetFaculties } from "@/features/faculty/hooks/useQuery";

interface CareerFormProps {
  onSubmit: (values: CareerFormValues) => void;
}

const CareerForm = ({ onSubmit }: CareerFormProps) => {
  const {
    handleSubmit,
    register,
    control,
    formState: { errors },
  } = useForm<CareerFormValues>({
    defaultValues: {
      facultyId: null,
      name: "",
      description: "",
      semesterLength: 8,
      degreeAwarded: "",
    },
  });

  const { data: faculties, isPending: isLoadingFaculties } = useGetFaculties();

  const facultyOptions =
    faculties?.map((faculty) => ({
      value: faculty.id,
      label: faculty.name,
    })) ?? [];

  return (
    <form
      className="space-y-4"
      onSubmit={handleSubmit(onSubmit)}
      id="career-form"
    >
      <div className="space-y-2">
        <Label htmlFor="facultyId">Facultad</Label>
        <Controller
          control={control}
          name="facultyId"
          rules={{ required: "Selecciona una facultad" }}
          render={({ field }) => (
            <Select
              inputId="facultyId"
              className="w-full"
              options={facultyOptions}
              isLoading={isLoadingFaculties}
              placeholder="Selecciona una facultad"
              value={
                facultyOptions.find(
                  (option) => option.value === field.value
                ) ?? null
              }
              onChange={(option) => field.onChange(option?.value ?? null)}
              isClearable
              classNamePrefix="react-select"
            />
          )}
        />
        {errors.facultyId && (
          <p className="text-sm text-red-600">{errors.facultyId.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="name">Nombre</Label>
        <Input
          id="name"
          {...register("name", { required: "Requerido" })}
          placeholder="Ingeniería de Sistemas"
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
          placeholder="Programa orientado a..."
          rows={3}
        />
      </div>

      <div className="space-y-2">
        <Label htmlFor="semesterLength">Duración (semestres)</Label>
        <Input
          id="semesterLength"
          type="number"
          min={1}
          {...register("semesterLength", {
            required: "Requerido",
            valueAsNumber: true,
            min: { value: 1, message: "Debe ser mayor a 0" },
          })}
        />
        {errors.semesterLength && (
          <p className="text-sm text-red-600">
            {errors.semesterLength.message}
          </p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="degreeAwarded">Grado otorgado</Label>
        <Input
          id="degreeAwarded"
          {...register("degreeAwarded", { required: "Requerido" })}
          placeholder="BSc / BA"
        />
        {errors.degreeAwarded && (
          <p className="text-sm text-red-600">
            {errors.degreeAwarded.message}
          </p>
        )}
      </div>
    </form>
  );
};

export default CareerForm;
