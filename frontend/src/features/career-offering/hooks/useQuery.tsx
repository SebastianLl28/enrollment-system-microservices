import { CAREER_OFFERING_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import { getAllCareerOfferings } from "../services/careerOfferingService";

export const useGetAllCareerOfferings = () => {
  return useQuery({
    queryKey: CAREER_OFFERING_QUERY,
    queryFn: getAllCareerOfferings,
  });
};
