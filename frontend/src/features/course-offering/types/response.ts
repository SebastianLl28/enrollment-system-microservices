import type { TermResponse } from "@/features/term/types/response";

export interface CourseOfferingCourse {
  id: number;
  name: string;
  code: string;
  active: boolean;
}

export interface CourseOfferingResponse {
  id: number;
  course: CourseOfferingCourse;
  term: TermResponse;
  section: string;
  capacity: number;
  active: boolean;
  createdAt: Date;
}
