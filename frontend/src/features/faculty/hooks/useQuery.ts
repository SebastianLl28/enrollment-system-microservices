import { FACULTY_LIST_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import {getFacultyList, type getFacultyListParams} from "../services/facultyService";

export const useGetFaculties = (options: getFacultyListParams) =>
  useQuery({
    queryKey: FACULTY_LIST_QUERY,
    queryFn: () => getFacultyList(options),
  });
