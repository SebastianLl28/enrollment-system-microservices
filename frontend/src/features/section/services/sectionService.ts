import { apiClient } from "@/config/apiClient";
import type { SectionResponse } from "../types/response";
import { SECTION_ENDPOINT } from "@/config/endpoints";
import type { SectionRequest, UpdateSectionRequest } from "../types/request";

export const getAllSections = async () => {
  return apiClient
    .get<SectionResponse[]>(SECTION_ENDPOINT.base)
    .then((res) => res.data);
};

export const postSection = async (section: SectionRequest) => {
  return apiClient
    .post<SectionResponse>(SECTION_ENDPOINT.base, section)
    .then((res) => res.data);
};

export const putSection = async ({
  id,
  section,
}: {
  id: number;
  section: UpdateSectionRequest;
}) => {
  return apiClient
    .put<SectionResponse>(`${SECTION_ENDPOINT.base}/${id}`, section)
    .then((res) => res.data);
};
