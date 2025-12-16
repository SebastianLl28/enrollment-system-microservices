import { apiClient } from "@/config/apiClient";
import { RBAC_ENDPOINT } from "@/config/endpoints";
import type {
  RoleResponse,
  PermissionResponse,
  UIViewResponse,
  UserRbacResponse,
  UserRoleResponse,
} from "../types/response";
import type { CreateRoleCommand, UpdateRoleCommand } from "../types/request";

// Roles
export const getRoles = async () => {
  return apiClient
    .get<RoleResponse[]>(RBAC_ENDPOINT.roles)
    .then((res) => res.data);
};

export const getRoleById = async (id: number) => {
  return apiClient
    .get<RoleResponse>(RBAC_ENDPOINT.roleById(id))
    .then((res) => res.data);
};

export const createRole = async (data: CreateRoleCommand) => {
  return apiClient
    .post<RoleResponse>(RBAC_ENDPOINT.roles, data)
    .then((res) => res.data);
};

export const updateRole = async (id: number, data: UpdateRoleCommand) => {
  return apiClient
    .put<RoleResponse>(RBAC_ENDPOINT.roleById(id), data)
    .then((res) => res.data);
};

export const deleteRole = async (id: number) => {
  return apiClient.delete(RBAC_ENDPOINT.roleById(id));
};

// Permissions
export const getPermissions = async () => {
  return apiClient
    .get<PermissionResponse[]>(RBAC_ENDPOINT.permissions)
    .then((res) => res.data);
};

// Views
export const getViews = async () => {
  return apiClient
    .get<UIViewResponse[]>(RBAC_ENDPOINT.views)
    .then((res) => res.data);
};

// Users
export const getUsers = async () => {
  return apiClient
    .get<UserRbacResponse[]>(RBAC_ENDPOINT.users)
    .then((res) => res.data);
};

// Assign roles to user
export const assignRolesToUser = async (userId: number, roleIds: number[]) => {
  return apiClient
    .put<UserRoleResponse>(`/auth/rbac/users/${userId}/roles`, { roleIds })
    .then((res) => res.data);
};
