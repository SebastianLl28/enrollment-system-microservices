import { ENROLLMENT_ENDPOINT } from "@/config/endpoints";
import type { EnrollmentResponse } from "../types/response";
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
}: EnrollmentRequestQuery) => {
  let queryParams = "";
  if (studentId !== undefined && studentId !== null) {
    queryParams += `studentId=${studentId}&`;
  }

  if (termId !== undefined && termId !== null) {
    queryParams += `termId=${termId}&`;
  }

  if (courseId !== undefined && courseId !== null) {
    queryParams += `courseId=${courseId}&`;
  }

  return await apiClient
    .get<EnrollmentResponse[]>(`${ENROLLMENT_ENDPOINT.base}?${queryParams}`)
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
