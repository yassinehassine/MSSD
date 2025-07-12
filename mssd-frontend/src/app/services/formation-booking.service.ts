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

export interface FormationBookingRequest {
  formationId: number;
  name: string;
  email: string;
  phone: string;
  company: string;
}

export interface FormationBookingResponse {
  id: number;
  formationId: number;
  name: string;
  email: string;
  phone: string;
  company: string;
  createdAt: string;
}

export interface FormationBookingAdmin extends FormationBookingResponse {
  formationTitle?: string;
}

@Injectable({ providedIn: 'root' })
export class FormationBookingService {
  private formationsUrl = '/api/formations/published';
  private bookingUrl = '/api/formation-bookings';

  constructor(private http: HttpClient) {}

  getPublishedFormations(): Observable<Formation[]> {
    return this.http.get<Formation[]>(this.formationsUrl);
  }

  bookFormation(request: FormationBookingRequest): Observable<FormationBookingResponse> {
    return this.http.post<FormationBookingResponse>(this.bookingUrl, request);
  }

  getAllBookings(): Observable<FormationBookingResponse[]> {
    return this.http.get<FormationBookingResponse[]>(this.bookingUrl);
  }
} 