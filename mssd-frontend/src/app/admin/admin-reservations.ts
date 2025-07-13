import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservationService, ReservationDto } from '../services/reservation.service';
import {RouterLink} from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-reservations',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './admin-reservations.html',
  styleUrls: ['./admin-reservations.scss']
})
export class AdminReservations implements OnInit {
  reservations: ReservationDto[] = [];
  loading = false;
  errorMessage = '';

  // Search/filter state
  searchTerm = '';
  statusFilter = '';
  eventFilter = '';

  // Edit modal state
  showEditModal = false;
  editReservation: ReservationDto | null = null;
  editForm: any = {};
  editLoading = false;
  editError = '';

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    this.loading = true;
    this.reservationService.getAllReservations().subscribe({
      next: (data) => {
        this.reservations = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to load reservations.';
        this.loading = false;
      }
    });
  }

  get filteredReservations() {
    let filtered = this.reservations;
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(r =>
        r.visitorName.toLowerCase().includes(term) ||
        r.visitorEmail.toLowerCase().includes(term) ||
        (r.calendarTitle && r.calendarTitle.toLowerCase().includes(term))
      );
    }
    if (this.statusFilter) {
      filtered = filtered.filter(r => r.status === this.statusFilter);
    }
    if (this.eventFilter) {
      filtered = filtered.filter(r => r.calendarTitle === this.eventFilter);
    }
    return filtered;
  }

  get groupedReservations() {
    const groups: { [event: string]: ReservationDto[] } = {};
    for (const r of this.filteredReservations) {
      const key = r.calendarTitle || `Event #${r.calendarId}`;
      if (!groups[key]) groups[key] = [];
      groups[key].push(r);
    }
    return groups;
  }

  get eventOptions() {
    return Array.from(new Set(this.reservations.map(r => r.calendarTitle))).filter(Boolean);
  }

  get statusOptions() {
    return Array.from(new Set(this.reservations.map(r => r.status)));
  }

  confirmReservation(id: number) {
    this.reservationService.confirmReservation(id).subscribe(() => this.loadReservations());
  }
  cancelReservation(id: number) {
    this.reservationService.cancelReservation(id).subscribe(() => this.loadReservations());
  }
  deleteReservation(id: number) {
    if (confirm('Are you sure you want to delete this reservation?')) {
      this.reservationService.deleteReservation(id).subscribe(() => this.loadReservations());
    }
  }
  openEditModal(res: ReservationDto) {
    this.editReservation = res;
    this.editForm = { ...res };
    this.editError = '';
    this.showEditModal = true;
  }
  closeEditModal() {
    this.showEditModal = false;
    this.editReservation = null;
    this.editForm = {};
    this.editError = '';
    this.editLoading = false;
  }
  submitEdit() {
    if (!this.editForm.visitorName || !this.editForm.visitorEmail) {
      this.editError = 'Name and email are required.';
      return;
    }
    this.editLoading = true;
    this.reservationService.updateReservation(this.editReservation!.id, this.editForm).subscribe({
      next: () => {
        this.editLoading = false;
        this.closeEditModal();
        this.loadReservations();
      },
      error: (err) => {
        this.editError = err.error?.message || 'Update failed.';
        this.editLoading = false;
      }
    });
  }
}
