import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BlogService, BlogPost, BlogStats } from '../../services/blog.service';

@Component({
  selector: 'app-admin-blog-management',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admin-blog-management.html',
  styleUrl: './admin-blog-management.scss'
})
export class AdminBlogManagement implements OnInit {
  blogs: BlogPost[] = [];
  filteredBlogs: BlogPost[] = [];
  stats: BlogStats = { total: 0, active: 0, inactive: 0 };
  
  // Form variables
  selectedBlog: BlogPost | null = null;
  isEditing = false;
  showForm = false;
  selectedImageFile: File | null = null;
  
  // Form model
  blogForm: BlogPost = {
    title: '',
    description: '',
    youtubeUrl: '',
    imageUrl: '',
    publishDate: '',
    active: true
  };
  
  // UI state
  loading = false;
  error = '';
  success = '';
  searchTerm = '';
  statusFilter = 'all'; // all, active, inactive
  
  constructor(private blogService: BlogService) {}

  ngOnInit() {
    this.loadBlogs();
    this.loadStats();
  }

  loadBlogs() {
    this.loading = true;
    this.error = '';
    
    this.blogService.getAllBlogsForAdmin().subscribe({
      next: (blogs) => {
        this.blogs = blogs;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des articles de blog.';
        this.loading = false;
        console.error('Error loading blogs:', err);
      }
    });
  }

  loadStats() {
    this.blogService.getBlogStats().subscribe({
      next: (stats) => {
        this.stats = stats;
      },
      error: (err) => {
        console.error('Error loading blog stats:', err);
      }
    });
  }

  applyFilters() {
    let filtered = [...this.blogs];
    
    // Apply search filter
    if (this.searchTerm.trim()) {
      filtered = filtered.filter(blog => 
        blog.title.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        blog.description.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
    
    // Apply status filter
    if (this.statusFilter === 'active') {
      filtered = filtered.filter(blog => blog.active);
    } else if (this.statusFilter === 'inactive') {
      filtered = filtered.filter(blog => !blog.active);
    }
    
    this.filteredBlogs = filtered;
  }

  onSearchChange() {
    this.applyFilters();
  }

  onStatusFilterChange() {
    this.applyFilters();
  }

  showCreateForm() {
    this.selectedBlog = null;
    this.isEditing = false;
    this.showForm = true;
    this.resetForm();
  }

  showEditForm(blog: BlogPost) {
    this.selectedBlog = blog;
    this.isEditing = true;
    this.showForm = true;
    this.populateForm(blog);
  }

  hideForm() {
    this.showForm = false;
    this.selectedBlog = null;
    this.isEditing = false;
    this.resetForm();
    this.clearMessages();
  }

  resetForm() {
    this.blogForm = {
      title: '',
      description: '',
      youtubeUrl: '',
      imageUrl: '',
      publishDate: '',
      active: true
    };
    this.selectedImageFile = null;
  }

  populateForm(blog: BlogPost) {
    this.blogForm = {
      title: blog.title,
      description: blog.description,
      youtubeUrl: blog.youtubeUrl || '',
      imageUrl: blog.imageUrl || '',
      publishDate: blog.publishDate ? this.blogService.formatDateForInput(blog.publishDate) : '',
      active: blog.active
    };
  }

  onImageSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      // Validate file type
      const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'];
      if (!allowedTypes.includes(file.type)) {
        this.error = 'Format d\'image non supporté. Utilisez JPG, PNG, GIF ou WebP.';
        return;
      }
      
      // Validate file size (max 5MB)
      const maxSize = 5 * 1024 * 1024; // 5MB
      if (file.size > maxSize) {
        this.error = 'L\'image est trop volumineuse. Taille maximum: 5MB.';
        return;
      }
      
      this.selectedImageFile = file;
      this.clearMessages(); // Clear any previous error messages
      this.success = `Image sélectionnée: ${file.name}`;
    }
  }

  saveBlog() {
    if (!this.validateForm()) {
      return;
    }

    this.loading = true;
    this.clearMessages();

    const blogData = { ...this.blogForm };
    
    // Convert publishDate if provided
    if (blogData.publishDate) {
      blogData.publishDate = new Date(blogData.publishDate).toISOString();
    }

    if (this.isEditing && this.selectedBlog) {
      // Update existing blog
      if (this.selectedImageFile) {
        this.blogService.updateBlogWithImage(this.selectedBlog.id!, blogData, this.selectedImageFile).subscribe({
          next: (updatedBlog) => {
            this.success = 'Article mis à jour avec succès!';
            this.loadBlogs();
            this.loadStats();
            this.hideForm();
            this.loading = false;
          },
          error: (err) => {
            this.error = 'Erreur lors de la mise à jour de l\'article.';
            this.loading = false;
            console.error('Error updating blog:', err);
          }
        });
      } else {
        this.blogService.updateBlog(this.selectedBlog.id!, blogData).subscribe({
          next: (updatedBlog) => {
            this.success = 'Article mis à jour avec succès!';
            this.loadBlogs();
            this.loadStats();
            this.hideForm();
            this.loading = false;
          },
          error: (err) => {
            this.error = 'Erreur lors de la mise à jour de l\'article.';
            this.loading = false;
            console.error('Error updating blog:', err);
          }
        });
      }
    } else {
      // Create new blog
      if (this.selectedImageFile) {
        this.blogService.createBlogWithImage(blogData, this.selectedImageFile).subscribe({
          next: (newBlog) => {
            this.success = 'Article créé avec succès!';
            this.loadBlogs();
            this.loadStats();
            this.hideForm();
            this.loading = false;
          },
          error: (err) => {
            this.error = 'Erreur lors de la création de l\'article.';
            this.loading = false;
            console.error('Error creating blog:', err);
          }
        });
      } else {
        this.blogService.createBlog(blogData).subscribe({
          next: (newBlog) => {
            this.success = 'Article créé avec succès!';
            this.loadBlogs();
            this.loadStats();
            this.hideForm();
            this.loading = false;
          },
          error: (err) => {
            this.error = 'Erreur lors de la création de l\'article.';
            this.loading = false;
            console.error('Error creating blog:', err);
          }
        });
      }
    }
  }

  deleteBlog(blog: BlogPost) {
    if (!confirm(`Êtes-vous sûr de vouloir supprimer l'article "${blog.title}" ?`)) {
      return;
    }

    this.loading = true;
    this.clearMessages();

    this.blogService.deleteBlog(blog.id!).subscribe({
      next: () => {
        this.success = 'Article supprimé avec succès!';
        this.loadBlogs();
        this.loadStats();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors de la suppression de l\'article.';
        this.loading = false;
        console.error('Error deleting blog:', err);
      }
    });
  }

  toggleBlogStatus(blog: BlogPost) {
    this.loading = true;
    this.clearMessages();

    this.blogService.toggleBlogStatus(blog.id!).subscribe({
      next: (updatedBlog) => {
        const status = updatedBlog.active ? 'activé' : 'désactivé';
        this.success = `Article ${status} avec succès!`;
        this.loadBlogs();
        this.loadStats();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du changement de statut.';
        this.loading = false;
        console.error('Error toggling blog status:', err);
      }
    });
  }

  validateForm(): boolean {
    if (!this.blogForm.title.trim()) {
      this.error = 'Le titre est requis.';
      return false;
    }
    
    if (!this.blogForm.description.trim()) {
      this.error = 'La description est requise.';
      return false;
    }
    
    return true;
  }

  clearMessages() {
    this.error = '';
    this.success = '';
  }

  getImageUrl(blog: BlogPost): string {
    if (blog.imageUrl) {
      if (blog.imageUrl.startsWith('http')) {
        return blog.imageUrl;
      }
      // Strip uploads/ prefix if present
      const path = blog.imageUrl.startsWith('uploads/') ? blog.imageUrl.substring(8) : blog.imageUrl;
      return `/api/files/${path}`;
    }
    return 'assets/img/blog/blog-default.jpg';
  }

  getYouTubeThumbnail(youtubeUrl: string): string | null {
    return this.blogService.getYouTubeThumbnail(youtubeUrl);
  }

  formatDate(dateString: string): string {
    return this.blogService.formatDate(dateString);
  }
}