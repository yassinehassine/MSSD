import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormationBookingService, FormationBookingResponse } from '../services/formation-booking.service';

@Component({
  selector: 'app-admin-bookings',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-bookings.html',
  styleUrl: './admin-contacts.scss'
})
export class AdminBookings implements OnInit {
  bookings: FormationBookingResponse[] = [];
  isLoading = true;
  error = '';

  constructor(private bookingService: FormationBookingService) {}

  ngOnInit() {
    this.loadBookings();
  }

  loadBookings() {
    this.isLoading = true;
    this.bookingService.getAllBookings().subscribe({
      next: (data) => {
        this.bookings = data;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Failed to load bookings.';
        this.isLoading = false;
      }
    });
  }
} 