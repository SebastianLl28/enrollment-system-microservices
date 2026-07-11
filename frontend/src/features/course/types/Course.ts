export interface CourseCareerAssignment {
  careerId: number;
  careerName: string;
  semesterLevel: number;
}

export interface Course {
  id: number;
  code: string;
  name: string;
  description?: string;
  credits: number;
  active: boolean;
  careers: CourseCareerAssignment[];
}

export interface CareerAssignmentFormValues {
  careerId: number | null;
  semesterLevel: number;
}

export interface CourseFormValues {
  code: string;
  name: string;
  description?: string;
  credits: number;
  careers: CareerAssignmentFormValues[];
  active?: boolean;
}

export interface CareerAssignmentPayload {
  careerId: number;
  semesterLevel: number;
}

export type CreateCoursePayload = Omit<CourseFormValues, "careers" | "active"> & {
  careers: CareerAssignmentPayload[];
};

export type UpdateCoursePayload = Omit<CourseFormValues, "careers" | "active"> & {
  careers: CareerAssignmentPayload[];
  active: boolean;
};
