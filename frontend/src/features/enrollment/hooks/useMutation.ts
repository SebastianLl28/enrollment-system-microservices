import { ENROLLMENT_LIST_QUERY, ENROLLMENT_POST_MUTATION } from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postEnrollment, putEnrollment } from "../services/EnrollmentService";
import { toast } from "sonner";
import { getApiErrorMessage } from "@/lib/apiError";

export const usePostEnrollment = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: ENROLLMENT_POST_MUTATION,
    mutationFn: postEnrollment,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ENROLLMENT_LIST_QUERY,
      });
      toast.success("Inscripción creada con éxito");
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo crear la inscripción"));
    },
  });
};

export const usePutEnrollment = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: ENROLLMENT_POST_MUTATION,
    mutationFn: putEnrollment,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ENROLLMENT_LIST_QUERY,
      });
      toast.success("Inscripción actualizada con éxito");
    },
    onError: (error) => {
      toast.error(
        getApiErrorMessage(error, "No se pudo actualizar la inscripción")
      );
    },
  });
};
