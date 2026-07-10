export const ROUTE_PATHS = {
  root: "/",
  login: "/login",
  register: "/register",
  twoFactorVerify: "/2fa-verify",
  paymentResult: "/payments/:status",
  notFound: "*",
  dashboard: "/app",
  statistics: "/app/statistics",
  faculty: "/app/faculty",
  career: "/app/career",
  course: "/app/course",
  students: "/app/students",
  profile: "/app/profile",
  term: "/app/term",
  courseOffering: "/app/course-offering",
  enrollment: "/app/enrollment",
  rbacRoles: "/app/rbac/roles",
  rbacUsers: "/app/rbac/users",
  rbacPermissions: "/app/rbac/permissions",
} as const;

export const AUTH_ROUTE_PATHS = {
  oauth2Login: "oauth2/redirect",
};
