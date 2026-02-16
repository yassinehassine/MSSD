import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from '../../services/review.service';
import { Review } from '../../model/review.model';
import { FormsModule } from '@angular/forms';
import { CommonModule, DatePipe, NgFor, NgIf } from '@angular/common';
import { PaginationComponent } from '../../shared/pagination/pagination.component';

@Component({
  selector: 'app-reviews-page',
  standalone: true,
  templateUrl: './reviews-page.html',
  styleUrls: ['./reviews-page.css'],
  imports: [CommonModule, NgIf, NgFor, DatePipe, FormsModule, PaginationComponent],
})
export class ReviewsPage implements OnInit {
  formationId!: number;
  reviews: Review[] = [];
  paginatedReviews: Review[] = [];

  authorName = '';
  comment = '';
  rating: number = 5;

  submitSuccess = false;
  submitError = false;

  // Pagination
  currentPage = 1;
  itemsPerPage = 6; // 6 reviews per page
  totalItems = 0;

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
        this.totalItems = data.length;
        this.updatePaginatedReviews();
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
        this.totalItems = this.reviews.length;
        this.updatePaginatedReviews();
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

  updatePaginatedReviews() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedReviews = this.reviews.slice(startIndex, endIndex);
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.updatePaginatedReviews();
  }

  deleteReview(id: number): void {
    this.reviewService.deleteReview(id).subscribe({
      next: () => {
        this.reviews = this.reviews.filter(r => r.id !== id);
        this.totalItems = this.reviews.length;
        this.updatePaginatedReviews();
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
