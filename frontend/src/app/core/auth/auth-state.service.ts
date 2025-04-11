import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { User } from "../../models/user.model";

@Injectable({
    providedIn: 'root'
})
export class AuthStateService {
    private readonly TOKEN_KEY = 'auth_token';
    private readonly USER_KEY = 'user_data';

    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$: Observable<User | null> = this.currentUserSubject.asObservable();

    constructor() {
        // Initial state from localstorage on app startup
        this.loadUserFromStorage();
    }

    private loadUserFromStorage(): void {
        if (typeof window !== 'undefined' && localStorage.getItem('yourToken')) {
            // safe to access localStorage
            const storedUser = localStorage.getItem(this.USER_KEY);
            if (storedUser) {
                try {
                    const userData = JSON.parse(storedUser);
                    this.currentUserSubject.next(userData);
                } catch (e) {
                    // Handle parsing error.
                    this.logout();
                }
            }
          }
    }

    public get currentUserValue(): User | null {
        return this.currentUserSubject.value;
    }

    public get isLoggedIn(): boolean {
        return !!this.currentUserValue;
    }

    public setUser(user: User): void {
        // store in state
        this.currentUserSubject.next(user);

        // store in localStorage.
        if (typeof window !== 'undefined' && localStorage.getItem('yourToken')) {
            // safe to access localStorage
            localStorage.setItem(this.TOKEN_KEY, user.token);
            localStorage.setItem(this.USER_KEY,  JSON.stringify(user));
          }
    }

    public logout(): void {
        // clear state
        this.currentUserSubject.next(null);

        // clear storage
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.USER_KEY);
    }
}