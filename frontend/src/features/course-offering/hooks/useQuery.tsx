import { COURSE_OFFERING_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import { getAllCourseOfferings } from "../services/courseOfferingService";

export const useGetAllCourseOfferings = () => {
  return useQuery({
    queryKey: COURSE_OFFERING_QUERY,
    queryFn: getAllCourseOfferings,
  });
};
