import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Portfolio, Formation } from '../model/portfolio.model';

@Injectable({
  providedIn: 'root'
})
export class PortfolioService {
  private apiUrl = 'http://localhost:8080/api/portfolio';
  private fileApiUrl = 'http://localhost:8080/api/files';

  constructor(private http: HttpClient) {}

  // Get all active portfolios (for public view)
  getActivePortfolios(): Observable<Portfolio[]> {
    return this.http.get<Portfolio[]>(this.apiUrl).pipe(
      tap(portfolios => console.log('Active portfolios fetched:', portfolios)),
      catchError(this.handleError)
    );
  }

  // Get all portfolios (for admin view)
  getAllPortfolios(): Observable<Portfolio[]> {
    return this.http.get<Portfolio[]>(`${this.apiUrl}/admin`).pipe(
      tap(portfolios => console.log('All portfolios fetched:', portfolios)),
      catchError(this.handleError)
    );
  }

  // Get portfolio by ID
  getPortfolioById(id: number): Observable<Portfolio> {
    return this.http.get<Portfolio>(`${this.apiUrl}/${id}`).pipe(
      tap(portfolio => console.log('Portfolio fetched by ID:', portfolio)),
      catchError(this.handleError)
    );
  }

  // Get portfolios by category
  getPortfoliosByCategory(category: string): Observable<Portfolio[]> {
    return this.http.get<Portfolio[]>(`${this.apiUrl}/category/${category}`).pipe(
      tap(portfolios => console.log('Portfolios by category fetched:', portfolios)),
      catchError(this.handleError)
    );
  }

  // Get portfolios by formation
  getPortfoliosByFormation(formationId: number): Observable<Portfolio[]> {
    return this.http.get<Portfolio[]>(`${this.apiUrl}/formation/${formationId}`).pipe(
      tap(portfolios => console.log('Portfolios by formation fetched:', portfolios)),
      catchError(this.handleError)
    );
  }

  // Create portfolio
  createPortfolio(portfolio: Portfolio): Observable<Portfolio> {
    console.log('=== DEBUG: Creating portfolio with data ===', portfolio);
    
    // Ensure formationId is a number
    const portfolioData = {
      ...portfolio,
      formationId: Number(portfolio.formationId),
      active: portfolio.active ?? true
    };
    
    console.log('Portfolio data being sent:', portfolioData);
    
    return this.http.post<Portfolio>(this.apiUrl, portfolioData).pipe(
      tap(createdPortfolio => console.log('Portfolio created:', createdPortfolio)),
      catchError(this.handleError)
    );
  }

  // Update portfolio
  updatePortfolio(id: number, portfolio: Portfolio): Observable<Portfolio> {
    console.log('=== DEBUG: Updating portfolio with data ===', portfolio);
    
    // Ensure formationId is a number
    const portfolioData = {
      ...portfolio,
      formationId: Number(portfolio.formationId)
    };
    
    return this.http.put<Portfolio>(`${this.apiUrl}/${id}`, portfolioData).pipe(
      tap(updatedPortfolio => console.log('Portfolio updated:', updatedPortfolio)),
      catchError(this.handleError)
    );
  }

  // Delete portfolio
  deletePortfolio(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => console.log('Portfolio deleted with ID:', id)),
      catchError(this.handleError)
    );
  }

  // Deactivate portfolio
  deactivatePortfolio(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/deactivate`, {}).pipe(
      tap(() => console.log('Portfolio deactivated with ID:', id)),
      catchError(this.handleError)
    );
  }

  // Activate portfolio
  activatePortfolio(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/activate`, {}).pipe(
      tap(() => console.log('Portfolio activated with ID:', id)),
      catchError(this.handleError)
    );
  }

  // Get formations
  getFormations(): Observable<Formation[]> {
    return this.http.get<Formation[]>(`${this.apiUrl}/formations`).pipe(
      tap(formations => console.log('Formations fetched:', formations)),
      catchError(this.handleError)
    );
  }

  // Upload file
  uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<any>(`${this.fileApiUrl}/upload`, formData).pipe(
      tap(response => console.log('File uploaded:', response)),
      catchError(this.handleError)
    );
  }

  // Get available images from the server
  getAvailableImages(): Observable<any[]> {
    return this.http.get<any[]>(`${this.fileApiUrl}/images`).pipe(
      tap(images => console.log('Available images fetched:', images)),
      catchError(this.handleError)
    );
  }

  // Get image URL with enhanced support for any image source
  getImageUrl(imagePath: string): string {
    if (!imagePath || imagePath.trim() === '') {
      return 'assets/img/portfolio/app-1.jpg'; // Default portfolio image (exists)
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
    // use the backend files endpoint (matches upload response url base)
  return `http://localhost:8080/api/files/${normalized}`;
  }

  // Alternative method for direct asset access (for backwards compatibility)
  getAssetImageUrl(imagePath: string): string {
    if (!imagePath) {
      return 'assets/img/portfolio/app-1.jpg';
    }
    if (imagePath.startsWith('assets/')) {
      return imagePath;
    }
    return `assets/img/${imagePath}`;
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

  // Error handling
  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('Portfolio service error:', error);
    
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
