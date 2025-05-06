import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, catchError, of, tap } from "rxjs";
import { CartResponse, CartItemDto, CartItemUpdateDto } from "../../../models/cart.model";
import { CartService } from "./cart.service";

@Injectable({
    providedIn: 'root'
})

export class CartStateService {
    private cartSubject = new BehaviorSubject<CartResponse | null>(null);
    private loadingSubject = new BehaviorSubject<boolean>(false);
    private errorSubject = new BehaviorSubject<string | null>(null);

    public cart$ = this.cartSubject.asObservable();
    public loading$ = this.loadingSubject.asObservable();
    public error = this.errorSubject.asObservable();

    constructor(private cartService: CartService) {}

    addItem(item: CartItemDto): Observable<CartResponse | null> {
        this.loadingSubject.next(true);
        return this.cartService.addItemToCart(item).pipe(
          tap(cart => {
            this.cartSubject.next(cart);
            this.loadingSubject.next(false);
            this.errorSubject.next(null);
          }),
          catchError(error => {
            this.loadingSubject.next(false);
            this.errorSubject.next(error.message || 'Failed to add item to cart');
            return of(null);
          })
        );
      }

    loadCart(): void {
        this.loadingSubject.next(true);
        this.cartService.getCart().pipe(
            tap(cart => {
                this.cartSubject.next(cart);
                this.loadingSubject.next(false);
                this.errorSubject.next(null);
                console.log("Loading cart with", cart);
            }),
            catchError(error => {
                this.loadingSubject.next(false);
                this.errorSubject.next(error.message || 'Failed to load cart');
                return of(null);
            })
        ).subscribe();
    }

    updateItem(itemId: string, update: CartItemUpdateDto): Observable<CartResponse | null> {
        this.loadingSubject.next(true);
        return this.cartService.updateCartItem(itemId, update).pipe(
            tap(cart => {
                this.cartSubject.next(cart);
                this.loadingSubject.next(false);
                this.errorSubject.next(null);
            }),
            catchError(error => {
                this.loadingSubject.next(false);
                this.errorSubject.next(error.message || 'Failed to update cart item');
                return of(null);
            })
        );
    }

    removeItem(itemId: string): Observable<CartResponse | null> {
        this.loadingSubject.next(true);
        return this.cartService.removeItemFromCart(itemId).pipe(
            tap(cart => {
                this.cartSubject.next(cart);
                this.loadingSubject.next(false);
                this.errorSubject.next(null)
            }),
            catchError(error => {
                this.loadingSubject.next(false);
                this.errorSubject.next(error.message || 'Failed to remove item from cart');
                return of(null)
            })
        );
    }

    clearCart(): Observable<CartResponse | null> {
        this.loadingSubject.next(true);
        return this.cartService.clearCart().pipe(
            tap(cart => {
                this.cartSubject.next(cart);
                this.loadingSubject.next(false);
                this.errorSubject.next(null);
            }),
            catchError(error => {
                this.loadingSubject.next(error.message || 'Failed to clear cart');
                return of(null);
            })
        );
    }

    getCartValue(): CartResponse | null {
        return this.cartSubject.value;
    }

    clearError(): void {
        this.errorSubject.next(null);
    }

    resetState(): void {
        this.cartSubject.next(null);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
    }
}