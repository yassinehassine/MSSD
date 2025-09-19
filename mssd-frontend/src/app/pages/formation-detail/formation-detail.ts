import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { FormationService } from '../../services/formation.service';
import { CustomRequestService, CustomRequest } from '../../services/custom-request.service';
import { Formation } from '../../model/theme.model';

@Component({
  selector: 'app-formation-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './formation-detail.html',
  styleUrl: './formation-detail.scss'
})
export class FormationDetail implements OnInit {
  formation: Formation | null = null;
  isLoading = true;
  error = '';

  // Custom Request Form
  requestForm: CustomRequest = this.emptyRequestForm();
  showRequestForm = false;
  formError = '';
  formSuccess = '';
  submitting = false;

  constructor(
    private route: ActivatedRoute,
    private formationService: FormationService,
    private customRequestService: CustomRequestService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      const id = +params['id'];
      if (id) {
        this.loadFormation(id);
      }
    });
  }

  emptyRequestForm(): CustomRequest {
    return {
      companyName: '',
      contactPerson: '',
      email: '',
      phone: '',
      subject: '',
      details: '',
      budget: undefined,
      preferredStartDate: '',
      isExistingProgram: true,
      formationId: undefined
    };
  }

  loadFormation(id: number) {
    this.isLoading = true;
    this.formationService.getFormationById(id).subscribe({
      next: (formation) => {
        this.formation = formation;
        // Pre-fill the request form for existing program
        this.requestForm.isExistingProgram = true;
        this.requestForm.formationId = formation.id;
        this.requestForm.subject = `Demande de formation: ${formation.title}`;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Formation non trouvée.';
        this.isLoading = false;
      }
    });
  }

  toggleRequestForm() {
    this.showRequestForm = !this.showRequestForm;
    if (this.showRequestForm) {
      // Reset form when opening
      this.formError = '';
      this.formSuccess = '';
    }
  }

  onProgramTypeChange() {
    if (this.requestForm.isExistingProgram && this.formation) {
      this.requestForm.formationId = this.formation.id;
      this.requestForm.subject = `Demande de formation: ${this.formation.title}`;
    } else {
      this.requestForm.formationId = undefined;
      this.requestForm.subject = 'Demande de formation personnalisée';
    }
  }

  submitRequest(event: Event) {
    event.preventDefault();
    this.formError = '';
    this.formSuccess = '';

    // Validate required fields
    if (!this.requestForm.companyName || !this.requestForm.contactPerson || 
        !this.requestForm.email || !this.requestForm.subject || !this.requestForm.details) {
      this.formError = 'Veuillez remplir tous les champs obligatoires.';
      return;
    }

    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.requestForm.email)) {
      this.formError = 'Veuillez saisir une adresse email valide.';
      return;
    }

    this.submitting = true;

    this.customRequestService.submitCustomRequest(this.requestForm).subscribe({
      next: () => {
        this.formSuccess = 'Votre demande a été envoyée avec succès. Nous vous contacterons bientôt.';
        this.requestForm = this.emptyRequestForm();
        if (this.formation) {
          this.requestForm.isExistingProgram = true;
          this.requestForm.formationId = this.formation.id;
          this.requestForm.subject = `Demande de formation: ${this.formation.title}`;
        }
        this.submitting = false;
      },
      error: () => {
        this.formError = 'Erreur lors de l\'envoi de votre demande. Veuillez réessayer.';
        this.submitting = false;
      }
    });
  }
}