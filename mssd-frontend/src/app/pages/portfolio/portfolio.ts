import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Portfolio as PortfolioModel, Formation } from '../../model/portfolio.model';
import { PortfolioService } from '../../services/portfolio.service';

@Component({
  selector: 'app-portfolio',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './portfolio.html',
  styleUrl: './portfolio.scss'
})
export class Portfolio implements OnInit {
  portfolios: PortfolioModel[] = [];
  filteredPortfolios: PortfolioModel[] = [];
  formations: Formation[] = [];
  selectedCategory = 'all';
  categories: string[] = [];
  isLoading = true;
  error: string | null = null;

  constructor(private portfolioService: PortfolioService) {}

  ngOnInit(): void {
    this.loadFormations();
    this.loadPortfolios();
  }

  loadFormations(): void {
    this.portfolioService.getFormations().subscribe({
      next: (formations) => {
        this.formations = formations;
        console.log('Formations loaded:', formations);
      },
      error: (error) => {
        console.error('Error loading formations:', error);
        this.error = 'Failed to load formation categories';
      }
    });
  }

  loadPortfolios(): void {
    this.isLoading = true;
    this.error = null;
    
    this.portfolioService.getActivePortfolios().subscribe({
      next: (portfolios) => {
        this.portfolios = portfolios;
        this.filteredPortfolios = portfolios;
        this.extractCategories();
        this.isLoading = false;
        console.log('Active portfolios loaded:', portfolios);
      },
      error: (error) => {
        console.error('Error loading portfolios:', error);
        this.error = 'Failed to load portfolios. Please try again later.';
        this.isLoading = false;
      }
    });
  }

  extractCategories(): void {
    // Extract unique formation categories
    const categorySet = new Set(
      this.portfolios
        .map(p => p.formationCategory)
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
      this.filteredPortfolios = this.portfolios.filter(p => p.formationCategory === category);
    }
    
    console.log('Filtered portfolios:', this.filteredPortfolios);
  }

  getUniqueCategories(): string[] {
    return ['all', ...this.categories];
  }

  getImageUrl(imagePath: string | undefined): string {
    if (!imagePath || imagePath.trim() === '') {
      return 'assets/img/portfolio-default.jpg'; // Single default image
    }
    return this.portfolioService.getImageUrl(imagePath);
  }

  getFormationName(portfolio: PortfolioModel): string {
    return portfolio.formationName || 'Unknown Formation';
  }

  getFormationCategory(portfolio: PortfolioModel): string {
    return portfolio.formationCategory || 'General';
  }

  getCategoryDisplayName(category: string): string {
    if (category === 'all') {
      return 'All Categories';
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
    if (!event.target.src.includes('portfolio-default.jpg')) {
      event.target.src = 'assets/img/portfolio-default.jpg';
    }
  }
}
