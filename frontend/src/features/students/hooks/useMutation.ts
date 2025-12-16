import {
  STUDENT_LIST_QUERY,
  STUDENT_POST_MUTATION,
} from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postStudent } from "../services/studentService";
import type { CreateStudentPayload } from "../types/Student";
import { toast } from "sonner";

export const usePostStudent = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: STUDENT_POST_MUTATION,
    mutationFn: (payload: CreateStudentPayload) => postStudent(payload),
    onSuccess: () => {
      toast.success("Estudiante creado exitosamente");
      queryClient.invalidateQueries({ queryKey: STUDENT_LIST_QUERY });
    },
  });
};
