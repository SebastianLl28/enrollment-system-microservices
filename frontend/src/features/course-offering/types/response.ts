import type { Course } from "@/features/course/types/Course";
import type { TermResponse } from "@/features/term/types/response";

export interface CourseOfferingResponse {
  id: number;
  course: Course;
  term: TermResponse;
  section: string;
  capacity: number;
  enrolledCount: number;
  active: boolean;
  createdAt: Date;
}
