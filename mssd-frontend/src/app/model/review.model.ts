// src/app/models/review.model.ts
export interface Review {
  id?: number;
  authorName: string;
  comment: string;
  rating: number;
  createdAt?: string;
  formationId: number;
}
