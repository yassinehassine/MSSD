import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CalendarReservation {
  id?: number;
  clientName: string;
  clientEmail: string;
  clientPhone?: string;
  eventTitle: string;
  eventDescription?: string;
  eventDate: string; // ISO string format
  duration?: number; // in minutes
  location?: string;
  status: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED';
  adminNotes?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface CalendarReservationStats {
  total: number;
  pending: number;
  confirmed: number;
  cancelled: number;
  completed: number;
}

@Injectable({
  providedIn: 'root'
})
export class CalendarReservationService {
  private apiUrl = '/api/calendar-reservations';

  constructor(private http: HttpClient) { }

  getAllReservations(): Observable<CalendarReservation[]> {
    return this.http.get<CalendarReservation[]>(this.apiUrl);
  }

  getReservationById(id: number): Observable<CalendarReservation> {
    return this.http.get<CalendarReservation>(`${this.apiUrl}/${id}`);
  }

  createReservation(reservation: CalendarReservation): Observable<CalendarReservation> {
    return this.http.post<CalendarReservation>(this.apiUrl, reservation);
  }

  updateReservation(id: number, reservation: CalendarReservation): Observable<CalendarReservation> {
    return this.http.put<CalendarReservation>(`${this.apiUrl}/${id}`, reservation);
  }

  deleteReservation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getReservationsByStatus(status: string): Observable<CalendarReservation[]> {
    return this.http.get<CalendarReservation[]>(`${this.apiUrl}/status/${status}`);
  }

  searchReservations(keyword: string): Observable<CalendarReservation[]> {
    return this.http.get<CalendarReservation[]>(`${this.apiUrl}/search`, {
      params: { keyword }
    });
  }

  getReservationStatistics(): Observable<CalendarReservationStats> {
    return this.http.get<CalendarReservationStats>(`${this.apiUrl}/statistics`);
  }

  updateReservationStatus(id: number, status: string): Observable<CalendarReservation> {
    return this.http.patch<CalendarReservation>(`${this.apiUrl}/${id}/status`, null, {
      params: { status }
    });
  }
}