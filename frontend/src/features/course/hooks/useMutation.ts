import { COURSE_LIST_QUERY, COURSE_POST_MUTATION, COURSE_PUT_MUTATION } from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postCourse, putCourse } from "../services/courseService";
import type { CreateCoursePayload, UpdateCoursePayload } from "../types/Course";
import { toast } from "sonner";
import { getApiErrorMessage } from "@/lib/apiError";

export const usePostCourse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: COURSE_POST_MUTATION,
    mutationFn: (payload: CreateCoursePayload) => postCourse(payload),
    onSuccess: () => {
      toast.success("Curso creado exitosamente");
      queryClient.invalidateQueries({ queryKey: COURSE_LIST_QUERY });
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo crear el curso"));
    },
  });
};

export const usePutCourse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: COURSE_PUT_MUTATION,
    mutationFn: ({ id, course }: { id: number; course: UpdateCoursePayload }) =>
      putCourse({ id, course }),
    onSuccess: () => {
      toast.success("Curso actualizado exitosamente");
      queryClient.invalidateQueries({ queryKey: COURSE_LIST_QUERY });
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo actualizar el curso"));
    },
  });
};
