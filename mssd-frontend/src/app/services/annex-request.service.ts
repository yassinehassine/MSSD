import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AnnexRequest, AnnexRequestResponse, ModalityOption } from '../model/annexes.model';

@Injectable({
  providedIn: 'root'
})
export class AnnexRequestService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) {}

  /**
   * Submit a new training request
   */
  createRequest(request: AnnexRequest): Observable<AnnexRequestResponse> {
    return this.http.post<AnnexRequestResponse>(`${this.apiUrl}/annex-requests`, request);
  }

  /**
   * Get all training requests (admin only)
   */
  getAllRequests(): Observable<AnnexRequestResponse[]> {
    return this.http.get<AnnexRequestResponse[]>(`${this.apiUrl}/annex-requests`);
  }

  /**
   * Get training request by ID
   */
  getRequestById(id: number): Observable<AnnexRequestResponse> {
    return this.http.get<AnnexRequestResponse>(`${this.apiUrl}/annex-requests/${id}`);
  }

  /**
   * Get training requests by email
   */
  getRequestsByEmail(email: string): Observable<AnnexRequestResponse[]> {
    return this.http.get<AnnexRequestResponse[]>(`${this.apiUrl}/annex-requests/by-email/${email}`);
  }

  /**
   * Update request status (admin only)
   */
  updateRequestStatus(id: number, status: string): Observable<AnnexRequestResponse> {
    return this.http.put<AnnexRequestResponse>(`${this.apiUrl}/annex-requests/${id}/status?status=${status}`, {});
  }

  /**
   * Get modality options for the form
   */
  getModalityOptions(): ModalityOption[] {
    return [
      { value: 'IN_PERSON', label: 'En présentiel' },
      { value: 'REMOTE', label: 'À distance' },
      { value: 'HYBRID', label: 'Hybride' }
    ];
  }
}