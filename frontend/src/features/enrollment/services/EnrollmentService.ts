import { ENROLLMENT_ENDPOINT } from "@/config/endpoints";
import type { EnrollmentResponse, PageResponse } from "../types/response";
import { apiClient } from "@/config/apiClient";
import type {
  EnrollmentRequest,
  EnrollmentRequestQuery,
  EnrollmentUpdateRequest,
} from "../types/request";

export const getEnrollmentsByStudentIdAndTermIdAndCourseId = async ({
  studentId,
  termId,
  courseId,
  page,
  size,
}: EnrollmentRequestQuery) => {
  const params = new URLSearchParams();
  if (studentId != null) params.append("studentId", String(studentId));
  if (termId != null) params.append("termId", String(termId));
  if (courseId != null) params.append("courseId", String(courseId));
  if (page != null) params.append("page", String(page));
  if (size != null) params.append("size", String(size));

  return await apiClient
    .get<PageResponse<EnrollmentResponse>>(
      `${ENROLLMENT_ENDPOINT.base}?${params.toString()}`
    )
    .then((res) => res.data);
};

export const postEnrollment = async (enrollmentRequest: EnrollmentRequest) => {
  return await apiClient
    .post<EnrollmentResponse>(ENROLLMENT_ENDPOINT.base, enrollmentRequest)
    .then((res) => res.data);
};

export const putEnrollment = async (enrollment: EnrollmentUpdateRequest) => {
  return await apiClient
    .put<EnrollmentResponse>(
      `${ENROLLMENT_ENDPOINT.base}/${enrollment.id}`,
      enrollment
    )
    .then((res) => res.data);
};
