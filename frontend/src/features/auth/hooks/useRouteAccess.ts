import { ROUTE_PROTECTION_CONFIG } from "@/config/routeProtection";
import { useAuthStore } from "@/store/authStore";
import { useMemo } from "react";

const getProtectionForPath = (pathname: string) => {
  // match exacto
  if (ROUTE_PROTECTION_CONFIG[pathname]) {
    return ROUTE_PROTECTION_CONFIG[pathname];
  }

  // match por prefijo (ruta más específica primero)
  const keys = Object.keys(ROUTE_PROTECTION_CONFIG).sort(
    (a, b) => b.length - a.length
  );

  return keys.find((k) => pathname.startsWith(k))
    ? ROUTE_PROTECTION_CONFIG[keys.find((k) => pathname.startsWith(k))!]
    : undefined;
};

export const useRouteAccess = (path: string) => {
  const uiViews = useAuthStore((state) => state.uiViews);
  const permissions = useAuthStore((state) => state.permissions);

  const protection = getProtectionForPath(path);

  const hasAccess = useMemo(() => {
    if (!protection) return false;

    if (!protection.requiredViewCode && !protection.requiredPermission) {
      return true;
    }

    if (protection.requiredViewCode) {
      const hasView = uiViews.some(
        (v) => v.code === protection.requiredViewCode && v.active
      );
      if (!hasView) return false;
    }

    if (protection.requiredPermission) {
      const hasPermission = permissions.some((p) =>
        p.startsWith(protection.requiredPermission!)
      );
      if (!hasPermission) return false;
    }

    return true;
  }, [uiViews, permissions, protection]);

  return {
    hasAccess,
    protection,
    isProtected: protection?.requiresAuth ?? false,
  };
};

// export const useRouteAccess = (path: string) => {
//   const uiViews = useAuthStore((state) => state.uiViews);
//   const permissions = useAuthStore((state) => state.permissions);

//   const protection = ROUTE_PROTECTION_CONFIG[path];

//   const hasAccess = useMemo(() => {
//     // Si no hay configuración de protección, denegar acceso por defecto
//     if (!protection) {
//       return false;
//     }

//     // Si no requiere vista ni permiso específico, solo necesita auth
//     if (!protection.requiredViewCode && !protection.requiredPermission) {
//       return true;
//     }

//     // Verificar vista
//     let hasView = true;
//     if (protection.requiredViewCode) {
//       hasView =
//         uiViews?.some(
//           (view) => view.code === protection.requiredViewCode && view.active
//         ) ?? false;
//     }

//     // Si no tiene la vista, denegar acceso inmediatamente
//     if (!hasView) {
//       return false;
//     }

//     // Verificar permiso
//     let hasPermission = true;
//     if (protection.requiredPermission) {
//       hasPermission =
//         permissions?.some((perm) =>
//           perm.startsWith(protection.requiredPermission!)
//         ) ?? false;
//     }

//     return hasPermission;
//   }, [uiViews, permissions, protection]);

//   return {
//     hasAccess,
//     protection,
//     isProtected: protection?.requiresAuth ?? false,
//   };
// };
