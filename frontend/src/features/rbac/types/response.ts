export interface UIViewResponse {
  code: string;
  route: string;
  label: string;
  module: string;
  sortOrder: number;
  active: boolean;
}

export interface PermissionResponse {
  id: number;
  resource: "STUDENT" | "ENROLLMENT" | "UI_VIEW";
  operation: "READ" | "CREATE" | "UPDATE" | "DELETE";
  scope: "SELF" | "ALL";
  description: string;
}

export interface RoleResponse {
  id: number;
  name: string;
  description: string;
  permissionIds: number[];
  viewCodes: string[];
}

export interface UserRbacResponse {
  id: number;
  username: string;
  email: string;
  fullName: string;
  twoFactorEnabled: boolean;
  hasPassword: boolean;
  permissions: string[];
  uiViews: UIViewResponse[];
}

export interface UserRoleResponse {
  userId: number;
  username: string;
  email: string;
  fullName: string;
  roles: RoleSimpleResponse[];
  permissions: string[];
  uiViews: UIViewResponse[];
}

export interface RoleSimpleResponse {
  id: number;
  name: string;
  description: string;
}
