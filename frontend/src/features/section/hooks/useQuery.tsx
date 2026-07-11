import { SECTION_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import { getAllSections } from "../services/sectionService";

export const useGetAllSections = () => {
  return useQuery({
    queryKey: SECTION_QUERY,
    queryFn: getAllSections,
  });
};
