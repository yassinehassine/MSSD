import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class FileUploadService {
  private uploadUrl = '/api/files/upload';

  constructor(private http: HttpClient) {}

  upload(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{filename: string, path: string, url: string}>(this.uploadUrl, formData)
      .pipe(
        map(response => response.filename) // Return just the filename
      );
  }
} 