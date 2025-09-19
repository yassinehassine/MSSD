import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ThemeService } from '../services/theme.service';
import { FileUploadService } from '../services/file-upload.service';
import { Theme, ThemeRequest } from '../model/theme.model';

@Component({
  selector: 'app-admin-themes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-themes.html',
  styleUrl: './admin-contacts.scss'
})
export class AdminThemes implements OnInit {
  themes: Theme[] = [];
  isLoading = true;
  error = '';

  // For add/edit
  form: ThemeRequest = this.emptyForm();
  editingId: number | null = null;
  formError = '';
  formSuccess = '';
  imageUploading = false;

  constructor(
    private themeService: ThemeService, 
    private fileUploadService: FileUploadService
  ) {}

  ngOnInit() {
    this.loadThemes();
  }

  emptyForm(): ThemeRequest {
    return {
      name: '',
      slug: '',
      description: '',
      imageUrl: '',
      published: false
    };
  }

  loadThemes() {
    this.isLoading = true;
    this.themeService.getAllThemes().subscribe({
      next: (data) => {
        this.themes = data;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Failed to load themes.';
        this.isLoading = false;
      }
    });
  }

  startAdd() {
    this.editingId = null;
    this.form = this.emptyForm();
    this.formError = '';
    this.formSuccess = '';
  }

  startEdit(theme: Theme) {
    this.editingId = theme.id;
    this.form = {
      name: theme.name,
      slug: theme.slug,
      description: theme.description || '',
      imageUrl: theme.imageUrl || '',
      published: theme.published
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
    
    if (!this.form.name || !this.form.slug) {
      this.formError = 'Please fill all required fields.';
      return;
    }
    
    if (this.editingId) {
      this.themeService.updateTheme(this.editingId, this.form).subscribe({
        next: () => {
          this.formSuccess = 'Theme updated!';
          this.loadThemes();
          this.editingId = null;
          this.form = this.emptyForm();
        },
        error: () => {
          this.formError = 'Failed to update theme.';
        }
      });
    } else {
      this.themeService.createTheme(this.form).subscribe({
        next: () => {
          this.formSuccess = 'Theme added!';
          this.loadThemes();
          this.form = this.emptyForm();
        },
        error: () => {
          this.formError = 'Failed to add theme.';
        }
      });
    }
  }

  deleteTheme(id: number) {
    if (!confirm('Delete this theme?')) return;
    this.themeService.deleteTheme(id).subscribe({
      next: () => this.loadThemes(),
      error: () => alert('Failed to delete theme.')
    });
  }

  cancelEdit() {
    this.editingId = null;
    this.form = this.emptyForm();
    this.formError = '';
    this.formSuccess = '';
  }
}