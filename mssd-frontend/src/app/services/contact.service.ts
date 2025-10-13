import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ContactDto {
  fullName: string;
  email: string;
  phone: string;
  subject: string;
  message: string;
}

@Injectable({ providedIn: 'root' })
export class ContactService {
  private apiUrl = '/api/contact';

  constructor(private http: HttpClient) {}

  submitContact(contact: ContactDto): Observable<void> {
    return this.http.post<void>(this.apiUrl, contact);
  }

  getAllContacts(): Observable<ContactDto[]> {
    return this.http.get<ContactDto[]>(this.apiUrl);
  }
}
