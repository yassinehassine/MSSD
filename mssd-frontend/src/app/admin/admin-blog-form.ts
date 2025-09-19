import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { Blog, BlogRequest } from '../model/blog.model';
import { BlogService } from '../services/blog.service';
import { FileUploadService } from '../services/file-upload.service';

@Component({
  selector: 'app-admin-blog-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './admin-blog-form.html',
  styleUrl: './admin-blog-form.scss'
})
export class AdminBlogForm implements OnInit {
  blog: BlogRequest = this.getEmptyBlog();
  isEditing = false;
  isLoading = false;
  isSaving = false;
  blogId: number | null = null;
  
  // Form validation
  titleTouched = false;
  slugTouched = false;
  contentTouched = false;
  isSlugAvailable = true;
  slugCheckInProgress = false;
  
  // Image handling
  selectedFile: File | null = null;
  isUploading = false;
  imagePreview: string | null = null;
  availableImages: any[] = [];
  showImageLibrary = false;
  
  // Categories and authors for dropdowns
  categories: string[] = [];
  authors: string[] = [];
  
  // Content editor
  showContentPreview = false;

  constructor(
    private blogService: BlogService,
    private fileUploadService: FileUploadService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  // Make blogService accessible to template
  get blogServicePublic() {
    return this.blogService;
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.blogId = +params['id'];
        this.isEditing = true;
        this.loadBlog();
      }
    });
    
    this.loadCategories();
    this.loadAuthors();
    this.loadAvailableImages();
  }

  loadBlog(): void {
    if (this.blogId) {
      this.isLoading = true;
      this.blogService.getBlogById(this.blogId).subscribe({
        next: (blog) => {
          this.blog = {
            title: blog.title,
            slug: blog.slug,
            excerpt: blog.excerpt,
            content: blog.content,
            imageUrl: blog.imageUrl,
            author: blog.author,
            category: blog.category,
            tags: blog.tags,
            published: blog.published
          };
          this.imagePreview = blog.imageUrl ? this.blogService.getImageUrl(blog.imageUrl) : null;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading blog:', error);
          this.isLoading = false;
          this.router.navigate(['/admin/blog']);
        }
      });
    }
  }

  loadCategories(): void {
    this.blogService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        console.error('Error loading categories:', error);
      }
    });
  }

  loadAuthors(): void {
    this.blogService.getAuthors().subscribe({
      next: (authors) => {
        this.authors = authors;
      },
      error: (error) => {
        console.error('Error loading authors:', error);
      }
    });
  }

  loadAvailableImages(): void {
    this.blogService.getAvailableImages().subscribe({
      next: (images) => {
        this.availableImages = images;
      },
      error: (error) => {
        console.error('Error loading available images:', error);
      }
    });
  }

  onTitleChange(): void {
    this.titleTouched = true;
    
    // Auto-generate slug if not manually set
    if (!this.slugTouched && this.blog.title) {
      this.blog.slug = this.blogService.generateSlug(this.blog.title);
      this.checkSlugAvailability();
    }
    
    // Auto-generate excerpt if not set
    if (!this.blog.excerpt && this.blog.content) {
      this.blog.excerpt = this.blogService.generateExcerpt(this.blog.content);
    }
  }

  onSlugChange(): void {
    this.slugTouched = true;
    this.checkSlugAvailability();
  }

  onContentChange(): void {
    this.contentTouched = true;
    
    // Auto-generate excerpt if not set
    if (!this.blog.excerpt && this.blog.content) {
      this.blog.excerpt = this.blogService.generateExcerpt(this.blog.content);
    }
  }

  checkSlugAvailability(): void {
    if (this.blog.slug && this.blog.slug.trim()) {
      this.slugCheckInProgress = true;
      this.blogService.checkSlugAvailability(this.blog.slug, this.blogId || undefined).subscribe({
        next: (response) => {
          this.isSlugAvailable = response.available;
          this.slugCheckInProgress = false;
        },
        error: (error) => {
          console.error('Error checking slug availability:', error);
          this.slugCheckInProgress = false;
        }
      });
    }
  }

  // Image handling methods
  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      
      // Create preview
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreview = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  uploadImage(): void {
    if (this.selectedFile) {
      this.isUploading = true;
      this.blogService.uploadFile(this.selectedFile).subscribe({
        next: (response: any) => {
          this.blog.imageUrl = response.url || response.filename;
          this.imagePreview = this.blogService.getImageUrl(this.blog.imageUrl || '');
          this.selectedFile = null;
          this.isUploading = false;
          console.log('Image uploaded successfully');
        },
        error: (error: any) => {
          console.error('Error uploading image:', error);
          this.isUploading = false;
        }
      });
    }
  }

  selectFromLibrary(image: any): void {
    this.blog.imageUrl = image.filename || image.url;
    this.imagePreview = this.blogService.getImageUrl(this.blog.imageUrl || '');
    this.showImageLibrary = false;
  }

  removeImage(): void {
    this.blog.imageUrl = '';
    this.imagePreview = null;
    this.selectedFile = null;
  }

  // Form validation
  isFormValid(): boolean {
    return !!(
      this.blog.title?.trim() &&
      this.blog.slug?.trim() &&
      this.blog.content?.trim() &&
      this.isSlugAvailable &&
      !this.slugCheckInProgress
    );
  }

  getFieldError(fieldName: string): string | null {
    switch (fieldName) {
      case 'title':
        if (this.titleTouched && !this.blog.title?.trim()) {
          return 'Title is required';
        }
        break;
      case 'slug':
        if (this.slugTouched && !this.blog.slug?.trim()) {
          return 'Slug is required';
        }
        if (this.blog.slug && !this.isSlugAvailable) {
          return 'This slug is already taken';
        }
        break;
      case 'content':
        if (this.contentTouched && !this.blog.content?.trim()) {
          return 'Content is required';
        }
        break;
    }
    return null;
  }

  hasFieldError(fieldName: string): boolean {
    return !!this.getFieldError(fieldName);
  }

  // Form submission
  onSubmit(): void {
    this.titleTouched = true;
    this.slugTouched = true;
    this.contentTouched = true;

    if (!this.isFormValid()) {
      console.log('Form is invalid');
      return;
    }

    // Upload image first if needed
    if (this.selectedFile) {
      this.uploadImage();
      // The save will be called after upload completes
      setTimeout(() => this.saveBlog(), 1000);
    } else {
      this.saveBlog();
    }
  }

  saveBlog(): void {
    this.isSaving = true;

    const operation = this.isEditing && this.blogId
      ? this.blogService.updateBlog(this.blogId, this.blog)
      : this.blogService.createBlog(this.blog);

    operation.subscribe({
      next: (savedBlog) => {
        console.log('Blog saved successfully:', savedBlog);
        this.isSaving = false;
        this.router.navigate(['/admin/blog']);
      },
      error: (error) => {
        console.error('Error saving blog:', error);
        this.isSaving = false;
      }
    });
  }

  // Utility methods
  toggleContentPreview(): void {
    this.showContentPreview = !this.showContentPreview;
  }

  getEmptyBlog(): BlogRequest {
    return {
      title: '',
      slug: '',
      excerpt: '',
      content: '',
      imageUrl: '',
      author: '',
      category: 'General',
      tags: '',
      published: false
    };
  }

  cancel(): void {
    this.router.navigate(['/admin/blog']);
  }

  // Content formatting helpers
  insertMarkdown(tag: string): void {
    const textarea = document.getElementById('content') as HTMLTextAreaElement;
    if (textarea) {
      const start = textarea.selectionStart;
      const end = textarea.selectionEnd;
      const selectedText = textarea.value.substring(start, end);
      
      let replacement = '';
      switch (tag) {
        case 'bold':
          replacement = `**${selectedText}**`;
          break;
        case 'italic':
          replacement = `*${selectedText}*`;
          break;
        case 'link':
          replacement = `[${selectedText}](url)`;
          break;
        case 'image':
          replacement = `![${selectedText}](image-url)`;
          break;
        case 'h1':
          replacement = `# ${selectedText}`;
          break;
        case 'h2':
          replacement = `## ${selectedText}`;
          break;
        case 'h3':
          replacement = `### ${selectedText}`;
          break;
      }
      
      this.blog.content = 
        textarea.value.substring(0, start) + 
        replacement + 
        textarea.value.substring(end);
    }
  }
}