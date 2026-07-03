import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postTerm, putTerm } from "../services/termService";
import {
  TERM_LIST_QUERY,
  TERM_POST_MUTATION,
  TERM_PUT_MUTATION,
} from "@/config/keys";
import { toast } from "sonner";
import { getApiErrorMessage } from "@/lib/apiError";
import type { TermRequest } from "../types/request";

export const usePostTerm = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: TERM_POST_MUTATION,
    mutationFn: postTerm,
    onSuccess: () => {
      toast.success("Vigencia creada exitosamente");
      queryClient.invalidateQueries({ queryKey: TERM_LIST_QUERY });
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo crear la vigencia"));
    },
  });
};

export const usePutTerm = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: TERM_PUT_MUTATION,
    mutationFn: ({ id, term }: { id: number; term: TermRequest }) =>
      putTerm({ id, term }),
    onSuccess: () => {
      toast.success("Vigencia actualizada exitosamente");
      queryClient.invalidateQueries({ queryKey: TERM_LIST_QUERY });
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo actualizar la vigencia"));
    },
  });
};
