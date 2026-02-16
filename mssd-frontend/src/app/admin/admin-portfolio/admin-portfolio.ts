import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PortfolioItemService, PortfolioItem } from '../../services/portfolio-item.service';

@Component({
  selector: 'app-admin-portfolio',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-portfolio.html',
  styleUrl: './admin-portfolio.scss'
})
export class AdminPortfolio implements OnInit {
  portfolioItems: PortfolioItem[] = [];
  currentItem: PortfolioItem = this.getEmptyItem();
  showForm: boolean = false;
  isEditing: boolean = false;
  selectedFile: File | null = null;
  isLoading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  constructor(private portfolioItemService: PortfolioItemService) {}

  ngOnInit(): void {
    this.loadPortfolioItems();
  }

  loadPortfolioItems(): void {
    this.isLoading = true;
    this.portfolioItemService.getAllPortfolioItems().subscribe({
      next: (items) => {
        this.portfolioItems = items;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading portfolio items:', error);
        this.showMessage('Erreur lors du chargement des éléments', 'error');
        this.isLoading = false;
      }
    });
  }

  showAddForm(): void {
    this.currentItem = this.getEmptyItem();
    this.isEditing = false;
    this.showForm = true;
    this.selectedFile = null;
  }

  editItem(item: PortfolioItem): void {
    this.currentItem = { ...item };
    this.isEditing = true;
    this.showForm = true;
    this.selectedFile = null;
  }

  cancelForm(): void {
    this.showForm = false;
    this.currentItem = this.getEmptyItem();
    this.selectedFile = null;
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file && file.type.startsWith('image/')) {
      this.selectedFile = file;
    } else {
      this.showMessage('Veuillez sélectionner un fichier image valide', 'error');
    }
  }

  saveItem(): void {
    if (!this.validateForm()) {
      return;
    }

    this.isLoading = true;

    // Upload logo first if a file is selected
    if (this.selectedFile) {
      this.portfolioItemService.uploadLogo(this.selectedFile).subscribe({
        next: (response) => {
          this.currentItem.logoUrl = response.url;
          this.savePortfolioItem();
        },
        error: (error) => {
          console.error('Error uploading logo:', error);
          this.showMessage('Erreur lors de l\'upload du logo', 'error');
          this.isLoading = false;
        }
      });
    } else {
      this.savePortfolioItem();
    }
  }

  private savePortfolioItem(): void {
    const operation = this.isEditing 
      ? this.portfolioItemService.updatePortfolioItem(this.currentItem.id!, this.currentItem)
      : this.portfolioItemService.createPortfolioItem(this.currentItem);

    operation.subscribe({
      next: () => {
        this.showMessage(
          this.isEditing ? 'Élément mis à jour avec succès' : 'Élément créé avec succès',
          'success'
        );
        this.loadPortfolioItems();
        this.cancelForm();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error saving portfolio item:', error);
        this.showMessage('Erreur lors de la sauvegarde', 'error');
        this.isLoading = false;
      }
    });
  }

  deleteItem(item: PortfolioItem): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer "${item.companyName}" ?`)) {
      this.isLoading = true;
      this.portfolioItemService.deletePortfolioItem(item.id!).subscribe({
        next: () => {
          this.showMessage('Élément supprimé avec succès', 'success');
          this.loadPortfolioItems();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error deleting portfolio item:', error);
          this.showMessage('Erreur lors de la suppression', 'error');
          this.isLoading = false;
        }
      });
    }
  }

  toggleActive(item: PortfolioItem): void {
    const updatedItem = { ...item, active: !item.active };
    this.portfolioItemService.updatePortfolioItem(item.id!, updatedItem).subscribe({
      next: () => {
        this.loadPortfolioItems();
        this.showMessage('Statut mis à jour', 'success');
      },
      error: (error) => {
        console.error('Error updating status:', error);
        this.showMessage('Erreur lors de la mise à jour du statut', 'error');
      }
    });
  }

  getImageUrl(logoUrl?: string): string {
    if (!logoUrl) return 'assets/img/logo1.png';
    if (logoUrl.startsWith('http')) return logoUrl;
    // Strip uploads/ prefix and use proxy-relative path
    const path = logoUrl.startsWith('uploads/') ? logoUrl.substring(8) : logoUrl;
    return `/api/files/${path}`;
  }

  private validateForm(): boolean {
    if (!this.currentItem.companyName?.trim()) {
      this.showMessage('Le nom de l\'entreprise est requis', 'error');
      return false;
    }
    if (!this.currentItem.trainingTitle?.trim()) {
      this.showMessage('Le titre de la formation est requis', 'error');
      return false;
    }
    if (!this.currentItem.trainingDate) {
      this.showMessage('La date de la formation est requise', 'error');
      return false;
    }
    return true;
  }

  private getEmptyItem(): PortfolioItem {
    return {
      companyName: '',
      trainingTitle: '',
      trainingDate: '',
      logoUrl: '',
      description: '',
      active: true
    };
  }

  private showMessage(message: string, type: 'success' | 'error'): void {
    this.message = message;
    this.messageType = type;
    setTimeout(() => {
      this.message = '';
    }, 5000);
  }
}