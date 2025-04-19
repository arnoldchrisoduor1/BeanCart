import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { ShopStateService } from "./shop-state.service";
import { Shop } from "../../../models/shop.model";

@Injectable({
    providedIn: 'root'
})
export class ShopService {
    private apiUrl = 'http://localhost:3000/api/shops'

    constructor(
        private http: HttpClient,
        private shopStateService: ShopStateService
    ) {
    }

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
                // saving shop data to state service
                this.shopStateService.setShop(response);
            })
        )
    }
}