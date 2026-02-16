import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review } from '../model/review.model';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = '/api/reviews';

  constructor(private http: HttpClient) {}

  // Get all reviews for admin (can be filtered later if needed)
  getAllReviews(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl + '/all');
  }

  deleteReview(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  addReview(review: Review): Observable<Review> {
    return this.http.post<Review>(this.apiUrl, review);
  }
  getReviewsByFormation(formationId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}?formationId=${formationId}`);
  }



}
