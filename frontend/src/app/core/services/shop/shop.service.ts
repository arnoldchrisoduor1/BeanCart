import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { ShopStateService } from "./shop-state.service";
import { Shop } from "../../../models/shop.model";
import { environment } from "../../../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class ShopService {
    // private apiUrl = `${environment.apiUrl}/api/shops`;
    private apiUrl = '/api/shops';
    
    constructor(
        private http: HttpClient,
        private shopStateService: ShopStateService
    ) {}
    
    // CREATE SHOP SERVICE
    createShop(createShopData: {
        name: string,
        description: string,
        phone: string,
        email: string,
        address: string,
        logoUrl: string
    }): Observable<Shop> {
        return this.http.post<Shop>(`${this.apiUrl}`, createShopData)
        .pipe(
            tap((response: Shop) => {
                // Save shop data to state service
                this.shopStateService.setShop(response);
            })
        );
    }
    
    // DELETE SHOP SERVICE
    deleteShop(deleteShopData: {shopId: string}): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${deleteShopData.shopId}`);
    }
    
    // GET SPECIFIC SHOP SERVICE
    getSpecificShop(getSpecificShopData: {shopId: string}): Observable<Shop> {
        // Fixed HTTP method from delete to get
        return this.http.get<Shop>(`${this.apiUrl}/${getSpecificShopData.shopId}`)
        .pipe(
            tap((response: Shop) => {
                // Not setting to state as this might not be the current user's shop
                // But could store it temporarily if needed
            })
        );
    }
    
    // GET ALL SHOPS SERVICE
    getAllShops(): Observable<Shop[]> {
        // Fixed return type from Shop to Shop[]
        return this.http.get<Shop[]>(`${this.apiUrl}`)
        .pipe(
            tap((response: Shop[]) => {
                // Store all shops in state
                this.shopStateService.setAllShops(response);
            })
        );
    }
    
    // UPDATING SPECIFIC SHOP SERVICE
    updateShop(shopId: string, updateShopData: {
        name: string,
        description: string,
        phone: string,
        email: string,
        address: string,
        logoUrl: string
    }): Observable<Shop> {
        // Added shopId parameter to specify which shop to update
        return this.http.put<Shop>(`${this.apiUrl}/${shopId}`, updateShopData)
        .pipe(
            tap((response: Shop) => {
                // If this is the current shop, update the state
                if (this.shopStateService.currentShopValue?.id === response.id) {
                    this.shopStateService.setShop(response);
                }
            })
        );
    }
    
    // GET SHOP BY SEARCH
    getShopByNameSearch(shopSearchData: {query: string}): Observable<Shop[]> {
        // Fixed return type from Shop to Shop[]
        return this.http.get<Shop[]>(`${this.apiUrl}/search?name=${shopSearchData.query}`)
        .pipe(
            tap((response: Shop[]) => {
                // Store search results in state
                this.shopStateService.setSearchResults(response);
            })
        );
    }
    
    // GET CURRENT USER'S SHOPS
    getCurrentUserShops(): Observable<Shop[]> {
        // Fixed return type from Shop to Shop[]
        return this.http.get<Shop[]>(`${this.apiUrl}/seller`)
        .pipe(
            tap((response: Shop[]) => {
                // Store user shops in state
                this.shopStateService.setUserShops(response);
                
                // If there's at least one shop and no current shop is set,
                // set the first shop as the current shop
                if (response.length > 0 && !this.shopStateService.currentShopValue) {
                    this.shopStateService.setShop(response[0]);
                }
            })
        );
    }
    
    // GET SHOP BY SPECIFIC SELLER
    getShopBySpecificSeller(sellerShopData: {id: string}): Observable<Shop[]> {
        // Fixed return type from Shop to Shop[]
        return this.http.get<Shop[]>(`${this.apiUrl}/seller/${sellerShopData.id}`)
        .pipe(
            tap((response: Shop[]) => {
                // Store seller shops in state
                this.shopStateService.setSellerShops(response);
            })
        );
    }
}