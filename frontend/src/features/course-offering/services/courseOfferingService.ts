import { apiClient } from "@/config/apiClient";
import type { CourseOfferingResponse } from "../types/response";
import { COURSE_OFFERING_ENDPOINT } from "@/config/endpoints";
import type {
  CourseOfferingRequest,
  UpdateCourseOfferingRequest,
} from "../types/request";

export const getAllCourseOfferings = async () => {
  return apiClient
    .get<CourseOfferingResponse[]>(COURSE_OFFERING_ENDPOINT.base)
    .then((res) => res.data);
};

export const postCourseOffering = async (
  courseOffering: CourseOfferingRequest
) => {
  return apiClient
    .post<CourseOfferingResponse>(COURSE_OFFERING_ENDPOINT.base, courseOffering)
    .then((res) => res.data);
};

export const putCourseOffering = async ({
  id,
  courseOffering,
}: {
  id: number;
  courseOffering: UpdateCourseOfferingRequest;
}) => {
  return apiClient
    .put<CourseOfferingResponse>(
      `${COURSE_OFFERING_ENDPOINT.base}/${id}`,
      courseOffering
    )
    .then((res) => res.data);
};
