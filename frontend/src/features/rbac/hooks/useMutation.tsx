import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  createRole,
  updateRole,
  deleteRole,
  assignRolesToUser,
} from "../services/rbacService";
import {
  RBAC_ROLE_CREATE_MUTATION,
  RBAC_ROLE_UPDATE_MUTATION,
  RBAC_ROLE_DELETE_MUTATION,
  RBAC_ROLES_QUERY,
  RBAC_ASSIGN_ROLES_MUTATION,
  RBAC_USERS_QUERY,
} from "@/config/keys";
import { toast } from "sonner";
import type { UpdateRoleCommand } from "../types/request";

export const useCreateRole = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: RBAC_ROLE_CREATE_MUTATION,
    mutationFn: createRole,
    onSuccess: () => {
      toast.success("Rol creado exitosamente");
      queryClient.invalidateQueries({ queryKey: RBAC_ROLES_QUERY });
    },
    onError: () => {
      toast.error("Error al crear el rol");
    },
  });
};

export const useUpdateRole = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: RBAC_ROLE_UPDATE_MUTATION,
    mutationFn: ({ id, data }: { id: number; data: UpdateRoleCommand }) =>
      updateRole(id, data),
    onSuccess: () => {
      toast.success("Rol actualizado exitosamente");
      queryClient.invalidateQueries({ queryKey: RBAC_ROLES_QUERY });
    },
    onError: () => {
      toast.error("Error al actualizar el rol");
    },
  });
};

export const useDeleteRole = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: RBAC_ROLE_DELETE_MUTATION,
    mutationFn: deleteRole,
    onSuccess: () => {
      toast.success("Rol eliminado exitosamente");
      queryClient.invalidateQueries({ queryKey: RBAC_ROLES_QUERY });
    },
    onError: () => {
      toast.error("Error al eliminar el rol");
    },
  });
};

export const useAssignRoles = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: RBAC_ASSIGN_ROLES_MUTATION,
    mutationFn: ({ userId, roleIds }: { userId: number; roleIds: number[] }) =>
      assignRolesToUser(userId, roleIds),
    onSuccess: () => {
      toast.success("Roles asignados exitosamente");
      queryClient.invalidateQueries({ queryKey: RBAC_USERS_QUERY });
    },
    onError: () => {
      toast.error("Error al asignar roles");
    },
  });
};
