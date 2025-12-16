import * as z from "zod";

export const loginSchema = z.object({
  username: z.string().min(2).max(100),
  password: z
    .string()
    .min(4, { message: "La contraseña debe tener al menos 4 caracteres" }),
});

export type LoginFormData = z.infer<typeof loginSchema>;
