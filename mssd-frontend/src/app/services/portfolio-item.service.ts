import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PortfolioItem {
  id?: number;
  companyName: string;
  trainingTitle: string;
  trainingDate: string;
  logoUrl?: string;
  description?: string;
  active?: boolean;
  createdAt?: Date;
  updatedAt?: Date;
}

@Injectable({
  providedIn: 'root'
})
export class PortfolioItemService {
  // Use Angular proxy to avoid CORS and hardcoded host/port
  private apiUrl = '/api/portfolio-items';

  constructor(private http: HttpClient) {}

  getAllPortfolioItems(): Observable<PortfolioItem[]> {
    return this.http.get<PortfolioItem[]>(`${this.apiUrl}`);
  }

  getActivePortfolioItems(): Observable<PortfolioItem[]> {
    return this.http.get<PortfolioItem[]>(`${this.apiUrl}/active`);
  }

  getPortfolioItemById(id: number): Observable<PortfolioItem> {
    return this.http.get<PortfolioItem>(`${this.apiUrl}/${id}`);
  }

  createPortfolioItem(item: PortfolioItem): Observable<PortfolioItem> {
    return this.http.post<PortfolioItem>(`${this.apiUrl}`, item);
  }

  updatePortfolioItem(id: number, item: PortfolioItem): Observable<PortfolioItem> {
    return this.http.put<PortfolioItem>(`${this.apiUrl}/${id}`, item);
  }

  deletePortfolioItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  uploadLogo(file: File): Observable<{url: string}> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{url: string}>(`${this.apiUrl}/upload-logo`, formData);
  }
}