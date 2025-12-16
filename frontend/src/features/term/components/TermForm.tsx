import { useForm } from "react-hook-form";
import type { TermRequest } from "../types/request";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";

interface TermFormProps {
  defaultFormValues: TermRequest | null;
  onSubmit: (values: TermRequest) => void;
}

const TermForm = ({ onSubmit, defaultFormValues }: TermFormProps) => {
  const { register, handleSubmit } = useForm<TermRequest>({
    defaultValues: defaultFormValues
      ? {
          code: defaultFormValues.code,
          startDate: defaultFormValues.startDate,
          endDate: defaultFormValues.endDate,
          active: defaultFormValues.active,
        }
      : {
          code: "",
          startDate: "",
          endDate: "",
          active: true,
        },
  });

  return (
    <form
      className="space-y-4"
      onSubmit={handleSubmit(onSubmit)}
      id="term-form"
    >
      <div className="space-y-2">
        <Label htmlFor="code">Código</Label>
        <Input
          id="code"
          {...register("code", { required: "Requerido" })}
          placeholder="Engineering"
        />
      </div>

      <div className="space-y-2">
        <Label htmlFor="startDate">Fecha de inicio</Label>
        <Input id="startDate" type="date" {...register("startDate")} />
      </div>

      <div className="space-y-2">
        <Label htmlFor="endDate">Fecha de fin</Label>
        <Input id="endDate" type="date" {...register("endDate")} />
      </div>

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
    </form>
  );
};

export default TermForm;
