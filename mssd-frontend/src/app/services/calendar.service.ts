import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Formation } from './formation.service';

export interface CalendarDto {
  id: number;
  title: string;
  description: string;
  startTime: string;
  endTime: string;
  location: string;
  maxCapacity: number;
  currentCapacity: number;
  status: 'AVAILABLE' | 'FULL' | 'CANCELLED' | 'COMPLETED';
  createdAt?: string;
  updatedAt?: string;
  availableSpots?: number;
  eventType?: 'EVENT' | 'FORMATION';
  formation?: Formation;
}

export interface CalendarRequestDto {
  title: string;
  description?: string;
  startTime: string;
  endTime: string;
  location: string;
  maxCapacity: number;
  status?: 'AVAILABLE' | 'FULL' | 'CANCELLED' | 'COMPLETED';
}

@Injectable({ providedIn: 'root' })
export class CalendarService {
  private apiUrl = '/api/calendars';

  constructor(private http: HttpClient) {}

  // Get all calendar events
  getAllCalendars(): Observable<CalendarDto[]> {
    return this.http.get<CalendarDto[]>(this.apiUrl);
  }

  // Get calendar event by ID
  getCalendarById(id: number): Observable<CalendarDto> {
    return this.http.get<CalendarDto>(`${this.apiUrl}/${id}`);
  }

  // Get available calendar events
  getAvailableCalendars(): Observable<CalendarDto[]> {
    return this.http.get<CalendarDto[]>(`${this.apiUrl}/available`);
  }

  // Get calendar events by date range
  getCalendarsByDateRange(start: string, end: string): Observable<CalendarDto[]> {
    return this.http.get<CalendarDto[]>(`${this.apiUrl}/range?start=${start}&end=${end}`);
  }

  // Get calendar events by location
  getCalendarsByLocation(location: string): Observable<CalendarDto[]> {
    return this.http.get<CalendarDto[]>(`${this.apiUrl}/location/${location}`);
  }

  // Create a new calendar event
  createCalendar(calendar: CalendarRequestDto): Observable<CalendarDto> {
    return this.http.post<CalendarDto>(this.apiUrl, calendar);
  }

  // Update an existing calendar event
  updateCalendar(id: number, calendar: CalendarRequestDto): Observable<CalendarDto> {
    return this.http.put<CalendarDto>(`${this.apiUrl}/${id}`, calendar);
  }

  // Delete a calendar event
  deleteCalendar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  joinEvent(id: number): Observable<CalendarDto> {
    return this.http.post<CalendarDto>(`${this.apiUrl}/${id}/join`, {});
  }
} 