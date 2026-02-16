import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AnnexRequestService } from '../services/annex-request.service';
import { AnnexRequestResponse } from '../model/annexes.model';

@Component({
  selector: 'app-admin-formation-requests',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-formation-requests.html',
  styleUrls: ['./admin-formation-requests.scss']
})
export class AdminFormationRequests implements OnInit {
  requests: AnnexRequestResponse[] = [];
  filteredRequests: AnnexRequestResponse[] = [];
  loading = true;
  error = '';
  
  // Filter and search
  searchTerm = '';
  statusFilter = '';
  modalityFilter = '';
  typeFilter = ''; // all, custom, standard
  
  // Modal state
  selectedRequest: AnnexRequestResponse | null = null;
  showDetailsModal = false;
  showStatusModal = false;
  newStatus = '';
  statusNote = '';

  // Statistics
  stats = {
    total: 0,
    pending: 0,
    approved: 0,
    rejected: 0,
    inProgress: 0,
    completed: 0
  };

  constructor(private annexRequestService: AnnexRequestService) {}

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests(): void {
    this.loading = true;
    this.error = '';
    
    this.annexRequestService.getAllRequests().subscribe({
      next: (requests) => {
        this.requests = requests;
        this.filteredRequests = requests;
        this.calculateStats();
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading requests:', err);
        this.error = 'Erreur lors du chargement des demandes de formation.';
        this.loading = false;
      }
    });
  }

  calculateStats(): void {
    this.stats.total = this.requests.length;
    this.stats.pending = this.requests.filter(r => r.status === 'PENDING').length;
    this.stats.approved = this.requests.filter(r => r.status === 'APPROVED').length;
    this.stats.rejected = this.requests.filter(r => r.status === 'REJECTED').length;
    this.stats.inProgress = this.requests.filter(r => r.status === 'IN_PROGRESS').length;
    this.stats.completed = this.requests.filter(r => r.status === 'COMPLETED').length;
  }

  applyFilters(): void {
    let filtered = [...this.requests];

    // Search filter
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(request => 
        request.companyName.toLowerCase().includes(term) ||
        request.email.toLowerCase().includes(term) ||
        (request.formationTitle && request.formationTitle.toLowerCase().includes(term)) ||
        (request.customDescription && request.customDescription.toLowerCase().includes(term))
      );
    }

    // Status filter
    if (this.statusFilter) {
      filtered = filtered.filter(request => request.status === this.statusFilter);
    }

    // Modality filter
    if (this.modalityFilter) {
      filtered = filtered.filter(request => request.modality === this.modalityFilter);
    }

    // Type filter
    if (this.typeFilter === 'custom') {
      filtered = filtered.filter(request => request.isCustom);
    } else if (this.typeFilter === 'standard') {
      filtered = filtered.filter(request => !request.isCustom);
    }

    this.filteredRequests = filtered;
  }

  onSearchChange(): void {
    this.applyFilters();
  }

  onFilterChange(): void {
    this.applyFilters();
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.statusFilter = '';
    this.modalityFilter = '';
    this.typeFilter = '';
    this.applyFilters();
  }

  viewDetails(request: AnnexRequestResponse): void {
    this.selectedRequest = request;
    this.showDetailsModal = true;
  }

  closeDetailsModal(): void {
    this.showDetailsModal = false;
    this.selectedRequest = null;
  }

  openStatusModal(request: AnnexRequestResponse): void {
    this.selectedRequest = request;
    this.newStatus = request.status;
    this.statusNote = '';
    this.showStatusModal = true;
  }

  closeStatusModal(): void {
    this.showStatusModal = false;
    this.selectedRequest = null;
    this.newStatus = '';
    this.statusNote = '';
  }

  updateStatus(): void {
    if (!this.selectedRequest || !this.newStatus) return;

    this.annexRequestService.updateRequestStatus(this.selectedRequest.id, this.newStatus).subscribe({
      next: (updatedRequest) => {
        // Update the request in the list
        const index = this.requests.findIndex(r => r.id === this.selectedRequest!.id);
        if (index !== -1) {
          this.requests[index] = updatedRequest;
        }
        this.calculateStats();
        this.applyFilters();
        this.closeStatusModal();
      },
      error: (err) => {
        console.error('Error updating status:', err);
        this.error = 'Erreur lors de la mise à jour du statut.';
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDING': return 'badge bg-warning';
      case 'APPROVED': return 'badge bg-success';
      case 'REJECTED': return 'badge bg-danger';
      case 'IN_PROGRESS': return 'badge bg-info';
      case 'COMPLETED': return 'badge bg-secondary';
      default: return 'badge bg-light';
    }
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'PENDING': return 'En attente';
      case 'APPROVED': return 'Approuvée';
      case 'REJECTED': return 'Rejetée';
      case 'IN_PROGRESS': return 'En cours';
      case 'COMPLETED': return 'Terminée';
      default: return status;
    }
  }

  getModalityLabel(modality: string): string {
    switch (modality) {
      case 'IN_PERSON': return 'Présentiel';
      case 'REMOTE': return 'À distance';
      case 'HYBRID': return 'Hybride';
      default: return modality;
    }
  }

  getModalityIcon(modality: string): string {
    switch (modality) {
      case 'IN_PERSON': return 'bi-people';
      case 'REMOTE': return 'bi-laptop';
      case 'HYBRID': return 'bi-hybrid';
      default: return 'bi-question';
    }
  }

  exportRequests(): void {
    // TODO: Implement export functionality
    console.log('Export functionality to be implemented');
  }
}