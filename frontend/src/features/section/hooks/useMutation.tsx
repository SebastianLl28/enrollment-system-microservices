import {
  SECTION_POST_MUTATION,
  SECTION_PUT_MUTATION,
  SECTION_QUERY,
} from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postSection, putSection } from "../services/sectionService";
import { toast } from "sonner";
import { getApiErrorMessage } from "@/lib/apiError";
import type { UpdateSectionRequest } from "../types/request";

export const usePostSection = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: SECTION_POST_MUTATION,
    mutationFn: postSection,
    onSuccess: () => {
      toast.success("Sección creada exitosamente");
      queryClient.invalidateQueries({ queryKey: SECTION_QUERY });
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo crear la sección"));
    },
  });
};

export const usePutSection = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: SECTION_PUT_MUTATION,
    mutationFn: ({
      id,
      section,
    }: {
      id: number;
      section: UpdateSectionRequest;
    }) => putSection({ id, section }),
    onSuccess: () => {
      toast.success("Sección actualizada exitosamente");
      queryClient.invalidateQueries({ queryKey: SECTION_QUERY });
    },
    onError: (error) => {
      toast.error(
        getApiErrorMessage(error, "No se pudo actualizar la sección")
      );
    },
  });
};
