import { apiClient } from "@/config/apiClient";
import { STUDENT_ENDPOINT } from "@/config/endpoints";
import type { CreateStudentPayload, Student } from "../types/Student";

export const getStudentList = async (): Promise<Student[]> =>
  await apiClient
    .get<Student[]>(STUDENT_ENDPOINT.base)
    .then((res) => res.data);

export const postStudent = async (
  payload: CreateStudentPayload
): Promise<Student> =>
  await apiClient
    .post<Student>(STUDENT_ENDPOINT.base, payload)
    .then((res) => res.data);
