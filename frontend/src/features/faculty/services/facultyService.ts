import { apiClient } from "@/config/apiClient";
import type { Faculty } from "../types/Faculty";
import { FACULTY_ENDPOINT } from "@/config/endpoints";

export const getFacultyList = async (): Promise<Faculty[]> =>
  await apiClient.get<Faculty[]>(FACULTY_ENDPOINT.base).then((res) => res.data);

export const postFaculty = async (
  faculty: Omit<Faculty, "id">
): Promise<Faculty> =>
  await apiClient
    .post<Faculty>(FACULTY_ENDPOINT.base, faculty)
    .then((res) => res.data);

interface PutFacultyParams {
  id: number;
  faculty: Omit<Faculty, "id">;
}
export const putFaculty = async ({
  id,
  faculty,
}: PutFacultyParams): Promise<Faculty> =>
  await apiClient
    .put<Faculty>(`${FACULTY_ENDPOINT.base}/${id}`, faculty)
    .then((res) => res.data);
