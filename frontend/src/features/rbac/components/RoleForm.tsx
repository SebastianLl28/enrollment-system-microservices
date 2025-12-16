import { useForm } from "react-hook-form";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Checkbox } from "@/components/ui/checkbox";
import type { CreateRoleCommand } from "../types/request";
import type {
  RoleResponse,
  PermissionResponse,
  UIViewResponse,
} from "../types/response";

interface RoleFormProps {
  editingRole: RoleResponse | null;
  onSubmit: (values: CreateRoleCommand) => void;
  permissions: PermissionResponse[];
  views: UIViewResponse[];
}

const RoleForm = ({
  onSubmit,
  editingRole,
  permissions,
  views,
}: RoleFormProps) => {
  const { register, handleSubmit, watch, setValue } =
    useForm<CreateRoleCommand>({
      defaultValues: editingRole
        ? {
            name: editingRole.name,
            description: editingRole.description,
            permissionIds: editingRole.permissionIds,
            viewCodes: editingRole.viewCodes,
          }
        : {
            name: "",
            description: "",
            permissionIds: [],
            viewCodes: [],
          },
    });

  const selectedPermissions = watch("permissionIds") || [];
  const selectedViews = watch("viewCodes") || [];

  const togglePermission = (permId: number) => {
    const current = selectedPermissions;
    const newValue = current.includes(permId)
      ? current.filter((id) => id !== permId)
      : [...current, permId];
    setValue("permissionIds", newValue);
  };

  const toggleView = (code: string) => {
    const current = selectedViews;
    const newValue = current.includes(code)
      ? current.filter((c) => c !== code)
      : [...current, code];
    setValue("viewCodes", newValue);
  };

  return (
    <form
      className="space-y-6"
      onSubmit={handleSubmit(onSubmit)}
      id="role-form"
    >
      <div className="space-y-2">
        <Label htmlFor="name">Nombre del rol *</Label>
        <Input
          id="name"
          {...register("name", { required: true })}
          placeholder="ADMIN, STAFF, TEACHER..."
        />
      </div>

      <div className="space-y-2">
        <Label htmlFor="description">Descripción</Label>
        <Textarea
          id="description"
          {...register("description")}
          placeholder="Descripción del rol..."
          rows={3}
        />
      </div>

      <div className="space-y-3">
        <Label className="text-base font-semibold">
          Permisos ({selectedPermissions.length} seleccionados)
        </Label>
        <div className="max-h-64 overflow-y-auto rounded border p-4 space-y-2">
          {permissions.map((perm) => (
            <div key={perm.id} className="flex items-start gap-3">
              <Checkbox
                id={`perm-${perm.id}`}
                checked={selectedPermissions.includes(perm.id)}
                onCheckedChange={() => togglePermission(perm.id)}
              />
              <label
                htmlFor={`perm-${perm.id}`}
                className="text-sm cursor-pointer flex-1"
              >
                <span className="font-mono font-semibold text-blue-600">
                  {perm.resource}:{perm.operation}:{perm.scope}
                </span>
                <p className="text-gray-600 text-xs mt-0.5">
                  {perm.description}
                </p>
              </label>
            </div>
          ))}
        </div>
      </div>

      <div className="space-y-3">
        <Label className="text-base font-semibold">
          Vistas UI ({selectedViews.length} seleccionadas)
        </Label>
        <div className="max-h-64 overflow-y-auto rounded border p-4 space-y-2">
          {views.map((view) => (
            <div key={view.code} className="flex items-start gap-3">
              <Checkbox
                id={`view-${view.code}`}
                checked={selectedViews.includes(view.code)}
                onCheckedChange={() => toggleView(view.code)}
              />
              <label
                htmlFor={`view-${view.code}`}
                className="text-sm cursor-pointer flex-1"
              >
                <span className="font-semibold">{view.label}</span>
                <p className="text-gray-600 text-xs">
                  {view.code} → {view.route} ({view.module})
                </p>
              </label>
            </div>
          ))}
        </div>
      </div>
    </form>
  );
};

export default RoleForm;
