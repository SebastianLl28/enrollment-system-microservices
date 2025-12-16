import { apiClient } from "@/config/apiClient";
import { COURSE_ENDPOINT } from "@/config/endpoints";
import type { Course, CreateCoursePayload } from "../types/Course";

export const getCourseList = async (): Promise<Course[]> =>
  await apiClient.get<Course[]>(COURSE_ENDPOINT.base).then((res) => res.data);

export const postCourse = async (
  payload: CreateCoursePayload
): Promise<Course> =>
  await apiClient
    .post<Course>(COURSE_ENDPOINT.base, payload)
    .then((res) => res.data);
