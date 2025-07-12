import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Formation {
  id: number;
  title: string;
  slug: string;
  description: string;
  category: string;
  price: number;
  duration: string;
  imageUrl?: string;
  level: string;
  published: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface FormationRequest {
  title: string;
  slug: string;
  description: string;
  category: string;
  price: number;
  duration: string;
  imageUrl?: string;
  level: string;
  published: boolean;
}

@Injectable({ providedIn: 'root' })
export class FormationService {
  private apiUrl = '/api/formations';

  constructor(private http: HttpClient) {}

  getAllFormations(): Observable<Formation[]> {
    return this.http.get<Formation[]>(this.apiUrl);
  }

  getFormationById(id: number): Observable<Formation> {
    return this.http.get<Formation>(`${this.apiUrl}/${id}`);
  }

  createFormation(formation: FormationRequest): Observable<Formation> {
    return this.http.post<Formation>(this.apiUrl, formation);
  }

  updateFormation(id: number, formation: FormationRequest): Observable<Formation> {
    return this.http.put<Formation>(`${this.apiUrl}/${id}`, formation);
  }

  deleteFormation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
} 