import { apiClient } from "@/config/apiClient";
import type { CareerOfferingResponse } from "../types/response";
import { CAREER_OFFERING_ENDPOINT } from "@/config/endpoints";
import type {
  CareerOfferingRequest,
  UpdateCareerOfferingRequest,
} from "../types/request";

export const getAllCareerOfferings = async () => {
  return apiClient
    .get<CareerOfferingResponse[]>(CAREER_OFFERING_ENDPOINT.base)
    .then((res) => res.data);
};

export const postCareerOffering = async (
  careerOffering: CareerOfferingRequest
) => {
  return apiClient
    .post<CareerOfferingResponse>(CAREER_OFFERING_ENDPOINT.base, careerOffering)
    .then((res) => res.data);
};

export const putCareerOffering = async ({
  id,
  careerOffering,
}: {
  id: number;
  careerOffering: UpdateCareerOfferingRequest;
}) => {
  return apiClient
    .put<CareerOfferingResponse>(
      `${CAREER_OFFERING_ENDPOINT.base}/${id}`,
      careerOffering
    )
    .then((res) => res.data);
};
