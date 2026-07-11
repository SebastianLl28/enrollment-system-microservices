import { CLASSROOM_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import { getAllClassrooms } from "../services/classroomService";

export const useGetAllClassrooms = () => {
  return useQuery({
    queryKey: CLASSROOM_QUERY,
    queryFn: getAllClassrooms,
  });
};
