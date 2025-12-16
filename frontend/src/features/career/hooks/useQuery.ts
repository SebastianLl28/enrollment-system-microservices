import { CAREER_LIST_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import { getCareerList } from "../services/careerService";

export const useGetCareers = () =>
  useQuery({
    queryKey: CAREER_LIST_QUERY,
    queryFn: getCareerList,
  });
