import {
  CAREER_LIST_QUERY,
  CAREER_POST_MUTATION,
} from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postCareer } from "../services/careerService";
import type { CreateCareerPayload } from "../types/Career";
import { toast } from "sonner";

export const usePostCareer = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: CAREER_POST_MUTATION,
    mutationFn: (payload: CreateCareerPayload) => postCareer(payload),
    onSuccess: () => {
      toast.success("Carrera creada exitosamente");
      queryClient.invalidateQueries({ queryKey: CAREER_LIST_QUERY });
    },
  });
};
