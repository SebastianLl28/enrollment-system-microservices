import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postTerm } from "../services/termService";
import { TERM_LIST_QUERY, TERM_POST_MUTATION } from "@/config/keys";
import { toast } from "sonner";

export const usePostTerm = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: TERM_POST_MUTATION,
    mutationFn: postTerm,
    onSuccess: () => {
      toast.success("Vigencia creada exitosamente");
      queryClient.invalidateQueries({ queryKey: TERM_LIST_QUERY });
    },
  });
};
