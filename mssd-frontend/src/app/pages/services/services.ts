import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { FormationService, Formation } from '../../services/formation.service';
import { AnnexRequestService } from '../../services/annex-request.service';
import { AnnexRequest } from '../../model/annexes.model';
import { PaginationComponent } from '../../shared/pagination/pagination.component';

@Component({
  selector: 'app-services',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, PaginationComponent],
  templateUrl: './services.html',
  styleUrl: './services.scss'
})
export class Services {
  formations: Formation[] = [];
  paginatedFormations: Formation[] = [];
  loading = true;
  error = '';

  // Pagination
  currentPage = 1;
  itemsPerPage = 9; // 3x3 grid
  totalItems = 0;

  // Booking functionality
  bookingForms: { [key: number]: any } = {};
  bookingStatus: { [key: number]: string } = {};
  bookingError: { [key: number]: string } = {};

  // Custom request functionality
  customRequest = {
    companyName: '',
    contactPerson: '',
    email: '',
    phone: '',
    subject: '',
    details: '',
    budget: '',
    preferredStartDate: ''
  };
  isSubmittingCustomRequest = false;
  customRequestSuccess = false;
  customRequestError = '';

  constructor(
    private formationService: FormationService,
    private annexRequestService: AnnexRequestService,
    private router: Router
  ) {
    this.loadFormations();
  }

  loadFormations() {
    this.loading = true;
    this.formationService.getAllFormations().subscribe({
      next: (data) => {
        this.formations = data.filter(f => f.published);
        this.totalItems = this.formations.length;
        
        // Initialize booking forms for each formation
        this.formations.forEach(formation => {
          this.bookingForms[formation.id] = {
            name: '',
            email: '',
            phone: '',
            company: ''
          };
          this.bookingStatus[formation.id] = '';
        });
        
        this.updatePaginatedFormations();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading formations:', err);
        this.error = 'Erreur lors du chargement des formations.';
        this.loading = false;
      }
    });
  }

  updatePaginatedFormations() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedFormations = this.formations.slice(startIndex, endIndex);
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.updatePaginatedFormations();
    // Scroll to top of services section
    const servicesSection = document.getElementById('services');
    if (servicesSection) {
      servicesSection.scrollIntoView({ behavior: 'smooth' });
    }
  }

  submitBooking(formationId: number, event: Event) {
    event.preventDefault();
    
    const form = this.bookingForms[formationId];
    if (!form.name || !form.email || !form.phone || !form.company) {
      this.bookingError[formationId] = 'Tous les champs sont obligatoires.';
      this.bookingStatus[formationId] = 'error';
      return;
    }

    this.bookingStatus[formationId] = 'submitting';
    this.bookingError[formationId] = '';

    const formation = this.formations.find(f => f.id === formationId);
    
    const annexRequest: AnnexRequest = {
      companyName: form.company,
      email: form.email,
      phone: form.phone,
      formationId: formationId,
      isCustom: false,
      numParticipants: 1,
      modality: 'IN_PERSON',
      preferredDate: new Date().toISOString().split('T')[0],
      notes: `Demande de formation depuis la page services - Contact: ${form.name}`
    };

    this.annexRequestService.createRequest(annexRequest).subscribe({
      next: (response) => {
        this.bookingStatus[formationId] = 'success';
        // Reset form
        this.bookingForms[formationId] = {
          name: '',
          email: '',
          phone: '',
          company: ''
        };
      },
      error: (err) => {
        console.error('Error submitting booking:', err);
        this.bookingError[formationId] = 'Erreur lors de la soumission. Veuillez réessayer.';
        this.bookingStatus[formationId] = 'error';
      }
    });
  }

  submitCustomRequest(event: Event) {
    event.preventDefault();
    
    if (!this.customRequest.companyName || !this.customRequest.email || !this.customRequest.subject) {
      this.customRequestError = 'Les champs obligatoires doivent être remplis.';
      return;
    }

    this.isSubmittingCustomRequest = true;
    this.customRequestError = '';
    this.customRequestSuccess = false;

    const annexRequest: AnnexRequest = {
      companyName: this.customRequest.companyName,
      email: this.customRequest.email,
      phone: this.customRequest.phone || '',
      isCustom: true,
      customDescription: `${this.customRequest.subject}\n\n${this.customRequest.details}\n\nBudget: ${this.customRequest.budget}`,
      numParticipants: 1,
      modality: 'IN_PERSON',
      preferredDate: this.customRequest.preferredStartDate || new Date().toISOString().split('T')[0],
      notes: `Demande personnalisée - Contact: ${this.customRequest.contactPerson}`
    };

    this.annexRequestService.createRequest(annexRequest).subscribe({
      next: (response) => {
        this.customRequestSuccess = true;
        this.isSubmittingCustomRequest = false;
        // Reset form
        this.customRequest = {
          companyName: '',
          contactPerson: '',
          email: '',
          phone: '',
          subject: '',
          details: '',
          budget: '',
          preferredStartDate: ''
        };
      },
      error: (err) => {
        console.error('Error submitting custom request:', err);
        this.customRequestError = 'Erreur lors de la soumission. Veuillez réessayer.';
        this.isSubmittingCustomRequest = false;
      }
    });
  }

  goToReviews(formationId: number) {
    this.router.navigate(['/formation', formationId, 'reviews']);
  }

  goToFormation(slug: string) {
    this.router.navigate(['/formation', slug]);
  }

  requestFormation() {
    this.router.navigate(['/annexes/request']);
  }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

  viewPortfolio() {
    this.router.navigate(['/portfolio']);
  }

  contactUs() {
    this.router.navigate(['/contact']);
  }
}
