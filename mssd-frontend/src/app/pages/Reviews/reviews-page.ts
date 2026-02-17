import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ReviewService } from '../../services/review.service';
import { FormationService, Formation } from '../../services/formation.service';
import { Review } from '../../model/review.model';
import { FormsModule } from '@angular/forms';
import { CommonModule, DatePipe, NgFor, NgIf } from '@angular/common';
import { PaginationComponent } from '../../shared/pagination/pagination.component';

@Component({
  selector: 'app-reviews-page',
  standalone: true,
  templateUrl: './reviews-page.html',
  styleUrls: ['./reviews-page.scss'],
  imports: [CommonModule, NgIf, NgFor, DatePipe, FormsModule, PaginationComponent, RouterModule],
})
export class ReviewsPage implements OnInit {
  formationId!: number;
  formation?: Formation;
  reviews: Review[] = [];
  paginatedReviews: Review[] = [];
  loading = true;
  loadingFormation = true;

  authorName = '';
  comment = '';
  rating: number = 5;
  hoveredRating = 0;

  submitSuccess = false;
  submitError = false;
  submitting = false;

  // Pagination
  currentPage = 1;
  itemsPerPage = 6; // 6 reviews per page
  totalItems = 0;

  // Stats
  averageRating = 0;
  ratingDistribution: Record<number, number> = { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 };

  // Expose Math for template
  Math = Math;

  constructor(
    private route: ActivatedRoute,
    private reviewService: ReviewService,
    private formationService: FormationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.formationId = +id;
        this.loadFormation();
        this.getReviews();
      }
    });
  }

  loadFormation(): void {
    this.loadingFormation = true;
    this.formationService.getFormationById(this.formationId).subscribe({
      next: (data) => {
        this.formation = data;
        this.loadingFormation = false;
      },
      error: (err) => {
        console.error('Error loading formation:', err);
        this.loadingFormation = false;
      }
    });
  }

  getReviews(): void {
    this.loading = true;
    this.reviewService.getReviewsByFormation(this.formationId).subscribe({
      next: (data) => {
        this.reviews = data;
        this.totalItems = data.length;
        this.calculateStats();
        this.updatePaginatedReviews();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching reviews:', err);
        this.loading = false;
      }
    });
  }

  calculateStats(): void {
    if (this.reviews.length === 0) {
      this.averageRating = 0;
      return;
    }

    const sum = this.reviews.reduce((acc, review) => acc + review.rating, 0);
    this.averageRating = sum / this.reviews.length;

    this.ratingDistribution = { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 };
    this.reviews.forEach(review => {
      this.ratingDistribution[review.rating as 1|2|3|4|5]++;
    });
  }

  getRatingPercentage(stars: number): number {
    if (this.reviews.length === 0) return 0;
    return (this.ratingDistribution[stars as 1|2|3|4|5] / this.reviews.length) * 100;
  }

  submitReview(): void {
    if (!this.authorName || !this.comment || this.rating < 1 || this.rating > 5) {
      return;
    }

    this.submitting = true;
    const newReview: Review = {
      authorName: this.authorName,
      comment: this.comment,
      rating: this.rating,
      formationId: this.formationId
    };

    this.reviewService.addReview(newReview).subscribe({
      next: (res) => {
        this.reviews.unshift(res);
        this.totalItems = this.reviews.length;
        this.calculateStats();
        this.updatePaginatedReviews();
        this.authorName = '';
        this.comment = '';
        this.rating = 5;
        this.submitSuccess = true;
        this.submitError = false;
        this.submitting = false;
        setTimeout(() => this.submitSuccess = false, 5000);
      },
      error: (err) => {
        console.error('Error submitting review:', err);
        this.submitSuccess = false;
        this.submitError = true;
        this.submitting = false;
        setTimeout(() => this.submitError = false, 5000);
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
        this.calculateStats();
        this.updatePaginatedReviews();
      },
      error: (err) => {
        console.error('Failed to delete review:', err);
      }
    });
  }

  setRating(stars: number): void {
    this.rating = stars;
  }

  setHoveredRating(stars: number): void {
    this.hoveredRating = stars;
  }

  clearHoveredRating(): void {
    this.hoveredRating = 0;
  }

  getStarArray(count: number): number[] {
    return Array(count).fill(0);
  }

  goBack(): void {
    this.router.navigate(['/services']);
  }
}
