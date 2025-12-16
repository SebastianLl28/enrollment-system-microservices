import { useQuery } from "@tanstack/react-query";
import type { EnrollmentRequestQuery } from "../types/request";
import { generateEnrollmentQueryKey } from "@/config/keys";
import { getEnrollmentsByStudentIdAndTermIdAndCourseId } from "../services/EnrollmentService";

const hasAnyValue = (...values: unknown[]) => values.some((v) => v != null);

export const useGetEnrollment = (query: EnrollmentRequestQuery) => {
  const hasAnyFilter = hasAnyValue(
    query.studentId,
    query.termId,
    query.courseId
  );
  return useQuery({
    queryKey: generateEnrollmentQueryKey(query),
    queryFn: () => getEnrollmentsByStudentIdAndTermIdAndCourseId(query),
    enabled: hasAnyFilter,
  });
};
