import { COURSE_LIST_QUERY, COURSE_POST_MUTATION } from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postCourse } from "../services/courseService";
import type { CreateCoursePayload } from "../types/Course";
import { toast } from "sonner";

export const usePostCourse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: COURSE_POST_MUTATION,
    mutationFn: (payload: CreateCoursePayload) => postCourse(payload),
    onSuccess: () => {
      toast.success("Curso creado exitosamente");
      queryClient.invalidateQueries({ queryKey: COURSE_LIST_QUERY });
    },
  });
};
