import { FACULTY_LIST_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import { getFacultyList } from "../services/facultyService";

export const useGetFaculties = () =>
  useQuery({
    queryKey: FACULTY_LIST_QUERY,
    queryFn: getFacultyList,
  });
