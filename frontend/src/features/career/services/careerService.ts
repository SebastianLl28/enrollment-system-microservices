import { apiClient } from "@/config/apiClient";
import { CAREER_ENDPOINT } from "@/config/endpoints";
import type { Career, CreateCareerPayload } from "../types/Career";

export interface getCareerListParams {
  includeInactive?: boolean;
}

export const getCareerList = async (params: getCareerListParams): Promise<Career[]> =>
  await apiClient
    .get<Career[]>(CAREER_ENDPOINT.base, { params })
    .then((res) => res.data);

export const postCareer = async (
  career: CreateCareerPayload
): Promise<Career> =>
  await apiClient
    .post<Career>(CAREER_ENDPOINT.base, career)
    .then((res) => res.data);
