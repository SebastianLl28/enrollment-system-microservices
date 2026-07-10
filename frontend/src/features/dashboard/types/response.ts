import type { TermResponse } from "@/features/term/types/response";

export interface LabelCountResponse {
  label: string;
  count: number;
}

export interface DashboardStatsResponse {
  totalStudents: number;
  activeStudents: number;
  totalCourses: number;
  activeCourseOfferings: number;
  totalEnrollments: number;
  enrollmentsByStatus: LabelCountResponse[];
  currentTerm: TermResponse | null;
  currentTermEnrollments: number;
  enrollmentsByTerm: LabelCountResponse[];
  topCourses: LabelCountResponse[];
}
