export interface CourseOfferingRequest {
  courseId: number;
  termId: number;
  sectionCode: string;
  capacity: number;
  price: number;
}

export interface UpdateCourseOfferingRequest {
  courseId: number;
  termId: number;
  sectionCode: string;
  capacity: number;
  active: boolean;
  price: number;
}
