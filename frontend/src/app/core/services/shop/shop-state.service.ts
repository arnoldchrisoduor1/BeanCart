import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { Shop } from "../../../models/shop.model";

@Injectable({
    providedIn: 'root'
})

export class ShopStateService {
    private readonly SHOP_KEY = 'shop_data'

    private currentShopSubject = new BehaviorSubject<Shop | null>(null);
    public currentShop$: Observable<Shop | null> = this.currentShopSubject.asObservable();

    constructor(){
        this.loadShopFromStorage();
    }

    private loadShopFromStorage(): void {
        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            const storedShop = localStorage.getItem(this.SHOP_KEY);
        }
    }

    public get currentShopValue(): Shop | null {
        return this.currentShopSubject.value;
    }

    public setShop(shop: Shop): void {
        this.currentShopSubject.next(shop);

        if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
            localStorage.setItem(this.SHOP_KEY, JSON.stringify(shop));
        }
    }
}