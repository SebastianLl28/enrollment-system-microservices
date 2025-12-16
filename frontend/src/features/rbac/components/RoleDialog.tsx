import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import RoleForm from "./RoleForm";
import type { CreateRoleCommand } from "../types/request";
import type {
  PermissionResponse,
  RoleResponse,
  UIViewResponse,
} from "../types/response";

interface RoleDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  editingRole: RoleResponse | null;
  onSubmit: (values: CreateRoleCommand) => void;
  permissions: PermissionResponse[];
  views: UIViewResponse[];
}

const RoleDialog = ({
  open,
  onOpenChange,
  editingRole,
  onSubmit,
  permissions,
  views,
}: RoleDialogProps) => {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-3xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>{editingRole ? "Editar rol" : "Crear rol"}</DialogTitle>
          <DialogDescription>
            Define el nombre, descripción y asigna permisos y vistas al rol.
          </DialogDescription>
        </DialogHeader>

        <RoleForm
          onSubmit={onSubmit}
          editingRole={editingRole}
          permissions={permissions}
          views={views}
        />

        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            Cancelar
          </Button>
          <Button type="submit" form="role-form">
            {editingRole ? "Guardar cambios" : "Crear"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default RoleDialog;
