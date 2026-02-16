import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-reviews',
  standalone: true,
  imports: [CommonModule, HttpClientModule, RouterModule],
  templateUrl: './admin-reviews.html',
  styleUrl: './admin-reviews.scss'
})
export class AdminReviewsComponent implements OnInit {
  reviews: any[] = [];
  error: string | null = null;

  private apiUrl = '/api/reviews';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadReviews();
  }

  loadReviews() {
    this.http.get<any[]>(`${this.apiUrl}/all`)
      .subscribe({
        next: (data) => {
          this.reviews = data;
        },
        error: (err) => {
          this.error = 'Impossible de charger les avis.';
          console.error(err);
        }
      });
  }

  deleteReview(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet avis ?')) {
      this.http.delete(`${this.apiUrl}/${id}`)
        .subscribe(() => {
          this.reviews = this.reviews.filter(r => r.id !== id);
        });
    }
  }
}
