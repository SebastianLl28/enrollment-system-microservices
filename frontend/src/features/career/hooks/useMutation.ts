import {
  CAREER_LIST_QUERY,
  CAREER_POST_MUTATION,
  CAREER_PUT_MUTATION,
} from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postCareer, putCareer } from "../services/careerService";
import type { CreateCareerPayload, UpdateCareerPayload } from "../types/Career";
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

export const usePutCareer = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: CAREER_PUT_MUTATION,
    mutationFn: ({ id, career }: { id: number; career: UpdateCareerPayload }) =>
      putCareer({ id, career }),
    onSuccess: () => {
      toast.success("Carrera actualizada exitosamente");
      queryClient.invalidateQueries({ queryKey: CAREER_LIST_QUERY });
    },
  });
};
