import {
  CAREER_OFFERING_POST_MUTATION,
  CAREER_OFFERING_PUT_MUTATION,
  CAREER_OFFERING_QUERY,
} from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  postCareerOffering,
  putCareerOffering,
} from "../services/careerOfferingService";
import { toast } from "sonner";
import { getApiErrorMessage } from "@/lib/apiError";
import type { UpdateCareerOfferingRequest } from "../types/request";

export const usePostCareerOffering = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: CAREER_OFFERING_POST_MUTATION,
    mutationFn: postCareerOffering,
    onSuccess: () => {
      toast.success("Carrera en vigencia creada exitosamente");
      queryClient.invalidateQueries({ queryKey: CAREER_OFFERING_QUERY });
    },
    onError: (error) => {
      toast.error(
        getApiErrorMessage(error, "No se pudo crear la carrera en vigencia")
      );
    },
  });
};

export const usePutCareerOffering = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: CAREER_OFFERING_PUT_MUTATION,
    mutationFn: ({
      id,
      careerOffering,
    }: {
      id: number;
      careerOffering: UpdateCareerOfferingRequest;
    }) => putCareerOffering({ id, careerOffering }),
    onSuccess: () => {
      toast.success("Carrera en vigencia actualizada exitosamente");
      queryClient.invalidateQueries({ queryKey: CAREER_OFFERING_QUERY });
    },
    onError: (error) => {
      toast.error(
        getApiErrorMessage(error, "No se pudo actualizar la carrera en vigencia")
      );
    },
  });
};
