import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:3000/api/auth';

    constructor(private http: HttpClient) {}

    register(userData:{ email: string, password: string, confirmPassword: string, role: string}): Observable<any> {
        return this.http.post(`${this.apiUrl}/register`, userData);
    }

    login(credentials: { email: string, password: string }):Observable<any> {
        return this.http.post(`${this.apiUrl}/login`, credentials);
    }
}