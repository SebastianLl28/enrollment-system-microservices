import { STUDENT_LIST_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import { getStudentList } from "../services/studentService";

export const useGetStudents = () =>
  useQuery({
    queryKey: STUDENT_LIST_QUERY,
    queryFn: getStudentList,
  });
