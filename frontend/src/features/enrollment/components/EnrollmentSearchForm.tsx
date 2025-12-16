import { useGetCourses } from "@/features/course/hooks/useQuery";
import type { EnrollmentRequestQuery } from "../types/request";
import Select from "react-select";
import { useGetTerms } from "@/features/term/hooks/useQuery";
import { useGetStudents } from "@/features/students/hooks/useQuery";

interface EnrollmentSearchFormProps {
  query: EnrollmentRequestQuery;
  setQuery: (query: EnrollmentRequestQuery) => void;
}

const EnrollmentSearchForm = ({
  query,
  setQuery,
}: EnrollmentSearchFormProps) => {
  const { data: courses } = useGetCourses();
  const { data: terms } = useGetTerms();
  const { data: students } = useGetStudents();

  const courseOptions =
    courses?.map((course) => ({
      value: course.id,
      label: course.name,
    })) ?? [];

  const termOptions =
    terms?.map((term) => ({
      value: term.id,
      label: term.code,
    })) ?? [];

  const studentOptions =
    students?.map((student) => ({
      value: student.id,
      label: `${student.name} ${student.lastName}`,
    })) ?? [];

  return (
    <div>
      <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-4">
        <div>
          <Select
            className="w-full"
            options={studentOptions}
            placeholder="Selecciona un estudiante"
            value={
              studentOptions.find(
                (option) => option.value === query.studentId
              ) ?? null
            }
            onChange={(option) =>
              setQuery({
                ...query,
                studentId: option?.value ?? null,
              })
            }
            isClearable
            classNamePrefix="react-select"
          />
        </div>
        <div>
          <Select
            className="w-full"
            options={termOptions}
            placeholder="Selecciona un periodo"
            value={
              termOptions.find((option) => option.value === query.termId) ??
              null
            }
            onChange={(option) =>
              setQuery({
                ...query,
                termId: option?.value ?? null,
              })
            }
            isClearable
            classNamePrefix="react-select"
          />
        </div>
        <div>
          <Select
            className="w-full"
            options={courseOptions}
            placeholder="Selecciona un curso"
            value={
              courseOptions.find((option) => option.value === query.courseId) ??
              null
            }
            onChange={(option) =>
              setQuery({
                ...query,
                courseId: option?.value ?? null,
              })
            }
            isClearable
            classNamePrefix="react-select"
          />
        </div>
      </div>
    </div>
  );
};

export default EnrollmentSearchForm;
