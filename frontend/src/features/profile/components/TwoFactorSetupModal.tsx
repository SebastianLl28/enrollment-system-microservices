// features/profile/components/TwoFactorSetupModal.tsx
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { Copy, Check } from "lucide-react";
import QRCode from "qrcode";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSlot,
} from "@/components/ui/input-otp";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";

interface TwoFactorSetupModalProps {
  isOpen: boolean;
  onClose: () => void;
  secret: string;
  otpauthUrl: string;
  onConfirm: (code: string) => void;
  isConfirming: boolean;
  error?: string;
}

interface FormValues {
  code: string;
}

const TwoFactorSetupModal = ({
  isOpen,
  onClose,
  secret,
  otpauthUrl,
  onConfirm,
  isConfirming,
  error,
}: TwoFactorSetupModalProps) => {
  const [qrCodeUrl, setQrCodeUrl] = useState("");
  const [copied, setCopied] = useState(false);

  const { setValue, watch, handleSubmit } = useForm<FormValues>({
    defaultValues: {
      code: "",
    },
  });

  const code = watch("code");

  useEffect(() => {
    if (otpauthUrl) {
      QRCode.toDataURL(otpauthUrl)
        .then(setQrCodeUrl)
        .catch((err) => console.error("Error generating QR:", err));
    }
  }, [otpauthUrl]);

  const handleCopySecret = async () => {
    await navigator.clipboard.writeText(secret);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const onSubmit = (data: FormValues) => {
    if (data.code.length === 6) {
      onConfirm(data.code);
    }
  };

  const handleOpenChange = (open: boolean) => {
    if (!open && !isConfirming) {
      onClose();
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={handleOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>Configurar autenticación de dos factores</DialogTitle>
          <DialogDescription>
            Escanea el código QR con tu aplicación de autenticación
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-6">
          {/* QR Code */}
          <div className="flex flex-col items-center space-y-4">
            {qrCodeUrl ? (
              <div className="rounded-lg border bg-white p-4">
                <img src={qrCodeUrl} alt="QR Code" className="h-48 w-48" />
              </div>
            ) : (
              <div className="flex h-48 w-48 items-center justify-center rounded-lg border bg-gray-50">
                <div className="h-8 w-8 animate-spin rounded-full border-4 border-gray-200 border-t-blue-600" />
              </div>
            )}
            <p className="text-center text-sm text-gray-600">
              Usa Google Authenticator, Authy o cualquier app compatible
            </p>
          </div>

          {/* Secret manual */}
          <div className="space-y-2">
            <Label className="text-sm font-medium">
              O ingresa este código manualmente:
            </Label>
            <div className="flex items-center gap-2">
              <code className="flex-1 rounded-md bg-gray-100 px-3 py-2 text-sm font-mono">
                {secret}
              </code>
              <Button
                type="button"
                variant="outline"
                size="icon"
                onClick={handleCopySecret}
                title="Copiar código"
              >
                {copied ? (
                  <Check className="h-4 w-4 text-green-600" />
                ) : (
                  <Copy className="h-4 w-4" />
                )}
              </Button>
            </div>
          </div>

          {/* Input OTP */}
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="code">Ingresa el código de 6 dígitos</Label>
              <div className="flex justify-center">
                <InputOTP
                  maxLength={6}
                  value={code}
                  onChange={(value) => setValue("code", value)}
                  disabled={isConfirming}
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
              {error && (
                <p className="text-center text-sm text-red-600">{error}</p>
              )}
            </div>

            <div className="flex gap-3">
              <Button
                type="button"
                variant="outline"
                onClick={onClose}
                className="flex-1"
                disabled={isConfirming}
              >
                Cancelar
              </Button>
              <Button
                type="submit"
                className="flex-1"
                disabled={isConfirming || code.length !== 6}
              >
                {isConfirming ? "Verificando..." : "Activar 2FA"}
              </Button>
            </div>
          </form>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default TwoFactorSetupModal;
