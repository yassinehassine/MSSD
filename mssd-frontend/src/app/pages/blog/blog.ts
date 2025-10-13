import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { BlogService, BlogPost } from '../../services/blog.service';
import { PaginationComponent } from '../../shared/pagination/pagination.component';

@Component({
  selector: 'app-blog',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, PaginationComponent],
  templateUrl: './blog.html',
  styleUrl: './blog.scss'
})
export class Blog implements OnInit {
  blogs: BlogPost[] = [];
  filteredBlogs: BlogPost[] = [];
  paginatedBlogs: BlogPost[] = [];
  loading = true;
  error = '';
  
  // Search and Filter
  searchTerm = '';
  sortBy = 'date'; // 'date', 'title', 'type'
  filterType = 'all'; // 'all', 'video', 'image', 'text'
  
  // Pagination
  currentPage = 1;
  itemsPerPage = 6; // 2 rows of 3
  totalItems = 0;
  
  constructor(
    private blogService: BlogService,
    private router: Router
  ) {}
  
  ngOnInit() {
    this.loadBlogs();
  }
  
  loadBlogs() {
    this.loading = true;
    this.error = '';
    
    this.blogService.getAllActiveBlogs().subscribe({
      next: (blogs) => {
        this.blogs = blogs;
        this.applyFiltersAndSearch();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des articles.';
        this.loading = false;
        console.error('Error loading blogs:', err);
      }
    });
  }
  
  applyFiltersAndSearch() {
    let filtered = [...this.blogs];
    
    // Apply search filter
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(blog => 
        blog.title.toLowerCase().includes(term) || 
        blog.description.toLowerCase().includes(term)
      );
    }
    
    // Apply type filter
    if (this.filterType !== 'all') {
      filtered = filtered.filter(blog => {
        switch (this.filterType) {
          case 'video':
            return !!blog.youtubeUrl;
          case 'image':
            return !!blog.imageUrl && !blog.youtubeUrl;
          case 'text':
            return !blog.youtubeUrl && !blog.imageUrl;
          default:
            return true;
        }
      });
    }
    
    // Apply sorting
    filtered.sort((a, b) => {
      switch (this.sortBy) {
        case 'title':
          return a.title.localeCompare(b.title);
        case 'date':
          return new Date(b.publishDate!).getTime() - new Date(a.publishDate!).getTime();
        case 'type':
          const getType = (blog: BlogPost) => {
            if (blog.youtubeUrl) return 'video';
            if (blog.imageUrl) return 'image';
            return 'text';
          };
          return getType(a).localeCompare(getType(b));
        default:
          return 0;
      }
    });
    
    this.filteredBlogs = filtered;
    this.totalItems = this.filteredBlogs.length;
    this.currentPage = 1; // Reset to first page
    this.updatePaginatedBlogs();
  }
  
  updatePaginatedBlogs() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedBlogs = this.filteredBlogs.slice(startIndex, endIndex);
  }
  
  onSearchChange() {
    this.applyFiltersAndSearch();
  }
  
  onSortChange() {
    this.applyFiltersAndSearch();
  }
  
  onFilterChange() {
    this.applyFiltersAndSearch();
  }
  
  onPageChange(page: number) {
    this.currentPage = page;
    this.updatePaginatedBlogs();
    // Scroll to top of blog section
    const blogSection = document.getElementById('blog-section');
    if (blogSection) {
      blogSection.scrollIntoView({ behavior: 'smooth' });
    }
  }
  
  getImageUrl(blog: BlogPost): string {
    if (blog.imageUrl) {
      // If URL starts with http/https, use as-is
      if (blog.imageUrl.startsWith('http')) {
        return blog.imageUrl;
      }
      // If URL starts with uploads/, use the file API
      if (blog.imageUrl.startsWith('uploads/')) {
        const filename = blog.imageUrl.replace('uploads/', '');
        return `http://localhost:8080/api/files/${filename}`;
      }
      // Otherwise, assume it's just a filename
      return `http://localhost:8080/api/files/${blog.imageUrl}`;
    }
    return 'assets/img/blog/blog-default.jpg';
  }
  
  getYouTubeEmbedUrl(youtubeUrl: string): string | null {
    return this.blogService.getYouTubeEmbedUrl(youtubeUrl);
  }
  
  formatDate(dateString: string): string {
    return this.blogService.formatDate(dateString);
  }
  
  hasMedia(blog: BlogPost): boolean {
    return !!(blog.youtubeUrl || blog.imageUrl);
  }
  
  onBlogClick(blog: BlogPost) {
    // Navigate to blog detail page
    this.router.navigate(['/blog', blog.id]);
  }
  
  openYouTubeVideo(youtubeUrl: string, event?: Event) {
    if (event) {
      event.stopPropagation(); // Prevent card click when button is clicked
    }
    
    // Extract YouTube video ID and open in new tab
    const videoId = this.extractYouTubeId(youtubeUrl);
    if (videoId) {
      const fullUrl = `https://www.youtube.com/watch?v=${videoId}`;
      window.open(fullUrl, '_blank');
    } else {
      // Fallback: open the original URL
      window.open(youtubeUrl, '_blank');
    }
  }
  
  private extractYouTubeId(url: string): string | null {
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
    const match = url.match(regExp);
    return (match && match[2].length === 11) ? match[2] : null;
  }
}
