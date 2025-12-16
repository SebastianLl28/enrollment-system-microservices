// features/profile/pages/ProfilePage.tsx
import { ROUTE_PATHS } from "@/app/route/path";
import Breadcrumbs from "@/components/common/Breadcrumbs";
import { useGetProfile } from "../hooks/useQuery";
import { useInitTwoFactor, useConfirmTwoFactor } from "../hooks/useMutation";
import { User, Shield, ChevronRight, Lock } from "lucide-react";
import { useState } from "react";
import TwoFactorSetupModal from "../components/TwoFactorSetupModal";
import { toast } from "sonner";

const ProfilePage = () => {
  const { data: profileData, isError, isLoading } = useGetProfile();
  const initTwoFactorMutation = useInitTwoFactor();
  const confirmTwoFactorMutation = useConfirmTwoFactor();

  const [showTwoFactorModal, setShowTwoFactorModal] = useState(false);
  const [twoFactorData, setTwoFactorData] = useState<{
    secret: string;
    otpauthUrl: string;
  } | null>(null);

  const handleEditProfile = () => {
    // TODO: Implementar edición de perfil
    console.log("Editar perfil");
  };

  const handleTwoFactorClick = async () => {
    // Verificar si el usuario tiene contraseña
    if (!profileData?.hasPassword) {
      toast.info(
        "No puedes activar 2FA porque estás autenticado con GitHub, que ya incluye su propia autenticación de dos factores."
      );
      return;
    }

    if (profileData?.twoFactorEnabled) {
      // TODO: Implementar desactivación de 2FA
      toast.info("La desactivación de 2FA estará disponible pronto.");
      return;
    }

    // Iniciar configuración de 2FA
    try {
      const data = await initTwoFactorMutation.mutateAsync();
      setTwoFactorData(data);
      setShowTwoFactorModal(true);
    } catch (error) {
      toast.error("No se pudo inicializar la autenticación de dos factores.");
      console.error("Error al inicializar 2FA:", error);
    }
  };

  const handleConfirmTwoFactor = async (code: string) => {
    try {
      await confirmTwoFactorMutation.mutateAsync(code);
      setShowTwoFactorModal(false);
      setTwoFactorData(null);
      toast.success("Autenticación de dos factores activada con éxito");
    } catch (error) {
      // El error se maneja en el modal para que el usuario pueda reintentar
      console.error("Error al confirmar 2FA:", error);
    }
  };

  const handleCloseModal = () => {
    setShowTwoFactorModal(false);
    setTwoFactorData(null);
    confirmTwoFactorMutation.reset(); // Limpiar el estado de error
  };

  // Determinar el estado del 2FA
  const getTwoFactorStatus = () => {
    if (!profileData?.hasPassword) {
      return {
        badge: "GitHub",
        badgeColor: "bg-purple-100 text-purple-700",
        description:
          "Tu cuenta usa autenticación de GitHub, que incluye su propia protección de seguridad",
        disabled: true,
      };
    }

    if (profileData?.twoFactorEnabled) {
      return {
        badge: "Activo",
        badgeColor: "bg-green-100 text-green-700",
        description: "Gestionar la autenticación de dos factores",
        disabled: false,
      };
    }

    return {
      badge: "Inactivo",
      badgeColor: "bg-gray-100 text-gray-600",
      description:
        "Activar la autenticación de dos factores para mayor seguridad",
      disabled: false,
    };
  };

  const twoFactorStatus = getTwoFactorStatus();

  const menuItems = [
    {
      id: "edit-profile",
      title: "Editar Perfil",
      description: "Actualiza tu información personal y preferencias",
      icon: User,
      onClick: handleEditProfile,
      disabled: false,
    },
    {
      id: "two-factor",
      title: "Autenticación de dos factores",
      description: twoFactorStatus.description,
      icon: Shield,
      badge: twoFactorStatus.badge,
      badgeColor: twoFactorStatus.badgeColor,
      onClick: handleTwoFactorClick,
      isLoading: initTwoFactorMutation.isPending,
      disabled: twoFactorStatus.disabled,
    },
  ];

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="mx-auto max-w-5xl space-y-6">
        <Breadcrumbs
          items={[
            { label: "Inicio", href: ROUTE_PATHS.dashboard },
            { label: "Perfil" },
          ]}
        />

        {/* Información del usuario */}
        <div className="rounded-lg bg-white p-6 shadow">
          <h2 className="text-lg font-semibold">Información del perfil</h2>
          <p className="text-sm text-gray-500">
            Aquí puedes ver y editar la información de tu perfil.
          </p>

          {isError && (
            <p className="mt-4 text-red-600">
              Error al cargar la información del perfil.
            </p>
          )}

          {isLoading && (
            <div className="mt-4 flex items-center justify-center py-8">
              <div className="h-8 w-8 animate-spin rounded-full border-4 border-gray-200 border-t-blue-600"></div>
            </div>
          )}

          {profileData && !isLoading && (
            <div className="mt-6 space-y-3">
              <div className="flex items-center gap-3">
                <div className="flex h-12 w-12 items-center justify-center rounded-full bg-blue-100 text-blue-600">
                  <User className="h-6 w-6" />
                </div>
                <div>
                  <p className="font-medium text-gray-900">
                    {profileData.fullName}
                  </p>
                  <p className="text-sm text-gray-500">
                    @{profileData.username}
                  </p>
                </div>
              </div>
              <div className="space-y-1 pt-2">
                <p className="text-sm text-gray-600">
                  <span className="font-medium">Email:</span>{" "}
                  {profileData.email}
                </p>
                <p className="flex items-center gap-2 text-sm text-gray-600">
                  <span className="font-medium">Método de acceso:</span>
                  {profileData.hasPassword ? (
                    <span className="flex items-center gap-1">
                      <Lock className="h-3.5 w-3.5" />
                      Usuario y contraseña
                    </span>
                  ) : (
                    <span className="flex items-center gap-1">
                      <svg
                        className="h-3.5 w-3.5"
                        viewBox="0 0 24 24"
                        fill="currentColor"
                      >
                        <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z" />
                      </svg>
                      GitHub
                    </span>
                  )}
                </p>
              </div>
            </div>
          )}
        </div>

        {/* Menú de configuración */}
        <div className="rounded-lg bg-white shadow">
          <div className="border-b border-gray-200 p-6">
            <h3 className="text-lg font-semibold">Configuración</h3>
            <p className="text-sm text-gray-500">
              Gestiona tu cuenta y preferencias de seguridad
            </p>
          </div>

          <div className="divide-y divide-gray-200">
            {menuItems.map((item) => {
              const Icon = item.icon;
              const isDisabled = isLoading || item.isLoading || item.disabled;

              return (
                <button
                  key={item.id}
                  onClick={item.onClick}
                  className="flex w-full items-center justify-between p-6 text-left transition-colors hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-60 disabled:hover:bg-white"
                  disabled={isDisabled}
                >
                  <div className="flex items-start gap-4">
                    <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-blue-50 text-blue-600">
                      {item.isLoading ? (
                        <div className="h-5 w-5 animate-spin rounded-full border-2 border-blue-200 border-t-blue-600" />
                      ) : (
                        <Icon className="h-5 w-5" />
                      )}
                    </div>
                    <div className="flex-1">
                      <h4 className="font-medium text-gray-900">
                        {item.title}
                      </h4>
                      <p className="mt-1 text-sm text-gray-500">
                        {item.description}
                      </p>
                      {item.badge && (
                        <span
                          className={`mt-2 inline-block rounded-full px-2.5 py-0.5 text-xs font-medium ${item.badgeColor}`}
                        >
                          {item.badge}
                        </span>
                      )}
                    </div>
                  </div>
                  <ChevronRight
                    className={`h-5 w-5 shrink-0 ${
                      item.disabled ? "text-gray-300" : "text-gray-400"
                    }`}
                  />
                </button>
              );
            })}
          </div>
        </div>

        {/* Nota informativa para usuarios de GitHub */}
        {profileData && !profileData.hasPassword && (
          <div className="rounded-lg border border-purple-200 bg-purple-50 p-4">
            <div className="flex gap-3">
              <Shield className="h-5 w-5 shrink-0 text-purple-600" />
              <div>
                <h4 className="font-medium text-purple-900">
                  Autenticación con GitHub
                </h4>
                <p className="mt-1 text-sm text-purple-700">
                  Tu cuenta está protegida por GitHub. Si deseas usar la
                  autenticación de dos factores de nuestra aplicación, deberás
                  establecer una contraseña para tu cuenta.
                </p>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Modal de configuración 2FA */}
      {twoFactorData && (
        <TwoFactorSetupModal
          isOpen={showTwoFactorModal}
          onClose={handleCloseModal}
          secret={twoFactorData.secret}
          otpauthUrl={twoFactorData.otpauthUrl}
          onConfirm={handleConfirmTwoFactor}
          isConfirming={confirmTwoFactorMutation.isPending}
          error={
            confirmTwoFactorMutation.isError
              ? "Código inválido. Intenta nuevamente."
              : undefined
          }
        />
      )}
    </div>
  );
};

export default ProfilePage;
