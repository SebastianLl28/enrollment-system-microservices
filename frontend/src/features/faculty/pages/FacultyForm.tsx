import { useForm } from "react-hook-form";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import type { Faculty, FacultyFormValues } from "../types/Faculty";

interface FacultyFormProps {
  defaultFormValues: Faculty | null;
  onSubmit: (values: FacultyFormValues) => void;
}

const FacultyForm = ({ defaultFormValues, onSubmit }: FacultyFormProps) => {
  const { handleSubmit, register } = useForm<FacultyFormValues>({
    defaultValues: {
      name: defaultFormValues ? defaultFormValues.name : "",
      location: defaultFormValues ? defaultFormValues.location : "",
      active: defaultFormValues ? defaultFormValues.active : true,
      dean: defaultFormValues ? defaultFormValues.dean : "",
    },
  });

  return (
    <form
      className="space-y-4"
      onSubmit={handleSubmit(onSubmit)}
      id="faculty-form"
    >
      <div className="space-y-2">
        <Label htmlFor="name">Nombre</Label>
        <Input
          id="name"
          {...register("name", { required: "Requerido" })}
          placeholder="Engineering"
        />
      </div>

      <div className="space-y-2">
        <Label htmlFor="location">Ubicación</Label>
        <Input
          id="location"
          {...register("location", { required: "Requerido" })}
          placeholder="Building A"
        />
      </div>

      <div className="space-y-2">
        <Label htmlFor="dean">Decano</Label>
        <Input
          id="dean"
          {...register("dean", { required: "Requerido" })}
          placeholder="Dr. John Doe"
        />
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

export default FacultyForm;
