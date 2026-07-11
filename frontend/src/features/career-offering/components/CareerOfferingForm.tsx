import { Controller, useForm } from "react-hook-form";
import type { CareerOfferingRequest } from "../types/request";
import { Label } from "@/components/ui/label";
import Select from "react-select";
import { useGetCareers } from "@/features/career/hooks/useQuery";
import { useGetTerms } from "@/features/term/hooks/useQuery";

export type CareerOfferingFormValues = CareerOfferingRequest & {
  active?: boolean;
};

interface CareerOfferingFormProps {
  onSubmit: (values: CareerOfferingFormValues) => void;
  defaultFormValues?: Partial<CareerOfferingFormValues> | null;
  isEditing?: boolean;
}

const CareerOfferingForm = ({
  onSubmit,
  defaultFormValues = null,
  isEditing = false,
}: CareerOfferingFormProps) => {
  const { register, handleSubmit, control } = useForm<CareerOfferingFormValues>({
    defaultValues: defaultFormValues ?? { active: true },
  });

  const { data: careers, isLoading: isLoadingCareers } = useGetCareers({
    includeInactive: false,
  });
  const { data: terms, isLoading: isLoadingTerms } = useGetTerms();

  const careerOptions =
    careers?.map((career) => ({
      value: career.id,
      label: career.name,
    })) ?? [];

  const termOptions =
    terms?.map((term) => ({
      value: term.id,
      label: term.code,
    })) ?? [];

  return (
    <form
      id="career-offering-form"
      onSubmit={handleSubmit(onSubmit)}
      className="space-y-4"
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
        <Label htmlFor="capacity">Vacantes</Label>
        <input
          type="number"
          id="capacity"
          className="w-full px-3 py-2 border border-gray-300 rounded-md"
          {...register("capacity", {
            required: "Requerido",
            valueAsNumber: true,
          })}
          placeholder="100"
        />
      </div>
      <div className="space-y-2">
        <Label htmlFor="price">Precio de matrícula (S/)</Label>
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
          placeholder="450.00"
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

export default CareerOfferingForm;
