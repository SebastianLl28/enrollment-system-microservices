import type { TermResponse } from "@/features/term/types/response";

export interface CareerOfferingCareer {
  id: number;
  name: string;
  active: boolean;
}

export interface CareerOfferingResponse {
  id: number;
  career: CareerOfferingCareer;
  term: TermResponse;
  capacity: number;
  enrolledCount: number;
  active: boolean;
  createdAt: Date;
  price: number | null;
}
