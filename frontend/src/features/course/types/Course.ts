export interface CourseStudent {
  id: number;
  firstName: string;
  lastName: string;
}

export interface Course {
  id: number;
  careerId: number;
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
  active?: boolean;
}

export type CreateCoursePayload = Omit<CourseFormValues, "careerId" | "active"> & {
  careerId: number;
};

export type UpdateCoursePayload = Omit<CourseFormValues, "careerId" | "active"> & {
  careerId: number;
  active: boolean;
};
