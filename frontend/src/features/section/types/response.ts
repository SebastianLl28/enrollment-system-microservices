import type { TermResponse } from "@/features/term/types/response";
import type { ClassroomResponse } from "@/features/classroom/types/response";

export interface SectionCourse {
  id: number;
  name: string;
  code: string;
  active: boolean;
}

export interface SectionResponse {
  id: number;
  course: SectionCourse;
  term: TermResponse;
  classroom: ClassroomResponse;
  sectionCode: string;
  active: boolean;
  createdAt: Date;
}
