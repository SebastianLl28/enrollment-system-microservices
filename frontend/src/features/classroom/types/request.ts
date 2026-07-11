export interface ClassroomRequest {
  code: string;
  name?: string;
  capacity: number | null;
  virtual: boolean;
}

export interface UpdateClassroomRequest {
  code: string;
  name?: string;
  capacity: number | null;
  virtual: boolean;
  active: boolean;
}
