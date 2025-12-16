import { useQuery } from "@tanstack/react-query";
import {
  RBAC_ROLES_QUERY,
  RBAC_ROLE_QUERY,
  RBAC_PERMISSIONS_QUERY,
  RBAC_VIEWS_QUERY,
  RBAC_USERS_QUERY,
} from "@/config/keys";
import {
  getRoles,
  getRoleById,
  getPermissions,
  getViews,
  getUsers,
} from "../services/rbacService";

export const useGetRoles = () => {
  return useQuery({
    queryKey: RBAC_ROLES_QUERY,
    queryFn: getRoles,
  });
};

export const useGetRoleById = (id: number) => {
  return useQuery({
    queryKey: RBAC_ROLE_QUERY(id),
    queryFn: () => getRoleById(id),
    enabled: !!id,
  });
};

export const useGetPermissions = () => {
  return useQuery({
    queryKey: RBAC_PERMISSIONS_QUERY,
    queryFn: getPermissions,
  });
};

export const useGetViews = () => {
  return useQuery({
    queryKey: RBAC_VIEWS_QUERY,
    queryFn: getViews,
  });
};

export const useGetUsers = () => {
  return useQuery({
    queryKey: RBAC_USERS_QUERY,
    queryFn: getUsers,
  });
};
