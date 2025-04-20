import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { User } from "../../models/user.model";
import { ShopStateService } from "../services/shop/shop-state.service";

@Injectable({
    providedIn: 'root'
})
export class AuthStateService {
    private readonly TOKEN_KEY = 'auth_token';
    private readonly USER_KEY = 'user_data';

    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$: Observable<User | null> = this.currentUserSubject.asObservable();

    constructor(private shopState: ShopStateService) {
        // Initial state from localstorage on app startup
        this.loadUserFromStorage();
    }

    private loadUserFromStorage(): void {
        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            const storedUser = localStorage.getItem(this.USER_KEY);
            const storedToken = localStorage.getItem(this.TOKEN_KEY);
            if (storedUser) {
                try {
                    const userData = JSON.parse(storedUser);
                // If the token exists separately, make sure the user object has it
                if (storedToken && (!userData.token || userData.token !== storedToken)) {
                    userData.token = storedToken;
                }
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
        // Preserve the token from the current user if it exists and the new user doesn't have one
        if (!user.token && this.currentUserValue?.token) {
            user.token = this.currentUserValue.token;
        }
        
        this.currentUserSubject.next(user);
        
        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            localStorage.setItem(this.USER_KEY, JSON.stringify(user));
            // Also update the token if it exists
            if (user.token) {
                localStorage.setItem(this.TOKEN_KEY, user.token);
            }
        }
    }

    public logout(): void {
        // clear state
        this.currentUserSubject.next(null);

        // clearing the shop data from local storage
        this.shopState.clearShopData();

        // clear storage
        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            localStorage.removeItem(this.TOKEN_KEY);
            localStorage.removeItem(this.USER_KEY);
            localStorage.removeItem('shop_data');
        }
    }
}