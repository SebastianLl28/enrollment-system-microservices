// features/auth/pages/TwoFactorVerifyPage.tsx
import { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useForm } from "react-hook-form";
import { useVerifyTwoFactorMutation } from "../hooks/mutation";
import { ROUTE_PATHS } from "@/app/route/path";
import { LOCAL_STORAGE_TEMP_TOKEN_KEY } from "@/config/constants";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSlot,
} from "@/components/ui/input-otp";
import { toast } from "sonner";
import { Shield, ArrowLeft } from "lucide-react";

interface TwoFactorFormValues {
  code: string;
}

const TwoFactorVerifyPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const verifyMutation = useVerifyTwoFactorMutation();
  const [tempToken, setTempToken] = useState<string | null>(null);

  const username = location.state?.username || "";

  const { setValue, watch, handleSubmit } = useForm<TwoFactorFormValues>({
    defaultValues: {
      code: "",
    },
  });

  // eslint-disable-next-line
  const code = watch("code");

  useEffect(() => {
    // Verificar que exista el token temporal
    const token = localStorage.getItem(LOCAL_STORAGE_TEMP_TOKEN_KEY);
    if (!token) {
      toast.error("Sesión inválida. Por favor, inicia sesión nuevamente.");
      navigate(ROUTE_PATHS.login, { replace: true });
      return;
    }
    setTempToken(token);
  }, [navigate]);

  const onSubmit = async (data: TwoFactorFormValues) => {
    if (!tempToken) {
      toast.error("Token temporal no encontrado");
      return;
    }

    try {
      await verifyMutation.mutateAsync({
        tempToken,
        code: data.code,
      });

      toast.success("Autenticación exitosa");
      navigate(ROUTE_PATHS.dashboard, { replace: true });
    } catch (error) {
      console.log(error);
      const errorMessage = "Código inválido. Por favor, intenta nuevamente.";
      toast.error(errorMessage);
      setValue("code", ""); // Limpiar el código
    }
  };

  const handleBackToLogin = () => {
    localStorage.removeItem(LOCAL_STORAGE_TEMP_TOKEN_KEY);
    navigate(ROUTE_PATHS.login);
  };

  if (!tempToken) {
    return null; // O un loader mientras redirige
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-100 p-4">
      <Card className="w-full max-w-md">
        <CardHeader className="space-y-1">
          <div className="flex items-center justify-center">
            <div className="flex h-12 w-12 items-center justify-center rounded-full bg-blue-100">
              <Shield className="h-6 w-6 text-blue-600" />
            </div>
          </div>
          <CardTitle className="text-center text-2xl font-bold">
            Verificación en dos pasos
          </CardTitle>
          <CardDescription className="text-center">
            {username && (
              <>
                Hola <span className="font-medium">{username}</span>,{" "}
              </>
            )}
            ingresa el código de 6 dígitos de tu aplicación de autenticación
          </CardDescription>
        </CardHeader>

        <form onSubmit={handleSubmit(onSubmit)}>
          <CardContent className="space-y-6">
            <div className="space-y-4">
              <Label htmlFor="code" className="text-center block">
                Código de autenticación
              </Label>
              <div className="flex justify-center">
                <InputOTP
                  maxLength={6}
                  value={code}
                  onChange={(value) => setValue("code", value)}
                  disabled={verifyMutation.isPending}
                >
                  <InputOTPGroup>
                    <InputOTPSlot index={0} />
                    <InputOTPSlot index={1} />
                    <InputOTPSlot index={2} />
                    <InputOTPSlot index={3} />
                    <InputOTPSlot index={4} />
                    <InputOTPSlot index={5} />
                  </InputOTPGroup>
                </InputOTP>
              </div>
              <p className="text-center text-xs text-gray-500">
                Abre tu aplicación de autenticación (Google Authenticator,
                Authy, etc.)
              </p>
            </div>

            <div className="space-y-3">
              <Button
                type="submit"
                className="w-full"
                disabled={verifyMutation.isPending || code.length !== 6}
              >
                {verifyMutation.isPending ? (
                  <>
                    <div className="mr-2 h-4 w-4 animate-spin rounded-full border-2 border-white border-t-transparent" />
                    Verificando...
                  </>
                ) : (
                  "Verificar código"
                )}
              </Button>

              <Button
                type="button"
                variant="ghost"
                className="w-full"
                onClick={handleBackToLogin}
                disabled={verifyMutation.isPending}
              >
                <ArrowLeft className="mr-2 h-4 w-4" />
                Volver al inicio de sesión
              </Button>
            </div>
          </CardContent>
        </form>
      </Card>
    </div>
  );
};

export default TwoFactorVerifyPage;
