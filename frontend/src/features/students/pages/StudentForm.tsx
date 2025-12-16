import { useForm } from "react-hook-form";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import type { StudentFormValues } from "../types/Student";

interface StudentFormProps {
  onSubmit: (values: StudentFormValues) => void;
}

const StudentForm = ({ onSubmit }: StudentFormProps) => {
  const {
    handleSubmit,
    register,
    formState: { errors },
  } = useForm<StudentFormValues>({
    defaultValues: {
      name: "",
      lastName: "",
      email: "",
      documentNumber: "",
      phoneNumber: "",
      birthDate: "",
      address: "",
    },
  });

  return (
    <form
      className="space-y-4"
      onSubmit={handleSubmit(onSubmit)}
      id="student-form"
    >
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="name">Nombre</Label>
          <Input
            id="name"
            {...register("name", { required: "Requerido" })}
            placeholder="Alice"
          />
          {errors.name && (
            <p className="text-sm text-red-600">{errors.name.message}</p>
          )}
        </div>

        <div className="space-y-2">
          <Label htmlFor="lastName">Apellido</Label>
          <Input
            id="lastName"
            {...register("lastName", { required: "Requerido" })}
            placeholder="Johnson"
          />
          {errors.lastName && (
            <p className="text-sm text-red-600">{errors.lastName.message}</p>
          )}
        </div>
      </div>

      <div className="space-y-2">
        <Label htmlFor="email">Email</Label>
        <Input
          id="email"
          type="email"
          {...register("email", {
            required: "Requerido",
            pattern: {
              value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
              message: "Formato inválido",
            },
          })}
          placeholder="test@test.com"
        />
        {errors.email && (
          <p className="text-sm text-red-600">{errors.email.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="documentNumber">Documento</Label>
        <Input
          id="documentNumber"
          {...register("documentNumber", { required: "Requerido" })}
          placeholder="123456789"
        />
        {errors.documentNumber && (
          <p className="text-sm text-red-600">
            {errors.documentNumber.message}
          </p>
        )}
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="phoneNumber">Teléfono</Label>
          <Input
            id="phoneNumber"
            {...register("phoneNumber")}
            placeholder="555-1234"
          />
        </div>

        <div className="space-y-2">
          <Label htmlFor="birthDate">Fecha de nacimiento</Label>
          <Input
            id="birthDate"
            type="date"
            {...register("birthDate", { required: "Requerido" })}
          />
          {errors.birthDate && (
            <p className="text-sm text-red-600">{errors.birthDate.message}</p>
          )}
        </div>
      </div>

      <div className="space-y-2">
        <Label htmlFor="address">Dirección</Label>
        <Input
          id="address"
          {...register("address")}
          placeholder="123 Main St"
        />
      </div>
    </form>
  );
};

export default StudentForm;
