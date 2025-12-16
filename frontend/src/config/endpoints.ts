export const AUTH_ENDPOINT = {
  login: "/auth/login",
  logout: "/auth/logout",
  register: "/auth/register",
  me: "/auth/me",
  validateToken: "/auth/validateToken",
  profile: "/auth/profile",
  twoFactorInit: "/auth/2fa/init",
  twoFactorConfirm: "/auth/2fa/confirm",
  twoFactorVerify: "/auth/2fa/verify",
};

export const FACULTY_ENDPOINT = {
  base: "/api/v1/faculty",
};

export const CAREER_ENDPOINT = {
  base: "/api/v1/career",
};

export const COURSE_ENDPOINT = {
  base: "/api/v1/course",
};

export const STUDENT_ENDPOINT = {
  base: "/api/v1/student",
};

export const TERM_ENDPOINT = {
  base: "/api/v1/term",
};

export const COURSE_OFFERING_ENDPOINT = {
  base: "/api/v1/course-offering",
};

export const ENROLLMENT_ENDPOINT = {
  base: "/api/v1/enrollment",
};

export const RBAC_ENDPOINT = {
  roles: "/auth/rbac/roles",
  roleById: (id: number) => `/auth/rbac/roles/${id}`,
  permissions: "/auth/rbac/permissions",
  permissionById: (id: number) => `/auth/rbac/permissions/${id}`,
  views: "/auth/rbac/views",
  viewByCode: (code: string) => `/auth/rbac/views/${code}`,
  users: "/auth/user",
} as const;
