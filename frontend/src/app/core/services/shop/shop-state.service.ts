import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { Shop } from "../../../models/shop.model";

@Injectable({
    providedIn: 'root'
})
export class ShopStateService {
    private readonly CURRENT_SHOP_KEY = 'current_shop_data';
    private readonly ALL_SHOPS_KEY = 'all_shops_data';
    private readonly SEARCH_RESULTS_KEY = 'search_shops_data';
    private readonly USER_SHOPS_KEY = 'user_shops_data';
    private readonly SELLER_SHOPS_KEY = 'seller_shops_data';

    // Current shop (typically logged in user's primary shop)
    private currentShopSubject = new BehaviorSubject<Shop | null>(null);
    public currentShop$: Observable<Shop | null> = this.currentShopSubject.asObservable();

    // All shops collection
    private allShopsSubject = new BehaviorSubject<Shop[]>([]);
    public allShops$: Observable<Shop[]> = this.allShopsSubject.asObservable();

    // Search results
    private searchResultsSubject = new BehaviorSubject<Shop[]>([]);
    public searchResults$: Observable<Shop[]> = this.searchResultsSubject.asObservable();

    // Current user's shops
    private userShopsSubject = new BehaviorSubject<Shop[]>([]);
    public userShops$: Observable<Shop[]> = this.userShopsSubject.asObservable();

    // Specific seller's shops
    private sellerShopsSubject = new BehaviorSubject<Shop[]>([]);
    public sellerShops$: Observable<Shop[]> = this.sellerShopsSubject.asObservable();

    constructor() {
        this.loadShopsFromStorage();
    }

    private loadShopsFromStorage(): void {
        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            // Load current shop
            const storedShop = localStorage.getItem(this.CURRENT_SHOP_KEY);
            if (storedShop) {
                this.currentShopSubject.next(JSON.parse(storedShop));
            }

            // Load all shops
            const storedAllShops = localStorage.getItem(this.ALL_SHOPS_KEY);
            if (storedAllShops) {
                this.allShopsSubject.next(JSON.parse(storedAllShops));
            }

            // Load search results
            const storedSearchResults = localStorage.getItem(this.SEARCH_RESULTS_KEY);
            if (storedSearchResults) {
                this.searchResultsSubject.next(JSON.parse(storedSearchResults));
            }

            // Load user shops
            const storedUserShops = localStorage.getItem(this.USER_SHOPS_KEY);
            if (storedUserShops) {
                this.userShopsSubject.next(JSON.parse(storedUserShops));
            }

            // Load seller shops
            const storedSellerShops = localStorage.getItem(this.SELLER_SHOPS_KEY);
            if (storedSellerShops) {
                this.sellerShopsSubject.next(JSON.parse(storedSellerShops));
            }
        }
    }

    // Current shop getters and setters
    public get currentShopValue(): Shop | null {
        return this.currentShopSubject.value;
    }

    public setShop(shop: Shop): void {
        this.currentShopSubject.next(shop);
        
        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            localStorage.setItem(this.CURRENT_SHOP_KEY, JSON.stringify(shop));
        }
    }

    // All shops getters and setters
    public get allShopsValue(): Shop[] {
        return this.allShopsSubject.value;
    }

    public setAllShops(shops: Shop[]): void {
        this.allShopsSubject.next(shops);
        
        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            localStorage.setItem(this.ALL_SHOPS_KEY, JSON.stringify(shops));
        }
    }

    // Search results getters and setters
    public get searchResultsValue(): Shop[] {
        return this.searchResultsSubject.value;
    }

    public setSearchResults(shops: Shop[]): void {
        this.searchResultsSubject.next(shops);
        
        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            localStorage.setItem(this.SEARCH_RESULTS_KEY, JSON.stringify(shops));
        }
    }

    // User shops getters and setters
    public get userShopsValue(): Shop[] {
        return this.userShopsSubject.value;
    }

    public setUserShops(shops: Shop[]): void {
        this.userShopsSubject.next(shops);
        
        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            localStorage.setItem(this.USER_SHOPS_KEY, JSON.stringify(shops));
        }
    }

    // Specific seller shops getters and setters
    public get sellerShopsValue(): Shop[] {
        return this.sellerShopsSubject.value;
    }

    public setSellerShops(shops: Shop[]): void {
        this.sellerShopsSubject.next(shops);
        
        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            localStorage.setItem(this.SELLER_SHOPS_KEY, JSON.stringify(shops));
        }
    }

    // Clear all shop data
    public clearShopData(): void {
        this.currentShopSubject.next(null);
        this.allShopsSubject.next([]);
        this.searchResultsSubject.next([]);
        this.userShopsSubject.next([]);
        this.sellerShopsSubject.next([]);
        
        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            localStorage.removeItem(this.CURRENT_SHOP_KEY);
            localStorage.removeItem(this.ALL_SHOPS_KEY);
            localStorage.removeItem(this.SEARCH_RESULTS_KEY);
            localStorage.removeItem(this.USER_SHOPS_KEY);
            localStorage.removeItem(this.SELLER_SHOPS_KEY);
        }
    }
}