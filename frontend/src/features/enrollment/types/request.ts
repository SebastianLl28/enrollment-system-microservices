export interface EnrollmentRequest {
  studentId: number;
  careerOfferingId: number;
}

export interface EnrollmentRequestQuery {
  studentId: number | null;
  termId: number | null;
  careerId: number | null;
  page?: number;
  size?: number;
}

export interface EnrollmentUpdateRequest {
  id: number;
  status: "PENDING" | "PAID" | "CANCELLED" | "COMPLETED";
}
