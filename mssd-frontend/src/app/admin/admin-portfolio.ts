import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Portfolio, Formation } from '../model/portfolio.model';
import { PortfolioService } from '../services/portfolio.service';
import { FileUploadService } from '../services/file-upload.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-admin-portfolio',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './admin-portfolio.html',
  styleUrl: './admin-portfolio.scss'
})
export class AdminPortfolio implements OnInit {
  portfolios: Portfolio[] = [];
  formations: Formation[] = [];
  selectedPortfolio: Portfolio | null = null;
  isEditing = false;
  isLoading = false;
  showDeleteConfirm = false;
  portfolioToDelete: Portfolio | null = null;
  selectedFile: File | null = null;
  isUploading = false;

  // Form data
  portfolioForm: Portfolio = this.getEmptyPortfolio();

  constructor(
    private portfolioService: PortfolioService,
    private fileUploadService: FileUploadService
  ) {}

  ngOnInit(): void {
    this.loadPortfolios();
    this.loadFormations();
  }

  loadFormations(): void {
    this.portfolioService.getFormations().subscribe({
      next: (formations) => {
        this.formations = formations;
        // Update empty portfolio to use first formation if available
        if (formations.length > 0 && this.portfolioForm.formationId === 0) {
          this.portfolioForm.formationId = formations[0].id;
        }
      },
      error: (error) => {
        console.error('Error loading formations:', error);
      }
    });
  }

  loadPortfolios(): void {
    this.isLoading = true;
    this.portfolioService.getAllPortfolios().subscribe({
      next: (portfolios) => {
        this.portfolios = portfolios;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading portfolios:', error);
        this.isLoading = false;
      }
    });
  }

  getEmptyPortfolio(): Portfolio {
    const defaultFormationId = this.formations.length > 0 ? this.formations[0].id : 1;
    return {
      title: '',
      description: '',
      formationId: defaultFormationId,
      category: '', // Will be set based on formation
      imageUrl: '',
      clientName: '',
      projectDate: '',
      projectUrl: '',
      active: true
    };
  }

  startEdit(portfolio?: Portfolio): void {
    this.isEditing = true;
    if (portfolio) {
      this.portfolioForm = { 
        ...portfolio,
        formationId: Number(portfolio.formationId) // Ensure it's a number
      };
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
    if (!this.portfolioForm.title || !this.portfolioForm.formationId || this.portfolioForm.formationId <= 0) {
      alert('Please fill in required fields (Title and Formation)');
      return;
    }

    this.isLoading = true;

    // Ensure formationId is a number, not a string
    const portfolioData: Portfolio = {
      ...this.portfolioForm,
      formationId: Number(this.portfolioForm.formationId)
    };

    console.log('Saving portfolio with data:', portfolioData);
    console.log('Formation ID type:', typeof portfolioData.formationId);
    console.log('Formation ID value:', portfolioData.formationId);

    if (this.selectedPortfolio && this.selectedPortfolio.id) {
      // Update existing portfolio
      this.portfolioService.updatePortfolio(this.selectedPortfolio.id, portfolioData).subscribe({
        next: (updatedPortfolio) => {
          console.log('Portfolio updated successfully:', updatedPortfolio);
          this.loadPortfolios();
          this.cancelEdit();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error updating portfolio:', error);
          alert('Error updating portfolio: ' + (error.message || 'Unknown error'));
          this.isLoading = false;
        }
      });
    } else {
      // Create new portfolio
      console.log('Creating portfolio with data:', portfolioData);
      this.portfolioService.createPortfolio(portfolioData).subscribe({
        next: (createdPortfolio) => {
          console.log('Portfolio created successfully:', createdPortfolio);
          this.loadPortfolios();
          this.cancelEdit();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error creating portfolio:', error);
          alert('Error creating portfolio: ' + (error.message || 'Unknown error'));
          this.isLoading = false;
        }
      });
    }
  }

  confirmDelete(portfolio: Portfolio): void {
    this.portfolioToDelete = portfolio;
    this.showDeleteConfirm = true;
  }

  deletePortfolio(): void {
    if (this.portfolioToDelete && this.portfolioToDelete.id) {
      this.isLoading = true;
      this.portfolioService.deletePortfolio(this.portfolioToDelete.id).subscribe({
        next: () => {
          this.loadPortfolios();
          this.showDeleteConfirm = false;
          this.portfolioToDelete = null;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error deleting portfolio:', error);
          this.isLoading = false;
        }
      });
    }
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.isUploading = true;
      this.fileUploadService.upload(file).subscribe({
        next: (url) => {
          this.portfolioForm.imageUrl = url;
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

  // Get image URL using the portfolio service
  getImageUrl(imagePath: string | undefined): string {
    if (!imagePath || imagePath.trim() === '') {
      return 'assets/img/portfolio/app-1.jpg'; // Default portfolio image (exists)
    }
    return this.portfolioService.getImageUrl(imagePath);
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

  toggleActive(portfolio: Portfolio): void {
    if (portfolio.id) {
      const updatedPortfolio = { ...portfolio, active: !portfolio.active };
      this.portfolioService.updatePortfolio(portfolio.id, updatedPortfolio).subscribe({
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
