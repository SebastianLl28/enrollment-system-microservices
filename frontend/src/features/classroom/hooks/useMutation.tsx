import {
  CLASSROOM_POST_MUTATION,
  CLASSROOM_PUT_MUTATION,
  CLASSROOM_QUERY,
} from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postClassroom, putClassroom } from "../services/classroomService";
import { toast } from "sonner";
import { getApiErrorMessage } from "@/lib/apiError";
import type { UpdateClassroomRequest } from "../types/request";

export const usePostClassroom = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: CLASSROOM_POST_MUTATION,
    mutationFn: postClassroom,
    onSuccess: () => {
      toast.success("Aula creada exitosamente");
      queryClient.invalidateQueries({ queryKey: CLASSROOM_QUERY });
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo crear el aula"));
    },
  });
};

export const usePutClassroom = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: CLASSROOM_PUT_MUTATION,
    mutationFn: ({
      id,
      classroom,
    }: {
      id: number;
      classroom: UpdateClassroomRequest;
    }) => putClassroom({ id, classroom }),
    onSuccess: () => {
      toast.success("Aula actualizada exitosamente");
      queryClient.invalidateQueries({ queryKey: CLASSROOM_QUERY });
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo actualizar el aula"));
    },
  });
};
