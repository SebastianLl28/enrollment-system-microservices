import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Eye, EyeOff, LogIn, Github, Info } from "lucide-react";
import { loginSchema, type LoginFormData } from "../schemas/loginSchema";
import { useLoginMutation } from "../hooks/mutation";
import { Link, useNavigate } from "react-router-dom";
import { ROUTE_PATHS } from "@/app/route/path";
import {
  OAUTH2_GITHUB_REDIRECT_URI,
  OAUTH2_GOOGLE_REDIRECT_URI,
} from "@/config/constants";
import { toast } from "sonner";
import GoogleImage from "@/assets/icon-google.png";

// Cuenta compartida de demostración: solo puede ver los módulos,
// sin crear ni editar (rol VIEWER del seed).
const DEMO_ACCOUNT = {
  username: "viewer123",
  password: "password123",
};

export default function LoginPage() {
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();
  const { mutate: login, isPending: isLoading } = useLoginMutation();

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

  const fillDemoAccount = () => {
    setValue("username", DEMO_ACCOUNT.username, { shouldValidate: true });
    setValue("password", DEMO_ACCOUNT.password, { shouldValidate: true });
  };

  const onSubmit = async (data: LoginFormData) => {
    login(data, {
      onSuccess: (response) => {
        if (response.twoFactorRequired) {
          // Redirigir a la página de verificación 2FA
          toast.info("Ingresa tu código de autenticación");
          navigate(ROUTE_PATHS.twoFactorVerify, {
            state: { username: response.username },
          });
        } else {
          // Login normal sin 2FA
          toast.success("Inicio de sesión exitoso");
          navigate(ROUTE_PATHS.dashboard, { replace: true });
        }
      },
      onError: () => {
        const errorMessage =
          "Credenciales inválidas. Por favor, verifica tu usuario y contraseña.";
        toast.error(errorMessage);
      },
    });
  };

  const handleGithubLogin = () => {
    // Navegación normal del navegador al backend
    window.location.href = OAUTH2_GITHUB_REDIRECT_URI;
  };

  const handleGoogleLogin = () => {
    window.location.href = OAUTH2_GOOGLE_REDIRECT_URI;
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4 bg-gray-100">
      <Card className="w-full max-w-md">
        <CardHeader className="space-y-1">
          <CardTitle className="text-2xl font-bold text-center">
            Iniciar Sesión
          </CardTitle>
          <CardDescription className="text-center">
            Ingresa tus credenciales para acceder
          </CardDescription>
        </CardHeader>
        <CardContent>
          {/* Cuenta demo de solo lectura */}
          <div className="mb-4 rounded-lg border border-blue-200 bg-blue-50 p-3">
            <div className="flex gap-2">
              <Info className="h-4 w-4 shrink-0 text-blue-600 mt-0.5" />
              <div className="text-sm text-blue-800">
                <p className="font-medium">Cuenta de invitado</p>
                <p className="text-blue-700">
                  Explora el sistema en modo solo lectura:{" "}
                  <span className="font-mono font-semibold">
                    {DEMO_ACCOUNT.username}
                  </span>{" "}
                  /{" "}
                  <span className="font-mono font-semibold">
                    {DEMO_ACCOUNT.password}
                  </span>
                </p>
                <button
                  type="button"
                  onClick={fillDemoAccount}
                  className="mt-1 font-medium text-blue-600 underline hover:text-blue-500"
                >
                  Usar esta cuenta
                </button>
              </div>
            </div>
          </div>

          {/* Login normal */}
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="username">Username</Label>
              <Input
                id="username"
                type="text"
                placeholder="usuario"
                {...register("username")}
                className={errors.username ? "border-red-500" : ""}
              />
              {errors.username && (
                <p className="text-sm text-red-500">
                  {errors.username.message}
                </p>
              )}
            </div>

            <div className="space-y-2">
              <Label htmlFor="password">Contraseña</Label>
              <div className="relative">
                <Input
                  id="password"
                  type={showPassword ? "text" : "password"}
                  placeholder="••••••••"
                  {...register("password")}
                  className={errors.password ? "border-red-500 pr-10" : "pr-10"}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700"
                >
                  {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                </button>
              </div>
              {errors.password && (
                <p className="text-sm text-red-500">
                  {errors.password.message}
                </p>
              )}
            </div>

            <Button type="submit" className="w-full" disabled={isLoading}>
              {isLoading ? (
                <>Cargando...</>
              ) : (
                <>
                  <LogIn className="mr-2 h-4 w-4" />
                  Iniciar Sesión
                </>
              )}
            </Button>
          </form>

          {/* Separador */}
          <div className="flex items-center my-4">
            <div className="flex-1 h-px bg-gray-200" />
            <span className="px-3 text-xs text-gray-500 uppercase">
              o continúa con
            </span>
            <div className="flex-1 h-px bg-gray-200" />
          </div>

          <Button
            type="button"
            variant="outline"
            className="w-full flex items-center justify-center gap-2"
            onClick={handleGithubLogin}
          >
            <Github className="h-4 w-4" />
            GitHub
          </Button>

          <Button
            type="button"
            variant="outline"
            className="w-full flex items-center justify-center gap-2 mt-3"
            onClick={handleGoogleLogin}
          >
            <img src={GoogleImage} alt="Google" className="h-4 w-4" />
            Google
          </Button>

          <div className="text-center text-sm text-gray-600 mt-4">
            ¿No tienes una cuenta?{" "}
            <Link
              to={ROUTE_PATHS.register}
              className="font-medium text-blue-600 hover:text-blue-500"
            >
              Registrarse
            </Link>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
