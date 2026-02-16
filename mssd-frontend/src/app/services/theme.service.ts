import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { Theme, FormationSummary } from '../model/annexes.model';

export interface ThemeCreateUpdateDto {
  name: string;
  slug: string;
  description?: string;
  iconUrl?: string;
  active: boolean;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
}

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) {}

  /**
   * Get all active themes
   */
  getAllThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(`${this.apiUrl}/themes`);
  }

  /**
   * Get themes with their formations for the annexes display
   */
  getThemesWithFormations(): Observable<Theme[]> {
    console.log('🌐 Calling API:', `${this.apiUrl}/themes/with-formations`);
    return this.http.get<Theme[]>(`${this.apiUrl}/themes/with-formations`).pipe(
      tap(response => console.log('📨 API Response:', response)),
      catchError(error => {
        console.error('🚫 API Error:', error);
        throw error;
      })
    );
  }

  /**
   * Get a single theme with its formations by slug
   */
  getThemeWithFormations(slug: string): Observable<Theme> {
    return this.http.get<Theme>(`${this.apiUrl}/themes/${slug}/formations`);
  }

  /**
   * Get all formations (flat list) for dropdown selection
   */
  getAllFormations(): Observable<FormationSummary[]> {
    return this.http.get<FormationSummary[]>(`${this.apiUrl}/formations`);
  }

  // ========== ADMIN CRUD METHODS ==========

  /**
   * Create a new theme (Admin only)
   */
  createTheme(theme: ThemeCreateUpdateDto): Observable<Theme> {
    return this.http.post<Theme>(`${this.apiUrl}/themes`, theme);
  }

  /**
   * Update an existing theme (Admin only)
   */
  updateTheme(id: number, theme: ThemeCreateUpdateDto): Observable<Theme> {
    return this.http.put<Theme>(`${this.apiUrl}/themes/${id}`, theme);
  }

  /**
   * Delete a theme (Admin only)
   */
  deleteTheme(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/themes/${id}`);
  }

  /**
   * Get theme by ID for editing (Admin only)
   */
  getThemeById(id: number): Observable<Theme> {
    return this.http.get<Theme>(`${this.apiUrl}/themes/${id}`);
  }

  /**
   * Get all themes including inactive ones (Admin only)
   */
  getAllThemesAdmin(): Observable<Theme[]> {
    return this.http.get<Theme[]>(`${this.apiUrl}/themes/admin`);
  }

  /**
   * Fix themes - make sure they are active and have proper data
   */
  fixThemes(): Observable<any> {
    console.log('🔧 Calling fix API:', `${this.apiUrl}/fix/themes`);
    return this.http.post<any>(`${this.apiUrl}/fix/themes`, {}).pipe(
      tap(response => console.log('🔧 Fix Response:', response)),
      catchError(error => {
        console.error('🚫 Fix Error:', error);
        throw error;
      })
    );
  }
}