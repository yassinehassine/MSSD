import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from '../../services/review.service';
import { Review } from '../../model/review.model';
import { FormsModule } from '@angular/forms';
import { CommonModule, DatePipe, NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-reviews-page',
  standalone: true,
  templateUrl: './reviews-page.html',
  styleUrls: ['./reviews-page.css'],
  imports: [CommonModule, NgIf, NgFor, DatePipe, FormsModule],
})
export class ReviewsPage implements OnInit {
  formationId!: number;
  reviews: Review[] = [];

  authorName = '';
  comment = '';
  rating: number = 5;

  submitSuccess = false;
  submitError = false;

  constructor(
    private route: ActivatedRoute,
    private reviewService: ReviewService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.formationId = +id;
        this.getReviews(); // ← cette ligne doit être bien présente
      }
    });
  }

  getReviews(): void {
    this.reviewService.getReviewsByFormation(this.formationId).subscribe({
      next: (data) => {
        this.reviews = data;
        console.log('Fetched reviews:', this.reviews); // ← Tu dois voir ça dans la console Angular
      },
      error: (err) => {
        console.error('Error fetching reviews:', err);
      }
    });
  }

  submitReview(): void {
    const newReview: Review = {
      authorName: this.authorName,
      comment: this.comment,
      rating: this.rating,
      formationId: this.formationId
    };

    this.reviewService.addReview(newReview).subscribe({
      next: (res) => {
        this.reviews.push(res);
        this.authorName = '';
        this.comment = '';
        this.rating = 5;
        this.submitSuccess = true;
        this.submitError = false;
      },
      error: (err) => {
        console.error('Error submitting review:', err);
        this.submitSuccess = false;
        this.submitError = true;
      }
    });
  }

  deleteReview(id: number): void {
    this.reviewService.deleteReview(id).subscribe({
      next: () => {
        this.reviews = this.reviews.filter(r => r.id !== id);
      },
      error: (err) => {
        console.error('Failed to delete review:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/services']);
  }
}
