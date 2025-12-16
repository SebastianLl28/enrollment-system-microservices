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
}

export type CreateStudentPayload = StudentFormValues;

export interface StudentResponse {
  id: number;
  firstName: string;
  lastName: string;
}
