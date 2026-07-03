import { apiClient } from "@/config/apiClient";
import type { TermResponse } from "../types/response";
import type { TermRequest } from "../types/request";
import { TERM_ENDPOINT } from "@/config/endpoints";

export const getTerms = async () => {
  return apiClient
    .get<TermResponse[]>(TERM_ENDPOINT.base)
    .then((res) => res.data);
};

export const postTerm = async (term: TermRequest) => {
  return apiClient
    .post<TermResponse>(TERM_ENDPOINT.base, term)
    .then((res) => res.data);
};

export const putTerm = async ({
  id,
  term,
}: {
  id: number;
  term: TermRequest;
}) => {
  return apiClient
    .put<TermResponse>(`${TERM_ENDPOINT.base}/${id}`, term)
    .then((res) => res.data);
};
