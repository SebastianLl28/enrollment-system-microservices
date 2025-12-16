import { COURSE_LIST_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import { getCourseList } from "../services/courseService";

export const useGetCourses = () =>
  useQuery({
    queryKey: COURSE_LIST_QUERY,
    queryFn: getCourseList,
  });
