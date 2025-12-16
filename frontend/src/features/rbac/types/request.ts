export interface CreateRoleCommand {
  name: string;
  description: string;
  permissionIds: number[];
  viewCodes: string[];
}

export interface UpdateRoleCommand {
  id: number;
  name: string;
  description: string;
  permissionIds: number[];
  viewCodes: string[];
}

export interface CreatePermissionCommand {
  resource: "STUDENT" | "ENROLLMENT" | "UI_VIEW";
  operation: "READ" | "CREATE" | "UPDATE" | "DELETE";
  scope: "SELF" | "ALL";
  description: string;
}
