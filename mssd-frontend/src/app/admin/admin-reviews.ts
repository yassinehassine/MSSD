import { Component } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-reviews',
  standalone: true,
  imports: [CommonModule, HttpClientModule, RouterModule], // ✅ nécessaires pour *ngFor et | date
  templateUrl: './admin-reviews.html'
})
export class AdminReviewsComponent {
  reviews: any[] = [];
  error: string | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadReviews();
  }

  loadReviews() {
    this.http.get<any[]>('http://localhost:8080/api/reviews/all')
      .subscribe({
        next: (data) => {
          this.reviews = data;
        },
        error: (err) => {
          this.error = 'Failed to load reviews.';
          console.error(err);
        }
      });
  }

  deleteReview(id: number) {
    if (confirm('Are you sure you want to delete this review?')) {
      this.http.delete(`http://localhost:8080/api/reviews/${id}`)
        .subscribe(() => {
          this.reviews = this.reviews.filter(r => r.id !== id);
        });
    }
  }
}
