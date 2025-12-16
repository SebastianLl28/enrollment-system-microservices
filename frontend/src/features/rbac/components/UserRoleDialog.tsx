import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Checkbox } from "@/components/ui/checkbox";
import { ScrollArea } from "@/components/ui/scroll-area";
import { useState } from "react";
import type { UserRbacResponse, RoleResponse } from "../types/response";
import { CheckCircle2 } from "lucide-react";

interface UserRoleDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  user: UserRbacResponse | null;
  roles: RoleResponse[];
  onAssign: (userId: number, roleIds: number[]) => void;
}

const UserRoleDialog = ({
  open,
  onOpenChange,
  user,
  roles,
  onAssign,
}: UserRoleDialogProps) => {
  // Encontrar los IDs de roles actuales del usuario
  const [selectedRoleIds, setSelectedRoleIds] = useState<number[]>([]);

  // Cuando se abre el dialog, inicializar con los roles actuales
  const handleOpenChange = (isOpen: boolean) => {
    if (isOpen && user) {
      // Por ahora asumimos que necesitas un endpoint para obtener los roles del usuario
      // o puedes agregar roleIds a UserRbacResponse
      setSelectedRoleIds([]);
    }
    onOpenChange(isOpen);
  };

  const toggleRole = (roleId: number) => {
    setSelectedRoleIds((prev) =>
      prev.includes(roleId)
        ? prev.filter((id) => id !== roleId)
        : [...prev, roleId]
    );
  };

  const handleSubmit = () => {
    if (user) {
      onAssign(user.id, selectedRoleIds);
      onOpenChange(false);
    }
  };

  if (!user) return null;

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle>Asignar roles a usuario</DialogTitle>
          <DialogDescription>
            Gestiona los roles de <strong>{user.fullName}</strong> ({user.email}
            )
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4">
          {/* Info del usuario */}
          <div className="rounded-lg border bg-gray-50 p-4">
            <div className="grid grid-cols-2 gap-4 text-sm">
              <div>
                <p className="text-gray-600">Username</p>
                <p className="font-semibold">{user.username}</p>
              </div>
              <div>
                <p className="text-gray-600">Email</p>
                <p className="font-semibold">{user.email}</p>
              </div>
              <div>
                <p className="text-gray-600">2FA</p>
                <Badge variant={user.twoFactorEnabled ? "success" : "outline"}>
                  {user.twoFactorEnabled ? "Activado" : "Desactivado"}
                </Badge>
              </div>
              <div>
                <p className="text-gray-600">Permisos actuales</p>
                <Badge variant="outline">{user.permissions.length}</Badge>
              </div>
            </div>
          </div>

          {/* Lista de roles */}
          <div className="space-y-2">
            <p className="text-sm font-semibold">
              Selecciona los roles ({selectedRoleIds.length} seleccionados)
            </p>
            <ScrollArea className="h-64 rounded border p-4">
              <div className="space-y-3">
                {roles.map((role) => {
                  const isSelected = selectedRoleIds.includes(role.id);
                  return (
                    <div
                      key={role.id}
                      className={`flex items-start gap-3 rounded p-3 transition ${
                        isSelected
                          ? "bg-blue-50 border border-blue-200"
                          : "hover:bg-gray-50"
                      }`}
                    >
                      <Checkbox
                        id={`role-${role.id}`}
                        checked={isSelected}
                        onCheckedChange={() => toggleRole(role.id)}
                      />
                      <label
                        htmlFor={`role-${role.id}`}
                        className="flex-1 cursor-pointer"
                      >
                        <div className="flex items-center gap-2">
                          <span className="font-semibold">{role.name}</span>
                          {isSelected && (
                            <CheckCircle2 className="h-4 w-4 text-blue-600" />
                          )}
                        </div>
                        <p className="text-xs text-gray-600 mt-1">
                          {role.description}
                        </p>
                        <div className="flex gap-2 mt-2">
                          <Badge variant="outline" className="text-xs">
                            {role.permissionIds.length} permisos
                          </Badge>
                          <Badge variant="outline" className="text-xs">
                            {role.viewCodes.length} vistas
                          </Badge>
                        </div>
                      </label>
                    </div>
                  );
                })}
              </div>
            </ScrollArea>
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            Cancelar
          </Button>
          <Button onClick={handleSubmit}>Asignar roles</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default UserRoleDialog;
