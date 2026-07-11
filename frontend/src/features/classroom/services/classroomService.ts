import { apiClient } from "@/config/apiClient";
import type { ClassroomResponse } from "../types/response";
import { CLASSROOM_ENDPOINT } from "@/config/endpoints";
import type { ClassroomRequest, UpdateClassroomRequest } from "../types/request";

export const getAllClassrooms = async () => {
  return apiClient
    .get<ClassroomResponse[]>(CLASSROOM_ENDPOINT.base)
    .then((res) => res.data);
};

export const postClassroom = async (classroom: ClassroomRequest) => {
  return apiClient
    .post<ClassroomResponse>(CLASSROOM_ENDPOINT.base, classroom)
    .then((res) => res.data);
};

export const putClassroom = async ({
  id,
  classroom,
}: {
  id: number;
  classroom: UpdateClassroomRequest;
}) => {
  return apiClient
    .put<ClassroomResponse>(`${CLASSROOM_ENDPOINT.base}/${id}`, classroom)
    .then((res) => res.data);
};
