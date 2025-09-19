import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Blog, BlogPageResponse } from '../model/blog.model';
import { BlogService } from '../services/blog.service';

@Component({
  selector: 'app-admin-blog',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './admin-blog.html',
  styleUrl: './admin-blog.scss'
})
export class AdminBlog implements OnInit {
  blogs: Blog[] = [];
  categories: string[] = [];
  authors: string[] = [];
  isLoading = false;
  showDeleteConfirm = false;
  blogToDelete: Blog | null = null;
  
  // Pagination
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  
  // Search and filters
  searchKeyword = '';
  selectedCategory = '';
  selectedAuthor = '';
  selectedStatus = ''; // all, published, unpublished

  constructor(private blogService: BlogService) {}

  ngOnInit(): void {
    this.loadBlogs();
    this.loadCategories();
    this.loadAuthors();
  }

  loadBlogs(): void {
    this.isLoading = true;
    
    if (this.searchKeyword.trim()) {
      this.searchBlogs();
    } else {
      this.blogService.getAllBlogsWithPagination(this.currentPage, this.pageSize).subscribe({
        next: (response: BlogPageResponse) => {
          this.blogs = this.filterBlogs(response.content);
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading blogs:', error);
          this.isLoading = false;
        }
      });
    }
  }

  searchBlogs(): void {
    this.isLoading = true;
    this.blogService.searchAllBlogs(this.searchKeyword, this.currentPage, this.pageSize).subscribe({
      next: (response: BlogPageResponse) => {
        this.blogs = this.filterBlogs(response.content);
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error searching blogs:', error);
        this.isLoading = false;
      }
    });
  }

  filterBlogs(blogs: Blog[]): Blog[] {
    let filtered = blogs;
    
    // Filter by category
    if (this.selectedCategory) {
      filtered = filtered.filter(blog => blog.category === this.selectedCategory);
    }
    
    // Filter by author
    if (this.selectedAuthor) {
      filtered = filtered.filter(blog => blog.author === this.selectedAuthor);
    }
    
    // Filter by status
    if (this.selectedStatus === 'published') {
      filtered = filtered.filter(blog => blog.published === true);
    } else if (this.selectedStatus === 'unpublished') {
      filtered = filtered.filter(blog => blog.published === false);
    }
    
    return filtered;
  }

  onSearch(): void {
    this.currentPage = 0;
    this.loadBlogs();
  }

  onFilterChange(): void {
    this.currentPage = 0;
    this.loadBlogs();
  }

  clearFilters(): void {
    this.searchKeyword = '';
    this.selectedCategory = '';
    this.selectedAuthor = '';
    this.selectedStatus = '';
    this.currentPage = 0;
    this.loadBlogs();
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

  // Pagination methods
  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadBlogs();
    }
  }

  goToFirstPage(): void {
    this.goToPage(0);
  }

  goToLastPage(): void {
    this.goToPage(this.totalPages - 1);
  }

  goToPreviousPage(): void {
    if (this.currentPage > 0) {
      this.goToPage(this.currentPage - 1);
    }
  }

  goToNextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.goToPage(this.currentPage + 1);
    }
  }

  getPageNumbers(): number[] {
    const maxPages = 5;
    const pages: number[] = [];
    const start = Math.max(0, this.currentPage - Math.floor(maxPages / 2));
    const end = Math.min(this.totalPages, start + maxPages);
    
    for (let i = start; i < end; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  // Blog actions
  togglePublishStatus(blog: Blog): void {
    if (blog.published) {
      this.unpublishBlog(blog);
    } else {
      this.publishBlog(blog);
    }
  }

  publishBlog(blog: Blog): void {
    if (blog.id) {
      this.blogService.publishBlog(blog.id).subscribe({
        next: (updatedBlog) => {
          const index = this.blogs.findIndex(b => b.id === blog.id);
          if (index !== -1) {
            this.blogs[index] = updatedBlog;
          }
          console.log('Blog published successfully');
        },
        error: (error) => {
          console.error('Error publishing blog:', error);
        }
      });
    }
  }

  unpublishBlog(blog: Blog): void {
    if (blog.id) {
      this.blogService.unpublishBlog(blog.id).subscribe({
        next: (updatedBlog) => {
          const index = this.blogs.findIndex(b => b.id === blog.id);
          if (index !== -1) {
            this.blogs[index] = updatedBlog;
          }
          console.log('Blog unpublished successfully');
        },
        error: (error) => {
          console.error('Error unpublishing blog:', error);
        }
      });
    }
  }

  confirmDelete(blog: Blog): void {
    this.blogToDelete = blog;
    this.showDeleteConfirm = true;
  }

  cancelDelete(): void {
    this.blogToDelete = null;
    this.showDeleteConfirm = false;
  }

  deleteBlog(): void {
    if (this.blogToDelete && this.blogToDelete.id) {
      this.blogService.deleteBlog(this.blogToDelete.id).subscribe({
        next: () => {
          this.blogs = this.blogs.filter(b => b.id !== this.blogToDelete!.id);
          this.blogToDelete = null;
          this.showDeleteConfirm = false;
          console.log('Blog deleted successfully');
          
          // Reload if current page is empty
          if (this.blogs.length === 0 && this.currentPage > 0) {
            this.currentPage--;
            this.loadBlogs();
          }
        },
        error: (error) => {
          console.error('Error deleting blog:', error);
          this.blogToDelete = null;
          this.showDeleteConfirm = false;
        }
      });
    }
  }

  // Utility methods
  getImageUrl(imagePath: string): string {
    return this.blogService.getImageUrl(imagePath);
  }

  formatDate(dateString: string | undefined): string {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString();
  }

  getStatusBadgeClass(blog: Blog): string {
    if (blog.published) {
      return 'badge bg-success';
    } else {
      return 'badge bg-warning';
    }
  }

  getStatusText(blog: Blog): string {
    if (blog.published) {
      return 'Published';
    } else {
      return 'Draft';
    }
  }

  truncateText(text: string | undefined, maxLength: number = 100): string {
    if (!text) return '';
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
  }

  getMaxElement(): number {
    return Math.min((this.currentPage + 1) * this.pageSize, this.totalElements);
  }
}