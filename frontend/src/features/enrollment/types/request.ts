export interface EnrollmentRequest {
  studentId: number;
  courseOfferingId: number;
}

export interface EnrollmentRequestQuery {
  studentId: number | null;
  termId: number | null;
  courseId: number | null;
  page?: number;
  size?: number;
}

export interface EnrollmentUpdateRequest {
  id: number;
  status: "PENDING" | "PAID" | "CANCELLED" | "COMPLETED";
}
