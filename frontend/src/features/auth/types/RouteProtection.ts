export interface RouteProtection {
  requiredViewCode?: string;
  requiredPermission?: string;
  requiresAuth: boolean;
}
