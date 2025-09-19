import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Blog, BlogRequest, BlogPageResponse } from '../model/blog.model';

@Injectable({
  providedIn: 'root'
})
export class BlogService {
  private apiUrl = 'http://localhost:8080/api/blog';
  private fileApiUrl = 'http://localhost:8080/api/files';

  constructor(private http: HttpClient) {}

  // Public methods for blog viewing
  getPublishedBlogs(): Observable<Blog[]> {
    return this.http.get<Blog[]>(this.apiUrl).pipe(
      tap(blogs => console.log('Published blogs fetched:', blogs)),
      catchError(this.handleError)
    );
  }

  getPublishedBlogsWithPagination(page: number = 0, size: number = 10): Observable<BlogPageResponse> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<BlogPageResponse>(`${this.apiUrl}/page`, { params }).pipe(
      tap(response => console.log('Published blogs with pagination fetched:', response)),
      catchError(this.handleError)
    );
  }

  getPublishedBlogBySlug(slug: string): Observable<Blog> {
    return this.http.get<Blog>(`${this.apiUrl}/slug/${slug}`).pipe(
      tap(blog => console.log('Published blog fetched by slug:', blog)),
      catchError(this.handleError)
    );
  }

  searchPublishedBlogs(keyword: string, page: number = 0, size: number = 10): Observable<BlogPageResponse> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<BlogPageResponse>(`${this.apiUrl}/search`, { params }).pipe(
      tap(response => console.log('Published blogs search results:', response)),
      catchError(this.handleError)
    );
  }

  getPublishedBlogsByCategory(category: string, page: number = 0, size: number = 10): Observable<BlogPageResponse> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<BlogPageResponse>(`${this.apiUrl}/category/${category}`, { params }).pipe(
      tap(response => console.log('Published blogs by category fetched:', response)),
      catchError(this.handleError)
    );
  }

  // Admin methods for blog management
  getAllBlogs(): Observable<Blog[]> {
    return this.http.get<Blog[]>(`${this.apiUrl}/admin`).pipe(
      tap(blogs => console.log('All blogs fetched:', blogs)),
      catchError(this.handleError)
    );
  }

  getAllBlogsWithPagination(page: number = 0, size: number = 10): Observable<BlogPageResponse> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<BlogPageResponse>(`${this.apiUrl}/admin/page`, { params }).pipe(
      tap(response => console.log('All blogs with pagination fetched:', response)),
      catchError(this.handleError)
    );
  }

  getBlogById(id: number): Observable<Blog> {
    return this.http.get<Blog>(`${this.apiUrl}/admin/${id}`).pipe(
      tap(blog => console.log('Blog fetched by ID:', blog)),
      catchError(this.handleError)
    );
  }

  getBlogBySlug(slug: string): Observable<Blog> {
    return this.http.get<Blog>(`${this.apiUrl}/admin/slug/${slug}`).pipe(
      tap(blog => console.log('Blog fetched by slug:', blog)),
      catchError(this.handleError)
    );
  }

  searchAllBlogs(keyword: string, page: number = 0, size: number = 10): Observable<BlogPageResponse> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<BlogPageResponse>(`${this.apiUrl}/admin/search`, { params }).pipe(
      tap(response => console.log('All blogs search results:', response)),
      catchError(this.handleError)
    );
  }

  createBlog(blog: BlogRequest): Observable<Blog> {
    console.log('=== DEBUG: Creating blog with data ===', blog);
    
    return this.http.post<Blog>(`${this.apiUrl}/admin`, blog).pipe(
      tap(createdBlog => console.log('Blog created:', createdBlog)),
      catchError(this.handleError)
    );
  }

  updateBlog(id: number, blog: BlogRequest): Observable<Blog> {
    console.log('=== DEBUG: Updating blog with data ===', blog);
    
    return this.http.put<Blog>(`${this.apiUrl}/admin/${id}`, blog).pipe(
      tap(updatedBlog => console.log('Blog updated:', updatedBlog)),
      catchError(this.handleError)
    );
  }

  deleteBlog(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/admin/${id}`).pipe(
      tap(() => console.log('Blog deleted with ID:', id)),
      catchError(this.handleError)
    );
  }

  publishBlog(id: number): Observable<Blog> {
    return this.http.put<Blog>(`${this.apiUrl}/admin/${id}/publish`, {}).pipe(
      tap(blog => console.log('Blog published:', blog)),
      catchError(this.handleError)
    );
  }

  unpublishBlog(id: number): Observable<Blog> {
    return this.http.put<Blog>(`${this.apiUrl}/admin/${id}/unpublish`, {}).pipe(
      tap(blog => console.log('Blog unpublished:', blog)),
      catchError(this.handleError)
    );
  }

  // Utility methods
  getCategories(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/categories`).pipe(
      tap(categories => console.log('Blog categories fetched:', categories)),
      catchError(this.handleError)
    );
  }

  getAuthors(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/authors`).pipe(
      tap(authors => console.log('Blog authors fetched:', authors)),
      catchError(this.handleError)
    );
  }

  checkSlugAvailability(slug: string, excludeId?: number): Observable<{available: boolean}> {
    let params = new HttpParams().set('slug', slug);
    if (excludeId !== undefined) {
      params = params.set('excludeId', excludeId.toString());
    }
    
    return this.http.get<{available: boolean}>(`${this.apiUrl}/admin/slug-available`, { params }).pipe(
      tap(response => console.log('Slug availability checked:', response)),
      catchError(this.handleError)
    );
  }

  // File upload methods
  uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<any>(`${this.fileApiUrl}/upload`, formData).pipe(
      tap(response => console.log('File uploaded:', response)),
      catchError(this.handleError)
    );
  }

  getAvailableImages(): Observable<any[]> {
    return this.http.get<any[]>(`${this.fileApiUrl}/images`).pipe(
      tap(images => console.log('Available images fetched:', images)),
      catchError(this.handleError)
    );
  }

  getImageUrl(imagePath: string): string {
    if (!imagePath || imagePath.trim() === '') {
      return 'assets/img/blog/blog-1.jpg'; // Default blog image
    }
    
    // If it's already a complete URL (external image), return as-is
    if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
      return imagePath;
    }
    
    // If it's a local asset path, return as-is
    if (imagePath.startsWith('assets/')) {
      return imagePath;
    }
    
    // Normalize common stored patterns like 'uploads/filename.ext'
    const normalized = imagePath.startsWith('uploads/') ? imagePath.substring('uploads/'.length) : imagePath;

    // For uploaded files (just filename) or any other paths,
    // use the backend files endpoint
    return `http://localhost:8080/api/files/${normalized}`;
  }

  // Method to check if an image URL is valid
  checkImageUrl(imageUrl: string): Promise<boolean> {
    return new Promise((resolve) => {
      const img = new Image();
      img.onload = () => resolve(true);
      img.onerror = () => resolve(false);
      img.src = imageUrl;
    });
  }

  // Utility method to generate slug from title
  generateSlug(title: string): string {
    return title
      .toLowerCase()
      .replace(/[^a-z0-9\s-]/g, '')
      .replace(/\s+/g, '-')
      .replace(/-+/g, '-')
      .replace(/^-|-$/g, '');
  }

  // Utility method to generate excerpt from content
  generateExcerpt(content: string, maxLength: number = 150): string {
    if (!content) return '';
    
    // Remove HTML tags
    const plainText = content.replace(/<[^>]*>/g, '');
    
    if (plainText.length <= maxLength) {
      return plainText;
    }
    
    return plainText.substring(0, maxLength) + '...';
  }

  // Error handling
  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('Blog service error:', error);
    
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Backend error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      if (error.error && error.error.message) {
        errorMessage += `\nDetails: ${error.error.message}`;
      }
    }
    
    return throwError(() => new Error(errorMessage));
  }
}