export interface Faculty {
  id: number;
  name: string;
  location: string;
  dean: string;
  active: boolean;
}

export type FacultyFormValues = Pick<
  Faculty,
  "name" | "location" | "dean" | "active"
>;
