import {
  COURSE_OFFERING_POST_MUTATION,
  COURSE_OFFERING_QUERY,
} from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postCourseOffering } from "../services/courseOfferingService";
import { toast } from "sonner";
import { getApiErrorMessage } from "@/lib/apiError";

export const usePostCourseOffering = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: COURSE_OFFERING_POST_MUTATION,
    mutationFn: postCourseOffering,
    onSuccess: () => {
      toast.success("Curso en vigencia creado exitosamente");
      queryClient.invalidateQueries({ queryKey: COURSE_OFFERING_QUERY });
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo crear el curso en vigencia"));
    },
  });
};
