import { keepPreviousData, useQuery } from "@tanstack/react-query";
import type { EnrollmentRequestQuery } from "../types/request";
import { generateEnrollmentQueryKey } from "@/config/keys";
import { getEnrollmentsByStudentIdAndTermIdAndCourseId } from "../services/EnrollmentService";

export const useGetEnrollment = (
  query: EnrollmentRequestQuery,
  options?: { enabled?: boolean }
) => {
  return useQuery({
    queryKey: generateEnrollmentQueryKey(query),
    queryFn: () => getEnrollmentsByStudentIdAndTermIdAndCourseId(query),
    placeholderData: keepPreviousData,
    enabled: options?.enabled ?? true,
  });
};
