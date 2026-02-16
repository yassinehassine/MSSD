import { Component, OnInit } from '@angular/core';
import { CommonModule, KeyValuePipe } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AnnexRequestService } from '../../services/annex-request.service';
import { ThemeService } from '../../services/theme.service';
import { FormationSummary, ModalityOption } from '../../model/annexes.model';

@Component({
  selector: 'app-annexes-request',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, KeyValuePipe],
  templateUrl: './annexes-request.html',
  styleUrls: ['./annexes-request.scss']
})
export class AnnexesRequest implements OnInit {
  requestForm: FormGroup;
  formations: FormationSummary[] = [];
  modalityOptions: ModalityOption[] = [];
  isSubmitting = false;
  error = '';
  success = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private annexRequestService: AnnexRequestService,
    private themeService: ThemeService
  ) {
    this.requestForm = this.createForm();
  }

  ngOnInit(): void {
    this.loadFormations();
    this.modalityOptions = this.annexRequestService.getModalityOptions();
    this.handleQueryParams();
  }

  createForm(): FormGroup {
    return this.fb.group({
      // Company Information
      companyName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: [''],

      // Training Selection
      isCustom: [false, Validators.required],
      formationId: [null],
      customDescription: [''],

      // Common Fields
      numParticipants: [1, [Validators.required, Validators.min(1), Validators.max(500)]],
      modality: ['', Validators.required],
      preferredDate: ['', Validators.required],
      notes: ['']
    });
  }

  loadFormations(): void {
    this.themeService.getAllFormations().subscribe({
      next: (formations) => {
        this.formations = formations;
        // After formations are loaded, check for theme query param
        this.handleQueryParams();
      },
      error: (err) => {
        console.error('Error loading formations:', err);
        this.error = 'Erreur lors du chargement des formations.';
      }
    });
  }

  handleQueryParams(): void {
    const themeSlug = this.route.snapshot.queryParamMap.get('theme');
    if (themeSlug && this.formations.length > 0) {
      // Find formations from the specified theme
      const themeFormations = this.formations.filter(f => 
        f.themeName?.toLowerCase().replace(/\s+/g, '-') === themeSlug
      );
      
      if (themeFormations.length > 0) {
        // Auto-select first formation from the theme
        this.requestForm.patchValue({
          isCustom: false,
          formationId: themeFormations[0].id
        });
      }
    }
  }

  onTrainingTypeChange(): void {
    const isCustom = this.requestForm.get('isCustom')?.value;
    
    if (isCustom) {
      // Custom training selected
      this.requestForm.get('formationId')?.clearValidators();
      this.requestForm.get('customDescription')?.setValidators([Validators.required, Validators.minLength(10)]);
    } else {
      // Existing training selected
      this.requestForm.get('formationId')?.setValidators([Validators.required]);
      this.requestForm.get('customDescription')?.clearValidators();
    }
    
    this.requestForm.get('formationId')?.updateValueAndValidity();
    this.requestForm.get('customDescription')?.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.requestForm.valid) {
      this.isSubmitting = true;
      this.error = '';

      const formData = this.requestForm.value;
      
      // Clean up data based on training type
      if (formData.isCustom) {
        formData.formationId = null;
      } else {
        formData.customDescription = null;
      }

      this.annexRequestService.createRequest(formData).subscribe({
        next: (response) => {
          this.success = true;
          this.isSubmitting = false;
          
          // Redirect to annexes page after 3 seconds
          setTimeout(() => {
            this.router.navigate(['/annexes']);
          }, 3000);
        },
        error: (err) => {
          console.error('Error submitting request:', err);
          this.error = err.error?.message || 'Erreur lors de l\'envoi de la demande. Veuillez réessayer.';
          this.isSubmitting = false;
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  markFormGroupTouched(): void {
    Object.keys(this.requestForm.controls).forEach(key => {
      const control = this.requestForm.get(key);
      control?.markAsTouched();
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.requestForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.requestForm.get(fieldName);
    if (field && field.errors) {
      if (field.errors['required']) return 'Ce champ est requis.';
      if (field.errors['email']) return 'Adresse email invalide.';
      if (field.errors['minlength']) return `Minimum ${field.errors['minlength'].requiredLength} caractères.`;
      if (field.errors['min']) return `Valeur minimum: ${field.errors['min'].min}`;
      if (field.errors['max']) return `Valeur maximum: ${field.errors['max'].max}`;
    }
    return '';
  }

  getFormationsByTheme(): { [themeName: string]: FormationSummary[] } {
    return this.formations.reduce((groups, formation) => {
      const theme = formation.themeName || 'Autres';
      if (!groups[theme]) {
        groups[theme] = [];
      }
      groups[theme].push(formation);
      return groups;
    }, {} as { [themeName: string]: FormationSummary[] });
  }

  goBack(): void {
    this.router.navigate(['/annexes']);
  }
}