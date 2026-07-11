import { useAuthStore } from "@/store/authStore";

// Espeja el modelo del backend: RESOURCE:OPERATION:SCOPE.
// El catálogo académico (faculty, career, course, term, career-offering)
// se protege con el recurso UI_VIEW, igual que el PermissionInterceptor
// del enrollment-server.
export type PermissionResource = "STUDENT" | "ENROLLMENT" | "UI_VIEW";
export type PermissionOperation = "READ" | "CREATE" | "UPDATE" | "DELETE";

export const useHasPermission = (
  resource: PermissionResource,
  operation: PermissionOperation
): boolean => {
  const permissions = useAuthStore((state) => state.permissions);
  const prefix = `${resource}:${operation}:`;
  return permissions.some((p) => p.startsWith(prefix));
};

// Cuentas de solo lectura (sin ningún permiso de escritura) no pueden modificar
// su cuenta, p. ej. activar 2FA: son cuentas compartidas de demostración y un
// invitado dejaría fuera al resto. Espeja la regla de
// TwoFactorApplicationService.initiate en el backend.
export const useCanManageOwnAccount = (): boolean => {
  const permissions = useAuthStore((state) => state.permissions);
  return permissions.some(
    (p) => p.includes(":CREATE:") || p.includes(":UPDATE:") || p.includes(":DELETE:")
  );
};
