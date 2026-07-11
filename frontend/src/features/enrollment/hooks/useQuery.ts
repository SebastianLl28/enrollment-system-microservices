import { keepPreviousData, useQuery } from "@tanstack/react-query";
import type { EnrollmentRequestQuery } from "../types/request";
import { generateEnrollmentQueryKey } from "@/config/keys";
import { getEnrollmentsByStudentIdAndTermIdAndCareerId } from "../services/EnrollmentService";

export const useGetEnrollment = (
  query: EnrollmentRequestQuery,
  options?: { enabled?: boolean }
) => {
  return useQuery({
    queryKey: generateEnrollmentQueryKey(query),
    queryFn: () => getEnrollmentsByStudentIdAndTermIdAndCareerId(query),
    placeholderData: keepPreviousData,
    enabled: options?.enabled ?? true,
  });
};
