import { Component, OnInit } from '@angular/core';
import {CommonModule} from '@angular/common';
import { Portfolio } from '../../model/portfolio.model';
import { PortfolioService } from '../../services/portfolio.service';
import { PaginationComponent } from '../../shared/pagination/pagination.component';
import { TranslationService } from '../../services/translation.service';

@Component({
  selector: 'app-portfolio',
  standalone: true,
  imports: [CommonModule, PaginationComponent],
  templateUrl: './portfolio.html',
  styleUrl: './portfolio.scss'
})
export class PortfolioComponent implements OnInit {
  portfolios: Portfolio[] = [];
  filteredPortfolios: Portfolio[] = [];
  paginatedPortfolios: Portfolio[] = [];
  selectedCategory = 'all';
  categories: string[] = [];
  isLoading = true;
  error: string | null = null;

  // Pagination
  currentPage = 1;
  itemsPerPage = 3; // Show only 3 items per page
  totalItems = 0;

  constructor(
    private portfolioService: PortfolioService,
    public translationService: TranslationService
  ) {}

  t(key: string): string {
    return this.translationService.translate(key);
  }

  ngOnInit(): void {
    console.log('Portfolio page initialized');
    this.loadPortfolios();
  }

  loadPortfolios(): void {
    this.isLoading = true;
    this.error = null;

    console.log('📦 Portfolio Page: Loading portfolios from API...');

    this.portfolioService.getActivePortfolios().subscribe({
      next: (portfolios) => {
        console.log(`✅ Portfolio Page: Received ${portfolios?.length || 0} portfolios`);
        if (portfolios && portfolios.length > 0) {
          console.table(portfolios.map(p => ({
            ID: p.id,
            Company: p.companyName,
            Training: p.trainingTitle,
            Date: p.trainingDate,
            Logo: p.logoUrl
          })));
        }

        this.portfolios = portfolios || [];
        this.filteredPortfolios = portfolios || [];
        this.totalItems = portfolios?.length || 0;
        this.extractCategories();
        this.updatePaginatedPortfolios();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('❌ Portfolio Page: Error loading portfolios');
        console.error('Error details:', error);

        this.error = 'Failed to load portfolios. Please try again later.';
        this.isLoading = false;
        this.portfolios = [];
        this.filteredPortfolios = [];
        this.paginatedPortfolios = [];
      }
    });
  }

  extractCategories(): void {
    // Extract unique training titles as categories
    const categorySet = new Set(
      this.portfolios
        .map(p => p.trainingTitle)
        .filter(category => category && category.trim() !== '')
    );
    this.categories = Array.from(categorySet) as string[];
    console.log('Categories extracted:', this.categories);
  }

  filterByCategory(category: string): void {
    this.selectedCategory = category;
    console.log('Filtering by category:', category);

    if (category === 'all') {
      this.filteredPortfolios = this.portfolios;
    } else {
      this.filteredPortfolios = this.portfolios.filter(p => p.trainingTitle === category);
    }

    this.totalItems = this.filteredPortfolios.length;
    this.currentPage = 1; // Reset to first page
    this.updatePaginatedPortfolios();
    console.log('Filtered portfolios:', this.filteredPortfolios);
  }

  updatePaginatedPortfolios() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedPortfolios = this.filteredPortfolios.slice(startIndex, endIndex);
    console.log(`📄 Pagination: Page ${this.currentPage}, showing ${this.paginatedPortfolios.length} of ${this.filteredPortfolios.length} portfolios`);
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.updatePaginatedPortfolios();
    // Scroll to top of portfolio section
    const portfolioSection = document.getElementById('portfolio');
    if (portfolioSection) {
      portfolioSection.scrollIntoView({ behavior: 'smooth' });
    }
  }

  getUniqueCategories(): string[] {
    return ['all', ...this.categories];
  }

  getImageUrl(imagePath: string | undefined): string {
    if (!imagePath || imagePath.trim() === '') {
      console.log('Portfolio: No image path provided, using default');
  return 'assets/img/portfolio/app-1.jpg'; // existing default image
    }
    const url = this.portfolioService.getImageUrl(imagePath);
    console.log('Portfolio: Generated image URL:', url, 'from path:', imagePath);
    return url;
  }

  getCategoryDisplayName(category: string): string {
    if (category === 'all') {
      return this.t('portfolio.all-categories');
    }
    return category;
  }

  // Helper method to format dates
  formatDate(dateString: string | undefined): string {
    if (!dateString) return '';

    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    } catch (error) {
      console.error('Error formatting date:', error);
      return dateString;
    }
  }

  // Retry loading portfolios
  retryLoad(): void {
    this.loadPortfolios();
  }

  // Handle image loading errors
  onImageError(event: any): void {
    console.error('Portfolio image failed to load:', event.target.src);
    // Set a fallback default image to prevent infinite loop
    const fallback = 'assets/img/portfolio/app-1.jpg';
    if (!event.target.src.includes('portfolio/app-1.jpg')) {
      event.target.src = fallback;
    }
  }
}
