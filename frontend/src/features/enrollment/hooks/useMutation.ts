import {
  ENROLLMENT_POST_MUTATION,
  generateEnrollmentQueryKey,
} from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import type { EnrollmentRequestQuery } from "../types/request";
import { postEnrollment, putEnrollment } from "../services/EnrollmentService";
import { toast } from "sonner";

export const usePostEnrollment = (query: EnrollmentRequestQuery) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: ENROLLMENT_POST_MUTATION,
    mutationFn: postEnrollment,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: generateEnrollmentQueryKey(query),
      });
      toast.success("Inscripción creada con éxito");
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
        queryKey: ["enrollment", "list"],
      });
      toast.success("Inscripción actualizada con éxito");
    },
  });
};
