import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Theme, ThemeRequest } from '../model/theme.model';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private apiUrl = '/api/themes';

  constructor(private http: HttpClient) {}

  getAllThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(this.apiUrl);
  }

  getPublishedThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(`${this.apiUrl}/published`);
  }

  getThemeById(id: number): Observable<Theme> {
    return this.http.get<Theme>(`${this.apiUrl}/${id}`);
  }

  getThemeBySlug(slug: string): Observable<Theme> {
    return this.http.get<Theme>(`${this.apiUrl}/slug/${slug}`);
  }

  createTheme(theme: ThemeRequest): Observable<Theme> {
    return this.http.post<Theme>(this.apiUrl, theme);
  }

  updateTheme(id: number, theme: ThemeRequest): Observable<Theme> {
    return this.http.put<Theme>(`${this.apiUrl}/${id}`, theme);
  }

  deleteTheme(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}