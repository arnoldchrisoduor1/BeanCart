// src/app/core/auth/auth.service.ts
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { AuthStateService } from "./auth-state.service";
import { User } from "../../models/user.model";
import { environment } from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // private apiUrl = `${environment.apiUrl}/api/auth`;
  // private apiUpdateUrl = `${environment.apiUrl}/api/users`;

  private apiUrl = '/api/auth';
  private apiUpdateUrl = '/api/users';
  
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

  // Updating the user profile.
  update(updateData: { 
    firstName: string, 
    lastName: string,
    email?: string,
    address?: string,
    mobileNumber?: string,
    profileImageUrl?: string,
    occupation?: string }): Observable<any> {
    return this.http.put(`${this.apiUpdateUrl}/profile`, updateData);
  }

  // Getting the user profile.
  getProfile(): Observable<User> {
    return this.http.get<User>(`${this.apiUpdateUrl}/profile`)
    .pipe(
      tap((response: User) => {
        this.authStateService.setUser(response);
      })
    )
  }
}