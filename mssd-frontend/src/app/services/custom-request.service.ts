import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CustomRequestResponse {
  id: number;
  companyName: string;
  contactPerson: string;
  email: string;
  phone: string;
  subject: string;
  details: string;
  budget: number;
  preferredStartDate: string;
  status: string;
  adminNotes: string;
  dateSubmitted: string;
  dateUpdated: string;
}

export interface CustomRequest {
  companyName: string;
  contactPerson: string;
  email: string;
  phone?: string;
  subject: string;
  details: string;
  budget?: number;
  preferredStartDate?: string;
}

export interface CustomRequestUpdate {
  status: string;
  adminNotes?: string;
}

@Injectable({ providedIn: 'root' })
export class CustomRequestService {
  private apiUrl = '/api/custom-requests';

  constructor(private http: HttpClient) {}

  getAllCustomRequests(): Observable<CustomRequestResponse[]> {
    return this.http.get<CustomRequestResponse[]>(this.apiUrl);
  }

  submitCustomRequest(request: CustomRequest): Observable<CustomRequestResponse> {
    return this.http.post<CustomRequestResponse>(this.apiUrl + '/submit', request);
  }

  updateCustomRequest(id: number, update: CustomRequestUpdate) {
    return this.http.put<CustomRequestResponse>(`${this.apiUrl}/${id}`, update);
  }
} 