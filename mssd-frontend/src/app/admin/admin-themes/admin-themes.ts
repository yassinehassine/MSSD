import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ThemeService, ThemeCreateUpdateDto } from '../../services/theme.service';
import { Theme } from '../../model/annexes.model';

@Component({
  selector: 'app-admin-themes',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-themes.html',
  styleUrls: ['./admin-themes.scss']
})
export class AdminThemes implements OnInit {
  themes: Theme[] = [];
  loading = true;
  error = '';
  success = '';
  
  // Modal states
  showModal = false;
  modalTitle = '';
  editingTheme: Theme | null = null;
  
  // Form
  themeForm: FormGroup;
  
  constructor(
    private themeService: ThemeService,
    private fb: FormBuilder
  ) {
    this.themeForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      slug: ['', [Validators.required, Validators.pattern(/^[a-z0-9-]+$/)]],
      description: [''],
      iconUrl: [''],
      active: [true]
    });
  }

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.loading = true;
    this.error = '';
    this.themeService.getAllThemesAdmin().subscribe({
      next: (themes) => {
        this.themes = themes;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading themes:', err);
        this.error = 'Erreur lors du chargement des thèmes.';
        this.loading = false;
      }
    });
  }

  openCreateModal(): void {
    this.modalTitle = 'Créer un nouveau thème';
    this.editingTheme = null;
    this.themeForm.reset({
      name: '',
      slug: '',
      description: '',
      iconUrl: '',
      active: true
    });
    this.showModal = true;
  }

  openEditModal(theme: Theme): void {
    this.modalTitle = 'Modifier le thème';
    this.editingTheme = theme;
    this.themeForm.patchValue({
      name: theme.name,
      slug: theme.slug,
      description: theme.description || '',
      iconUrl: theme.iconUrl || '',
      active: theme.active
    });
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.editingTheme = null;
    this.themeForm.reset();
    this.clearMessages();
  }

  onSubmit(): void {
    if (this.themeForm.invalid) {
      Object.keys(this.themeForm.controls).forEach(key => {
        this.themeForm.get(key)?.markAsTouched();
      });
      return;
    }

    const formData: ThemeCreateUpdateDto = this.themeForm.value;
    
    if (this.editingTheme) {
      this.updateTheme(this.editingTheme.id, formData);
    } else {
      this.createTheme(formData);
    }
  }

  createTheme(data: ThemeCreateUpdateDto): void {
    this.themeService.createTheme(data).subscribe({
      next: (theme) => {
        this.success = 'Thème créé avec succès!';
        this.closeModal();
        this.loadThemes();
      },
      error: (err) => {
        console.error('Error creating theme:', err);
        this.error = err.error?.message || 'Erreur lors de la création du thème.';
      }
    });
  }

  updateTheme(id: number, data: ThemeCreateUpdateDto): void {
    this.themeService.updateTheme(id, data).subscribe({
      next: (theme) => {
        this.success = 'Thème modifié avec succès!';
        this.closeModal();
        this.loadThemes();
      },
      error: (err) => {
        console.error('Error updating theme:', err);
        this.error = err.error?.message || 'Erreur lors de la modification du thème.';
      }
    });
  }

  deleteTheme(theme: Theme): void {
    if (!confirm(`Êtes-vous sûr de vouloir supprimer le thème "${theme.name}" ?\n\nAttention: Cette action supprimera également toutes les formations associées.`)) {
      return;
    }

    this.themeService.deleteTheme(theme.id).subscribe({
      next: () => {
        this.success = 'Thème supprimé avec succès!';
        this.loadThemes();
      },
      error: (err) => {
        console.error('Error deleting theme:', err);
        this.error = err.error?.message || 'Erreur lors de la suppression du thème.';
      }
    });
  }

  onNameChange(): void {
    const name = this.themeForm.get('name')?.value;
    if (name && !this.editingTheme) {
      // Auto-generate slug from name
      const slug = name
        .toLowerCase()
        .replace(/[^a-z0-9\s-]/g, '')
        .replace(/\s+/g, '-')
        .replace(/-+/g, '-')
        .trim();
      this.themeForm.get('slug')?.setValue(slug);
    }
  }

  clearMessages(): void {
    this.error = '';
    this.success = '';
  }

  getFormationCount(theme: Theme): number {
    return theme.formations?.length || 0;
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.themeForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(fieldName: string): string {
    const field = this.themeForm.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) return `${fieldName} est requis.`;
      if (field.errors['minlength']) return `${fieldName} doit contenir au moins ${field.errors['minlength'].requiredLength} caractères.`;
      if (field.errors['pattern']) return `${fieldName} contient des caractères invalides.`;
    }
    return '';
  }
}