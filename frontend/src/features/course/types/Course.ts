export interface CourseStudent {
  id: number;
  firstName: string;
  lastName: string;
}

export interface Course {
  id: number;
  code: string;
  name: string;
  description?: string;
  credits: number;
  semesterLevel: number;
  active: boolean;
  enrolledStudentList: CourseStudent[];
}

export interface CourseFormValues {
  careerId: number | null;
  code: string;
  name: string;
  description?: string;
  credits: number;
  semesterLevel: number;
}

export type CreateCoursePayload = Omit<CourseFormValues, "careerId"> & {
  careerId: number;
};
