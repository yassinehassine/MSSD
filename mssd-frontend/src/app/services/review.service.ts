import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review } from '../model/review.model';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'http://localhost:8080/api/reviews';

  constructor(private http: HttpClient) {}

  // Get all reviews for admin (can be filtered later if needed)
  getAllReviews(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl + '/all');
  }

  deleteReview(id: number): Observable<void> {
    return this.http.delete<void>(`/api/reviews/${id}`);
  }

  addReview(review: Review): Observable<Review> {
    return this.http.post<Review>('/api/reviews', review);
  }
  getReviewsByFormation(formationId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`http://localhost:8080/api/reviews?formationId=${formationId}`);
  }



}
