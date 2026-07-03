export interface Student {
  id: number;
  name: string;
  lastName: string;
  email: string;
  documentNumber: string;
  phoneNumber?: string;
  birthDate: string;
  address?: string;
  createdAt: string;
  active: boolean;
}

export interface StudentFormValues {
  name: string;
  lastName: string;
  email: string;
  documentNumber: string;
  phoneNumber?: string;
  birthDate: string;
  address?: string;
  active?: boolean;
}

export type CreateStudentPayload = Omit<StudentFormValues, "active">;

export type UpdateStudentPayload = Omit<StudentFormValues, "active"> & {
  active: boolean;
};

export interface StudentResponse {
  id: number;
  firstName: string;
  lastName: string;
}
