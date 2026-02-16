import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule],
  template: `
    <nav aria-label="Page navigation" *ngIf="totalPages > 1" class="d-flex justify-content-center mt-4">
      <ul class="pagination">
        <!-- Previous button -->
        <li class="page-item" [class.disabled]="currentPage === 1">
          <a class="page-link" (click)="goToPage(currentPage - 1)" [class.disabled]="currentPage === 1">
            <i class="fas fa-chevron-left"></i>
            <span class="d-none d-sm-inline ms-1">Précédent</span>
          </a>
        </li>

        <!-- First page -->
        <li class="page-item" [class.active]="currentPage === 1" *ngIf="showFirstPage">
          <a class="page-link" (click)="goToPage(1)">1</a>
        </li>

        <!-- First ellipsis -->
        <li class="page-item disabled" *ngIf="showFirstEllipsis">
          <span class="page-link">...</span>
        </li>

        <!-- Page numbers -->
        <li class="page-item" 
            [class.active]="page === currentPage" 
            *ngFor="let page of visiblePages">
          <a class="page-link" (click)="goToPage(page)">{{ page }}</a>
        </li>

        <!-- Last ellipsis -->
        <li class="page-item disabled" *ngIf="showLastEllipsis">
          <span class="page-link">...</span>
        </li>

        <!-- Last page -->
        <li class="page-item" [class.active]="currentPage === totalPages" *ngIf="showLastPage">
          <a class="page-link" (click)="goToPage(totalPages)">{{ totalPages }}</a>
        </li>

        <!-- Next button -->
        <li class="page-item" [class.disabled]="currentPage === totalPages">
          <a class="page-link" (click)="goToPage(currentPage + 1)" [class.disabled]="currentPage === totalPages">
            <span class="d-none d-sm-inline me-1">Suivant</span>
            <i class="fas fa-chevron-right"></i>
          </a>
        </li>
      </ul>
    </nav>

    <!-- Items info -->
    <div class="d-flex justify-content-center align-items-center mt-2" *ngIf="totalItems > 0">
      <small class="text-muted">
        Affichage {{ startItem }} - {{ endItem }} sur {{ totalItems }} éléments
      </small>
    </div>
  `,
  styles: [`
    .pagination {
      margin-bottom: 0;
    }
    
    .page-link {
      cursor: pointer;
      color: #0d6efd;
      border-color: #dee2e6;
      padding: 0.5rem 0.75rem;
      transition: all 0.2s ease-in-out;
    }
    
    .page-link:hover:not(.disabled) {
      background-color: #e9ecef;
      border-color: #adb5bd;
      color: #0a58ca;
    }
    
    .page-item.active .page-link {
      background-color: #0d6efd;
      border-color: #0d6efd;
      color: white;
    }
    
    .page-item.disabled .page-link {
      color: #6c757d;
      cursor: not-allowed;
      background-color: transparent;
      border-color: #dee2e6;
    }
    
    .page-link.disabled {
      pointer-events: none;
    }
    
    @media (max-width: 576px) {
      .pagination {
        font-size: 0.875rem;
      }
      
      .page-link {
        padding: 0.25rem 0.5rem;
      }
    }
  `]
})
export class PaginationComponent implements OnChanges {
  @Input() currentPage: number = 1;
  @Input() totalItems: number = 0;
  @Input() itemsPerPage: number = 9; // 3x3 grid
  @Input() maxVisiblePages: number = 5;
  
  @Output() pageChange = new EventEmitter<number>();

  totalPages: number = 0;
  visiblePages: number[] = [];
  showFirstPage: boolean = false;
  showLastPage: boolean = false;
  showFirstEllipsis: boolean = false;
  showLastEllipsis: boolean = false;
  startItem: number = 0;
  endItem: number = 0;

  ngOnChanges(changes: SimpleChanges) {
    this.calculatePagination();
  }

  private calculatePagination() {
    this.totalPages = Math.ceil(this.totalItems / this.itemsPerPage);
    
    if (this.totalPages <= 1) {
      this.visiblePages = [];
      return;
    }

    // Calculate start and end item numbers
    this.startItem = (this.currentPage - 1) * this.itemsPerPage + 1;
    this.endItem = Math.min(this.currentPage * this.itemsPerPage, this.totalItems);

    // Calculate visible pages
    let start = Math.max(1, this.currentPage - Math.floor(this.maxVisiblePages / 2));
    let end = Math.min(this.totalPages, start + this.maxVisiblePages - 1);
    
    // Adjust if we're near the end
    if (end - start + 1 < this.maxVisiblePages) {
      start = Math.max(1, end - this.maxVisiblePages + 1);
    }

    this.visiblePages = [];
    for (let i = start; i <= end; i++) {
      this.visiblePages.push(i);
    }

    // Determine what to show
    this.showFirstPage = start > 1;
    this.showLastPage = end < this.totalPages;
    this.showFirstEllipsis = start > 2;
    this.showLastEllipsis = end < this.totalPages - 1;

    // Don't show first page separately if it's in visible pages
    if (this.visiblePages.includes(1)) {
      this.showFirstPage = false;
    }
    
    // Don't show last page separately if it's in visible pages
    if (this.visiblePages.includes(this.totalPages)) {
      this.showLastPage = false;
    }
  }

  goToPage(page: number) {
    if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
      this.pageChange.emit(page);
    }
  }
}