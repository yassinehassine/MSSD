import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FormationService, Formation, FormationRequest } from '../services/formation.service';
import { FileUploadService } from '../services/file-upload.service';
import { ThemeService } from '../services/theme.service';
import { Theme } from '../model/annexes.model';

@Component({
  selector: 'app-admin-formations',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-formations.html',
  styleUrl: './admin-contacts.scss'
})
export class AdminFormations implements OnInit {
  formations: Formation[] = [];
  themes: Theme[] = [];
  isLoading = true;
  error = '';

  // For add/edit
  form: FormationRequest = this.emptyForm();
  editingId: number | null = null;
  formError = '';
  formSuccess = '';
  imageUploading = false;

  // For details modal
  selectedFormation: Formation | null = null;
  showDetailsModal = false;

  constructor(
    private formationService: FormationService, 
    private fileUploadService: FileUploadService,
    private themeService: ThemeService
  ) {}

  ngOnInit() {
    this.loadFormations();
    this.loadThemes();
  }

  emptyForm(): FormationRequest {
    return {
      title: '',
      slug: '',
      description: '',
      category: '',
      price: 0,
      duration: '',
      imageUrl: '',
      level: 'BEGINNER',
      published: false,
      themeId: undefined
    };
  }

  loadFormations() {
    this.isLoading = true;
    this.formationService.getAllFormations().subscribe({
      next: (data) => {
        this.formations = data;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Failed to load formations.';
        this.isLoading = false;
      }
    });
  }

  loadThemes() {
    this.themeService.getAllThemes().subscribe({
      next: (themes) => {
        this.themes = themes;
      },
      error: (err) => {
        console.error('Failed to load themes:', err);
      }
    });
  }

  startAdd() {
    this.editingId = null;
    this.form = this.emptyForm();
    this.formError = '';
    this.formSuccess = '';
  }

  startEdit(formation: Formation) {
    this.editingId = formation.id;
    this.form = {
      title: formation.title,
      slug: formation.slug,
      description: formation.description,
      category: formation.category,
      price: formation.price,
      duration: formation.duration,
      imageUrl: formation.imageUrl,
      level: formation.level,
      published: formation.published,
      themeId: formation.theme?.id
    };
    this.formError = '';
    this.formSuccess = '';
  }

  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.imageUploading = true;
      this.fileUploadService.upload(file).subscribe({
        next: (url) => {
          this.form.imageUrl = url;
          this.imageUploading = false;
        },
        error: () => {
          this.formError = 'Image upload failed.';
          this.imageUploading = false;
        }
      });
    }
  }

  submitForm(event: Event) {
    event.preventDefault();
    this.formError = '';
    this.formSuccess = '';
    if (!this.form.title || !this.form.slug || !this.form.category || !this.form.price || !this.form.duration || !this.form.level) {
      this.formError = 'Please fill all required fields.';
      return;
    }
    if (this.editingId) {
      this.formationService.updateFormation(this.editingId, this.form).subscribe({
        next: () => {
          this.formSuccess = 'Formation updated!';
          this.loadFormations();
          this.editingId = null;
          this.form = this.emptyForm();
        },
        error: () => {
          this.formError = 'Failed to update formation.';
        }
      });
    } else {
      this.formationService.createFormation(this.form).subscribe({
        next: () => {
          this.formSuccess = 'Formation added!';
          this.loadFormations();
          this.form = this.emptyForm();
        },
        error: () => {
          this.formError = 'Failed to add formation.';
        }
      });
    }
  }

  deleteFormation(id: number) {
    if (!confirm('Delete this formation?')) return;
    this.formationService.deleteFormation(id).subscribe({
      next: () => this.loadFormations(),
      error: () => alert('Failed to delete formation.')
    });
  }

  cancelEdit() {
    this.editingId = null;
    this.form = this.emptyForm();
    this.formError = '';
    this.formSuccess = '';
  }

  viewDetails(formation: Formation) {
    this.selectedFormation = formation;
    this.showDetailsModal = true;
  }

  closeDetailsModal() {
    this.showDetailsModal = false;
    this.selectedFormation = null;
  }

  editFromModal(formation: Formation) {
    this.closeDetailsModal();
    this.startEdit(formation);
  }
} 