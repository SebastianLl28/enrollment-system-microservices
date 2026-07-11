export interface SectionRequest {
  courseId: number;
  termId: number;
  classroomId: number;
  sectionCode: string;
}

export interface UpdateSectionRequest {
  courseId: number;
  termId: number;
  classroomId: number;
  sectionCode: string;
  active: boolean;
}
