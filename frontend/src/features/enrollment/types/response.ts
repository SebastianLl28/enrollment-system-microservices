import type { CourseOfferingResponse } from "@/features/course-offering/types/response";
import type { StudentResponse } from "@/features/students/types/Student";

export interface EnrollmentResponse {
  id: number;
  student: StudentResponse;
  courseOffering: CourseOfferingResponse;
  enrollmentDate: Date;
  unenrollmentDate?: Date;
  status: "PENDING" | "PAID" | "CANCELLED" | "COMPLETED";
}
