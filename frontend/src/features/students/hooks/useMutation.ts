import {
  STUDENT_LIST_QUERY,
  STUDENT_POST_MUTATION,
  STUDENT_PUT_MUTATION,
} from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postStudent, putStudent } from "../services/studentService";
import type { CreateStudentPayload, UpdateStudentPayload } from "../types/Student";
import { toast } from "sonner";
import { getApiErrorMessage } from "@/lib/apiError";

export const usePostStudent = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: STUDENT_POST_MUTATION,
    mutationFn: (payload: CreateStudentPayload) => postStudent(payload),
    onSuccess: () => {
      toast.success("Estudiante creado exitosamente");
      queryClient.invalidateQueries({ queryKey: STUDENT_LIST_QUERY });
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo crear el estudiante"));
    },
  });
};

export const usePutStudent = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: STUDENT_PUT_MUTATION,
    mutationFn: ({ id, student }: { id: number; student: UpdateStudentPayload }) =>
      putStudent({ id, student }),
    onSuccess: () => {
      toast.success("Estudiante actualizado exitosamente");
      queryClient.invalidateQueries({ queryKey: STUDENT_LIST_QUERY });
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo actualizar el estudiante"));
    },
  });
};
