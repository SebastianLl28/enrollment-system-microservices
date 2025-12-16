import {
  COURSE_OFFERING_POST_MUTATION,
  COURSE_OFFERING_QUERY,
} from "@/config/keys";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postCourseOffering } from "../services/courseOfferingService";

export const usePostCourseOffering = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: COURSE_OFFERING_POST_MUTATION,
    mutationFn: postCourseOffering,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: COURSE_OFFERING_QUERY });
    },
  });
};
