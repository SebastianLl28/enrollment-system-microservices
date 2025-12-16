import { TERM_LIST_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import { getTerms } from "../services/termService";

export const useGetTerms = () => {
  return useQuery({
    queryKey: TERM_LIST_QUERY,
    queryFn: getTerms,
  });
};
