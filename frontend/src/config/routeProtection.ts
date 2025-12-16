import { ROUTE_PATHS } from "@/app/route/path";
import type { RouteProtection } from "@/features/auth/types/RouteProtection";

export const ROUTE_PROTECTION_CONFIG: Record<string, RouteProtection> = {
  [ROUTE_PATHS.dashboard]: {
    requiresAuth: true,
  },

  [ROUTE_PATHS.faculty]: {
    requiresAuth: true,
    requiredViewCode: "FACULTY_LIST",
    requiredPermission: "UI_VIEW:READ",
  },

  [ROUTE_PATHS.career]: {
    requiresAuth: true,
    requiredViewCode: "CAREER_LIST",
    requiredPermission: "UI_VIEW:READ",
  },

  [ROUTE_PATHS.course]: {
    requiresAuth: true,
    requiredViewCode: "COURSE_LIST",
    requiredPermission: "UI_VIEW:READ",
  },

  [ROUTE_PATHS.students]: {
    requiresAuth: true,
    requiredViewCode: "STUDENT_LIST",
    requiredPermission: "STUDENT:READ",
  },

  [ROUTE_PATHS.profile]: {
    requiresAuth: true,
    requiredViewCode: "MY_PROFILE",
  },

  [ROUTE_PATHS.term]: {
    requiresAuth: true,
    requiredViewCode: "TERM_LIST",
    requiredPermission: "UI_VIEW:READ",
  },

  [ROUTE_PATHS.courseOffering]: {
    requiresAuth: true,
    requiredViewCode: "COURSE_OFFERING_LIST",
    requiredPermission: "UI_VIEW:READ",
  },

  [ROUTE_PATHS.enrollment]: {
    requiresAuth: true,
    requiredViewCode: "ENROLLMENT_ADMIN",
    requiredPermission: "ENROLLMENT:READ",
  },

  [ROUTE_PATHS.rbacRoles]: {
    requiresAuth: true,
    requiredViewCode: "RBAC_ROLES",
    requiredPermission: "UI_VIEW:READ",
  },

  [ROUTE_PATHS.rbacUsers]: {
    requiresAuth: true,
    requiredViewCode: "RBAC_USERS",
    requiredPermission: "UI_VIEW:READ",
  },

  [ROUTE_PATHS.rbacPermissions]: {
    requiresAuth: true,
    requiredViewCode: "RBAC_PERMISSIONS",
    requiredPermission: "UI_VIEW:READ",
  },
};
