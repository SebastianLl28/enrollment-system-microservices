import { CAREER_LIST_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import {getCareerList, type getCareerListParams} from "../services/careerService";


export const useGetCareers = (params: getCareerListParams) =>
  useQuery({
    queryKey: CAREER_LIST_QUERY,
    queryFn: () => getCareerList(params),
  });
