import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReservationDto {
  id: number;
  calendarId: number;
  calendarTitle: string;
  visitorName: string;
  visitorEmail: string;
  visitorPhone: string;
  numberOfPeople: number;
  notes?: string;
  status: string;
  reservationDate?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface ReservationRequestDto {
  calendarId: number;
  visitorName: string;
  visitorEmail: string;
  visitorPhone?: string;
  numberOfPeople: number;
  notes?: string;
}

@Injectable({ providedIn: 'root' })
export class ReservationService {
  private apiUrl = '/api/reservations';

  constructor(private http: HttpClient) {}

  createReservation(reservation: ReservationRequestDto): Observable<ReservationDto> {
    return this.http.post<ReservationDto>(this.apiUrl, reservation);
  }

  getAllReservations(): Observable<ReservationDto[]> {
    return this.http.get<ReservationDto[]>(this.apiUrl);
  }

  confirmReservation(id: number): Observable<ReservationDto> {
    return this.http.put<ReservationDto>(`${this.apiUrl}/${id}/confirm`, {});
  }
  cancelReservation(id: number): Observable<ReservationDto> {
    return this.http.put<ReservationDto>(`${this.apiUrl}/${id}/cancel`, {});
  }
  updateReservation(id: number, reservation: ReservationRequestDto): Observable<ReservationDto> {
    return this.http.put<ReservationDto>(`${this.apiUrl}/${id}`, reservation);
  }
  deleteReservation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
} 