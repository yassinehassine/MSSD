import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PortfolioItem, PortfolioItemService } from '../services/portfolio-item.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-admin-portfolio',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './admin-portfolio.html',
  styleUrl: './admin-portfolio.scss'
})
export class AdminPortfolio implements OnInit {
  portfolios: PortfolioItem[] = [];
  selectedPortfolio: PortfolioItem | null = null;
  isEditing = false;
  isLoading = false;
  showDeleteConfirm = false;
  portfolioToDelete: PortfolioItem | null = null;
  selectedFile: File | null = null;
  isUploading = false;

  // Form data
  portfolioForm: PortfolioItem = this.getEmptyPortfolio();

  constructor(
    private portfolioItemService: PortfolioItemService
  ) {}

  ngOnInit(): void {
    this.loadPortfolios();
  }

  loadPortfolios(): void {
    this.isLoading = true;
    this.portfolioItemService.getAllPortfolioItems().subscribe({
      next: (portfolios) => {
        this.portfolios = portfolios;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading portfolio items:', error);
        this.isLoading = false;
      }
    });
  }

  getEmptyPortfolio(): PortfolioItem {
    return {
      companyName: '',
      trainingTitle: '',
      trainingDate: new Date().toISOString().split('T')[0],
      logoUrl: '',
      description: '',
      active: true
    };
  }

  startEdit(portfolio?: PortfolioItem): void {
    this.isEditing = true;
    if (portfolio) {
      this.portfolioForm = { ...portfolio };
      this.selectedPortfolio = portfolio;
    } else {
      this.portfolioForm = this.getEmptyPortfolio();
      this.selectedPortfolio = null;
    }
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.selectedPortfolio = null;
    this.portfolioForm = this.getEmptyPortfolio();
  }

  savePortfolio(): void {
    if (!this.portfolioForm.companyName || !this.portfolioForm.trainingTitle || !this.portfolioForm.trainingDate) {
      alert('Please fill in required fields (Company Name, Training Title, and Training Date)');
      return;
    }

    this.isLoading = true;

    const portfolioData: PortfolioItem = {
      ...this.portfolioForm,
      active: this.portfolioForm.active ?? true
    };

    console.log('Saving portfolio item with data:', portfolioData);

    if (this.selectedPortfolio && this.selectedPortfolio.id) {
      // Update existing portfolio
      this.portfolioItemService.updatePortfolioItem(this.selectedPortfolio.id, portfolioData).subscribe({
        next: (updatedPortfolio) => {
          console.log('Portfolio item updated successfully:', updatedPortfolio);
          this.loadPortfolios();
          this.cancelEdit();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error updating portfolio item:', error);
          alert('Error updating portfolio item: ' + (error.error?.message || error.message || 'Unknown error'));
          this.isLoading = false;
        }
      });
    } else {
      // Create new portfolio
      console.log('Creating portfolio item with data:', portfolioData);
      this.portfolioItemService.createPortfolioItem(portfolioData).subscribe({
        next: (createdPortfolio) => {
          console.log('Portfolio item created successfully:', createdPortfolio);
          this.loadPortfolios();
          this.cancelEdit();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error creating portfolio item:', error);
          alert('Error creating portfolio item: ' + (error.error?.message || error.message || 'Unknown error'));
          this.isLoading = false;
        }
      });
    }
  }

  confirmDelete(portfolio: PortfolioItem): void {
    this.portfolioToDelete = portfolio;
    this.showDeleteConfirm = true;
  }

  deletePortfolio(): void {
    if (this.portfolioToDelete && this.portfolioToDelete.id) {
      this.isLoading = true;
      this.portfolioItemService.deletePortfolioItem(this.portfolioToDelete.id).subscribe({
        next: () => {
          this.loadPortfolios();
          this.showDeleteConfirm = false;
          this.portfolioToDelete = null;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error deleting portfolio item:', error);
          this.isLoading = false;
        }
      });
    }
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.isUploading = true;
      this.portfolioItemService.uploadLogo(file).subscribe({
        next: (response) => {
          this.portfolioForm.logoUrl = response.url;
          this.isUploading = false;
        },
        error: (error) => {
          console.error('Error uploading file:', error);
          alert('Error uploading file. Please try again.');
          this.isUploading = false;
        }
      });
    }
  }

  // Get image URL
  getImageUrl(imagePath: string | undefined): string {
    if (!imagePath || imagePath.trim() === '') {
      return 'assets/img/portfolio/app-1.jpg'; // Default portfolio image
    }
    // If it's already a full URL, return it
    if (imagePath.startsWith('http')) {
      return imagePath;
    }
    // Otherwise prepend the API base URL
    return `http://localhost:8080/api/files/${imagePath}`;
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

  toggleActive(portfolio: PortfolioItem): void {
    if (portfolio.id) {
      const updatedPortfolio = { ...portfolio, active: !portfolio.active };
      this.portfolioItemService.updatePortfolioItem(portfolio.id, updatedPortfolio).subscribe({
        next: () => {
          this.loadPortfolios();
        },
        error: (error) => {
          console.error('Error updating portfolio status:', error);
        }
      });
    }
  }

  cancelDelete(): void {
    this.showDeleteConfirm = false;
    this.portfolioToDelete = null;
  }
}
