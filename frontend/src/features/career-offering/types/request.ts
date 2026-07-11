export interface CareerOfferingRequest {
  careerId: number;
  termId: number;
  capacity: number;
  price: number;
}

export interface UpdateCareerOfferingRequest {
  careerId: number;
  termId: number;
  capacity: number;
  active: boolean;
  price: number;
}
