import {
  FACULTY_LIST_QUERY,
  FACULTY_POST_MUTATION,
  FACULTY_PUT_MUTATION,
} from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postFaculty, putFaculty } from "../services/facultyService";
import { toast } from "sonner";

export const usePostFaculty = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: FACULTY_POST_MUTATION,
    mutationFn: postFaculty,
    onSuccess: () => {
      toast.success("Facultad creada exitosamente");
      queryClient.invalidateQueries({ queryKey: FACULTY_LIST_QUERY });
    },
  });
};

export const usePutFaculty = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: FACULTY_PUT_MUTATION,
    mutationFn: putFaculty,
    onSuccess: () => {
      toast.success("Facultad actualizada exitosamente");
      queryClient.invalidateQueries({ queryKey: FACULTY_LIST_QUERY });
    },
  });
};
