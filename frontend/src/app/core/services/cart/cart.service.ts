import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../../environments/environment";
import { CartResponse, CartItemDto, CartItemUpdateDto } from "../../../models/cart.model";

@Injectable({
    providedIn: 'root'
})

export class CartService {
    private apiUrl = `${environment.apiUrl}/api/cart`;

    constructor(private http: HttpClient) {}

    getCart(): Observable<CartResponse> {
        return this.http.get<CartResponse>(this.apiUrl);
    }

    addItemToCart(item: CartItemDto): Observable<CartResponse> {
        return this.http.post<CartResponse>(`${this.apiUrl}/items`, item);
    }

    updateCartItem(itemId: string, update: CartItemUpdateDto): Observable<CartResponse> {
        return this.http.put<CartResponse>(`${this.apiUrl}/itema/${itemId}`, update)
    }

    removeItemFromCart(itemId: string): Observable<CartResponse> {
        return this.http.delete<CartResponse>(`${this.apiUrl}/items/${itemId}`);
    }

    clearCart(): Observable<CartResponse> {
        return this.http.delete<CartResponse>(this.apiUrl);
    }
}