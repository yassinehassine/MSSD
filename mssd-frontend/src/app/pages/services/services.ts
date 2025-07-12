import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FormationBookingService, Formation, FormationBookingRequest } from '../../services/formation-booking.service';
import { CustomRequestService, CustomRequest } from '../../services/custom-request.service';

const BACKEND_URL = 'http://localhost:8080'; // Change if your backend runs elsewhere

@Component({
  selector: 'app-services',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './services.html',
  styleUrl: './services.scss'
})
export class Services {
  formations: Formation[] = [];
  bookingStatus: { [formationId: number]: 'idle' | 'submitting' | 'success' | 'error' } = {};
  bookingError: { [formationId: number]: string } = {};

  bookingForms: { [formationId: number]: FormationBookingRequest } = {};

  customRequest: CustomRequest = {
    companyName: '',
    contactPerson: '',
    email: '',
    phone: '',
    subject: '',
    details: '',
    budget: undefined,
    preferredStartDate: undefined
  };
  isSubmittingCustomRequest = false;
  customRequestSuccess = false;
  customRequestError = '';

  constructor(
    private formationBookingService: FormationBookingService,
    private customRequestService: CustomRequestService
  ) {
    this.loadFormations();
  }

  loadFormations() {
    this.formationBookingService.getPublishedFormations().subscribe({
      next: (data) => {
        // Prepend backend URL to imageUrl if it's a relative path
        this.formations = data.map(f => ({
          ...f,
          imageUrl: f.imageUrl && !/^https?:\/\//.test(f.imageUrl) ? BACKEND_URL + f.imageUrl : f.imageUrl
        }));
        for (const f of this.formations) {
          this.bookingForms[f.id] = {
            formationId: f.id,
            name: '',
            email: '',
            phone: '',
            company: ''
          };
          this.bookingStatus[f.id] = 'idle';
          this.bookingError[f.id] = '';
        }
      }
    });
  }

  submitBooking(formationId: number, event: Event) {
    event.preventDefault();
    const form = this.bookingForms[formationId];
    this.bookingStatus[formationId] = 'submitting';
    this.bookingError[formationId] = '';
    this.formationBookingService.bookFormation(form).subscribe({
      next: () => {
        this.bookingStatus[formationId] = 'success';
      },
      error: (err) => {
        this.bookingStatus[formationId] = 'error';
        this.bookingError[formationId] = 'Booking failed. Please try again.';
      }
    });
  }

  submitCustomRequest(event: Event) {
    event.preventDefault();
    this.isSubmittingCustomRequest = true;
    this.customRequestSuccess = false;
    this.customRequestError = '';
    this.customRequestService.submitCustomRequest(this.customRequest).subscribe({
      next: () => {
        this.isSubmittingCustomRequest = false;
        this.customRequestSuccess = true;
        this.customRequest = {
          companyName: '',
          contactPerson: '',
          email: '',
          phone: '',
          subject: '',
          details: '',
          budget: undefined,
          preferredStartDate: undefined
        };
      },
      error: () => {
        this.isSubmittingCustomRequest = false;
        this.customRequestError = 'Failed to submit your request. Please try again.';
      }
    });
  }
}
