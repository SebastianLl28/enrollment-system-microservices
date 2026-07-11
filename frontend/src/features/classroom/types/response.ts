export interface ClassroomResponse {
  id: number;
  code: string;
  name: string | null;
  capacity: number | null;
  virtual: boolean;
  active: boolean;
  createdAt: Date;
}
