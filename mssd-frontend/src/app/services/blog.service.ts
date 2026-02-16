import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface BlogPost {
  id?: number;
  title: string;
  description: string;
  youtubeUrl?: string;
  imageUrl?: string;
  publishDate?: string;
  active: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface BlogStats {
  total: number;
  active: number;
  inactive: number;
}

@Injectable({
  providedIn: 'root'
})
export class BlogService {
  private apiUrl = '/api/blogs';

  constructor(private http: HttpClient) {}

  // Get all active blogs for public display
  getAllActiveBlogs(): Observable<BlogPost[]> {
    return this.http.get<BlogPost[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  // Get all blogs for admin (including inactive)
  getAllBlogsForAdmin(): Observable<BlogPost[]> {
    return this.http.get<BlogPost[]>(`${this.apiUrl}/admin`)
      .pipe(catchError(this.handleError));
  }

  // Get blog by ID
  getBlogById(id: number): Observable<BlogPost> {
    return this.http.get<BlogPost>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  // Create new blog post
  createBlog(blog: BlogPost): Observable<BlogPost> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<BlogPost>(this.apiUrl, blog, { headers })
      .pipe(catchError(this.handleError));
  }

  // Create blog with image upload
  createBlogWithImage(blog: BlogPost, imageFile?: File): Observable<BlogPost> {
    const formData = new FormData();
    formData.append('title', blog.title);
    formData.append('description', blog.description);
    formData.append('active', blog.active.toString());
    
    if (blog.youtubeUrl) {
      formData.append('youtubeUrl', blog.youtubeUrl);
    }
    
    if (blog.publishDate) {
      formData.append('publishDate', blog.publishDate);
    }
    
    if (imageFile) {
      formData.append('image', imageFile);
    }

    return this.http.post<BlogPost>(`${this.apiUrl}/with-image`, formData)
      .pipe(catchError(this.handleError));
  }

  // Update existing blog post
  updateBlog(id: number, blog: BlogPost): Observable<BlogPost> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<BlogPost>(`${this.apiUrl}/${id}`, blog, { headers })
      .pipe(catchError(this.handleError));
  }

  // Update blog with image upload
  updateBlogWithImage(id: number, blog: BlogPost, imageFile?: File): Observable<BlogPost> {
    const formData = new FormData();
    formData.append('title', blog.title);
    formData.append('description', blog.description);
    formData.append('active', blog.active.toString());
    
    if (blog.youtubeUrl) {
      formData.append('youtubeUrl', blog.youtubeUrl);
    }
    
    if (blog.publishDate) {
      formData.append('publishDate', blog.publishDate);
    }
    
    if (imageFile) {
      formData.append('image', imageFile);
    }

    return this.http.put<BlogPost>(`${this.apiUrl}/${id}/with-image`, formData)
      .pipe(catchError(this.handleError));
  }

  // Delete blog post
  deleteBlog(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  // Toggle blog active status
  toggleBlogStatus(id: number): Observable<BlogPost> {
    return this.http.patch<BlogPost>(`${this.apiUrl}/${id}/toggle-status`, {})
      .pipe(catchError(this.handleError));
  }

  // Search blogs
  searchBlogs(searchTerm: string): Observable<BlogPost[]> {
    const params = searchTerm ? `?q=${encodeURIComponent(searchTerm)}` : '';
    return this.http.get<BlogPost[]>(`${this.apiUrl}/search${params}`)
      .pipe(catchError(this.handleError));
  }

  // Get recent blogs
  getRecentBlogs(): Observable<BlogPost[]> {
    return this.http.get<BlogPost[]>(`${this.apiUrl}/recent`)
      .pipe(catchError(this.handleError));
  }

  // Get blog statistics
  getBlogStats(): Observable<BlogStats> {
    return this.http.get<BlogStats>(`${this.apiUrl}/stats`)
      .pipe(catchError(this.handleError));
  }

  // Helper method to extract YouTube video ID from URL
  getYouTubeVideoId(url: string): string | null {
    if (!url) return null;
    
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|&v=)([^#&?]*).*/;
    const match = url.match(regExp);
    
    return (match && match[2].length === 11) ? match[2] : null;
  }

  // Helper method to get YouTube embed URL
  getYouTubeEmbedUrl(url: string): string | null {
    const videoId = this.getYouTubeVideoId(url);
    return videoId ? `https://www.youtube.com/embed/${videoId}` : null;
  }

  // Helper method to get YouTube thumbnail URL
  getYouTubeThumbnail(url: string): string | null {
    const videoId = this.getYouTubeVideoId(url);
    return videoId ? `https://img.youtube.com/vi/${videoId}/maxresdefault.jpg` : null;
  }

  // Format date for display
  formatDate(dateString: string): string {
    if (!dateString) return '';
    
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  // Format date for datetime-local input
  formatDateForInput(dateString: string): string {
    if (!dateString) return '';
    
    const date = new Date(dateString);
    return date.toISOString().slice(0, 16);
  }

  private handleError(error: any): Observable<never> {
    console.error('Blog service error:', error);
    return throwError(() => new Error(error.message || 'Server error'));
  }
}