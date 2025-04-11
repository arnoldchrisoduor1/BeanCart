// src/app/core/auth/auth.service.ts
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { AuthStateService } from "./auth-state.service";
import { User } from "../../models/user.model";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:3000/api/auth';
  
  constructor(
    private http: HttpClient,
    private authStateService: AuthStateService
  ) {}
  
  register(userData: { 
    email: string, 
    password: string, 
    confirmPassword: string, 
    role: string
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }
  
  login(credentials: { email: string, password: string }): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap((response: User) => {
          // Saving user data in state service
          this.authStateService.setUser(response);
        })
      );
  }
  
  logout(): void {
    this.authStateService.logout();
  }
}