import { apiClient } from "@/config/apiClient";
import { STUDENT_ENDPOINT } from "@/config/endpoints";
import type { CreateStudentPayload, Student, UpdateStudentPayload } from "../types/Student";

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

interface PutStudentParams {
  id: number;
  student: UpdateStudentPayload;
}
export const putStudent = async ({ id, student }: PutStudentParams): Promise<Student> =>
  await apiClient
    .put<Student>(`${STUDENT_ENDPOINT.base}/${id}`, student)
    .then((res) => res.data);
