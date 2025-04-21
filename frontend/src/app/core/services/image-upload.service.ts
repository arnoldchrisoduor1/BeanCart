// src/app/core/services/image-upload.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ImageUploadService {
  private apiUrl = `${environment.apiUrl}/api/images`;

  constructor(private http: HttpClient) {}

  getImageUploadUrl(): Observable<{ uploadURL: string }> {
    return this.http.get<{ uploadURL: string }>(`${this.apiUrl}/upload-url`);
  }

  uploadImageToS3(url: string, file: File): Observable<any> {
    return this.http.put(url, file, {
      headers: {
        'Content-Type': file.type || 'application/octet-stream'
      }
    });
  }
}