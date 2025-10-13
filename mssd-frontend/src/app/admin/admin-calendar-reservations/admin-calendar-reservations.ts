import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CalendarReservationService, CalendarReservation, CalendarReservationStats } from '../../services/calendar-reservation.service';

@Component({
  selector: 'app-admin-calendar-reservations',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admin-calendar-reservations.html',
  styleUrl: './admin-calendar-reservations.scss'
})
export class AdminCalendarReservations implements OnInit {
  reservations: CalendarReservation[] = [];
  filteredReservations: CalendarReservation[] = [];
  stats: CalendarReservationStats = {
    total: 0,
    pending: 0,
    confirmed: 0,
    cancelled: 0,
    completed: 0
  };
  
  loading = true;
  error = '';
  searchTerm = '';
  statusFilter = 'ALL';
  selectedReservation: CalendarReservation | null = null;
  showModal = false;

  constructor(private calendarReservationService: CalendarReservationService) {}

  ngOnInit() {
    this.loadReservations();
    this.loadStatistics();
  }

  loadReservations() {
    this.loading = true;
    this.calendarReservationService.getAllReservations().subscribe({
      next: (data) => {
        this.reservations = data;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading calendar reservations:', err);
        this.error = 'Erreur lors du chargement des réservations.';
        this.loading = false;
      }
    });
  }

  loadStatistics() {
    this.calendarReservationService.getReservationStatistics().subscribe({
      next: (data) => {
        this.stats = data;
      },
      error: (err) => {
        console.error('Error loading statistics:', err);
      }
    });
  }

  applyFilters() {
    let filtered = [...this.reservations];

    // Apply search filter
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(reservation =>
        reservation.clientName.toLowerCase().includes(term) ||
        reservation.clientEmail.toLowerCase().includes(term) ||
        reservation.eventTitle.toLowerCase().includes(term) ||
        (reservation.eventDescription && reservation.eventDescription.toLowerCase().includes(term))
      );
    }

    // Apply status filter
    if (this.statusFilter !== 'ALL') {
      filtered = filtered.filter(reservation => reservation.status === this.statusFilter);
    }

    this.filteredReservations = filtered;
  }

  onSearchChange() {
    this.applyFilters();
  }

  onStatusFilterChange() {
    this.applyFilters();
  }

  viewReservation(reservation: CalendarReservation) {
    this.selectedReservation = { ...reservation };
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.selectedReservation = null;
  }

  updateReservationStatus(id: number, status: string) {
    this.calendarReservationService.updateReservationStatus(id, status).subscribe({
      next: () => {
        this.loadReservations();
        this.loadStatistics();
        if (this.selectedReservation && this.selectedReservation.id === id) {
          this.selectedReservation.status = status as any;
        }
      },
      error: (err) => {
        console.error('Error updating reservation status:', err);
        this.error = 'Erreur lors de la mise à jour du statut.';
      }
    });
  }

  deleteReservation(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette réservation ?')) {
      this.calendarReservationService.deleteReservation(id).subscribe({
        next: () => {
          this.loadReservations();
          this.loadStatistics();
          this.closeModal();
        },
        error: (err) => {
          console.error('Error deleting reservation:', err);
          this.error = 'Erreur lors de la suppression de la réservation.';
        }
      });
    }
  }

  updateReservation() {
    if (this.selectedReservation && this.selectedReservation.id) {
      this.calendarReservationService.updateReservation(this.selectedReservation.id, this.selectedReservation).subscribe({
        next: () => {
          this.loadReservations();
          this.loadStatistics();
          this.closeModal();
        },
        error: (err) => {
          console.error('Error updating reservation:', err);
          this.error = 'Erreur lors de la mise à jour de la réservation.';
        }
      });
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDING': return 'badge bg-warning';
      case 'CONFIRMED': return 'badge bg-success';
      case 'CANCELLED': return 'badge bg-danger';
      case 'COMPLETED': return 'badge bg-info';
      default: return 'badge bg-secondary';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'PENDING': return 'En attente';
      case 'CONFIRMED': return 'Confirmée';
      case 'CANCELLED': return 'Annulée';
      case 'COMPLETED': return 'Terminée';
      default: return status;
    }
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  formatDuration(minutes: number | undefined): string {
    if (!minutes) return 'Non spécifiée';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    if (hours === 0) return `${mins}min`;
    if (mins === 0) return `${hours}h`;
    return `${hours}h${mins}min`;
  }
}