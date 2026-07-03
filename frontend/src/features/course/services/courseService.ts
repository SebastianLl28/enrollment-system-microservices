import { apiClient } from "@/config/apiClient";
import { COURSE_ENDPOINT } from "@/config/endpoints";
import type { Course, CreateCoursePayload, UpdateCoursePayload } from "../types/Course";

export const getCourseList = async (): Promise<Course[]> =>
  await apiClient.get<Course[]>(COURSE_ENDPOINT.base).then((res) => res.data);

export const postCourse = async (
  payload: CreateCoursePayload
): Promise<Course> =>
  await apiClient
    .post<Course>(COURSE_ENDPOINT.base, payload)
    .then((res) => res.data);

interface PutCourseParams {
  id: number;
  course: UpdateCoursePayload;
}
export const putCourse = async ({ id, course }: PutCourseParams): Promise<Course> =>
  await apiClient
    .put<Course>(`${COURSE_ENDPOINT.base}/${id}`, course)
    .then((res) => res.data);
