import type { EnrollmentRequestQuery } from "@/features/enrollment/types/request";

export const AUTH_LOGIN_MUTATION = ["auth", "login"] as const;
export const AUTH_VALIDATE_QUERY = ["auth", "validate"] as const;

export const FACULTY_LIST_QUERY = ["faculty", "list"] as const;
export const FACULTY_POST_MUTATION = ["faculty", "post"] as const;
export const FACULTY_PUT_MUTATION = ["faculty", "put"] as const;

export const CAREER_LIST_QUERY = ["career", "list"] as const;
export const CAREER_POST_MUTATION = ["career", "post"] as const;

export const COURSE_LIST_QUERY = ["course", "list"] as const;
export const COURSE_POST_MUTATION = ["course", "post"] as const;

export const STUDENT_LIST_QUERY = ["student", "list"] as const;
export const STUDENT_POST_MUTATION = ["student", "post"] as const;

export const PROFILE_QUERY = ["profile", "data"] as const;

export const PROFILE_2FA_INIT_MUTATION = ["profile", "2fa", "init"] as const;

export const TERM_LIST_QUERY = ["term", "list"] as const;
export const TERM_POST_MUTATION = ["term", "post"] as const;

export const COURSE_OFFERING_QUERY = ["course-offering", "list"] as const;
export const COURSE_OFFERING_POST_MUTATION = [
  "course-offering",
  "post",
] as const;

export const generateEnrollmentQueryKey = (query: EnrollmentRequestQuery) =>
  ["enrollment", "list", query] as const;

export const ENROLLMENT_POST_MUTATION = ["enrollment", "post"] as const;

export const RBAC_ROLES_QUERY = ["rbac", "roles"] as const;
export const RBAC_ROLE_QUERY = (id: number) => ["rbac", "role", id] as const;
export const RBAC_PERMISSIONS_QUERY = ["rbac", "permissions"] as const;
export const RBAC_VIEWS_QUERY = ["rbac", "views"] as const;
export const RBAC_USERS_QUERY = ["rbac", "users"] as const;

export const RBAC_ROLE_CREATE_MUTATION = ["rbac", "role", "create"] as const;
export const RBAC_ROLE_UPDATE_MUTATION = ["rbac", "role", "update"] as const;
export const RBAC_ROLE_DELETE_MUTATION = ["rbac", "role", "delete"] as const;
export const RBAC_ASSIGN_ROLES_MUTATION = ["rbac", "assign", "roles"] as const;
