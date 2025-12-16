export interface CareerFaculty {
  id: number;
  name: string;
}

export interface CareerCourse {
  id: number;
  name: string;
  code: string;
  active: boolean;
}

export interface Career {
  id: number;
  faculty: CareerFaculty;
  name: string;
  description?: string;
  semesterLength: number;
  degreeAwarded: string;
  registrationDate: string;
  active: boolean;
  courseList: CareerCourse[];
}

export interface CareerFormValues {
  facultyId: number | null;
  name: string;
  description?: string;
  semesterLength: number;
  degreeAwarded: string;
}

export type CreateCareerPayload = Omit<CareerFormValues, "facultyId"> & {
  facultyId: number;
};
