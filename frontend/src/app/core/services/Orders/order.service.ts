import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../../environments/environment";
import { 
    OrderItem, 
    OrderItemCreateDto, 
    OrderItemUpdateDto, 
    Order, 
    OrderResponse, 
    OrderUpdateDto, 
    CreateOrderDto 
} from "../../../models/order.models";

@Injectable({
    providedIn: 'root'
})
export class OrderService {
    private baseUrl = `${environment.apiUrl}/api`;
    private ordersUrl = `${this.baseUrl}/orders`;
    private orderItemsUrl = `${this.baseUrl}/order-items`;

    constructor(private http: HttpClient) {}

    // =============== Order Methods ===============

    createOrder(orderData: CreateOrderDto): Observable<OrderResponse> {
        console.log("====Creating Order========");
        return this.http.post<OrderResponse>(this.ordersUrl, orderData);
    }

    getOrderById(orderId: string): Observable<OrderResponse> {
        return this.http.get<OrderResponse>(`${this.ordersUrl}/${orderId}`);
    }

    getAllOrders(): Observable<OrderResponse[]> {
        return this.http.get<OrderResponse[]>(this.ordersUrl);
    }

    getOrdersByBuyer(buyerId: string): Observable<OrderResponse[]> {
        return this.http.get<OrderResponse[]>(`${this.ordersUrl}/buyer/${buyerId}`);
    }

    getOrdersByStatus(status: string): Observable<OrderResponse[]> {
        return this.http.get<OrderResponse[]>(`${this.ordersUrl}/status/${status}`);
    }

    updateOrder(orderId: string, updateData: OrderUpdateDto): Observable<OrderResponse> {
        return this.http.put<OrderResponse>(`${this.ordersUrl}/${orderId}`, updateData);
    }

    deleteOrder(orderId: string): Observable<void> {
        return this.http.delete<void>(`${this.ordersUrl}/${orderId}`);
    }

    countOrdersByStatus(status: string): Observable<number> {
        return this.http.get<number>(`${this.ordersUrl}/count/status/${status}`);
    }

    getLatestOrdersByBuyer(buyerId: string): Observable<OrderResponse[]> {
        return this.http.get<OrderResponse[]>(`${this.ordersUrl}/latest/buyer/${buyerId}`);
    }

    // =============== Order Item Methods ===============

    createOrderItem(itemData: OrderItemCreateDto): Observable<OrderItem> {
        return this.http.post<OrderItem>(this.orderItemsUrl, itemData);
    }

    getOrderItemById(itemId: string): Observable<OrderItem> {
        return this.http.get<OrderItem>(`${this.orderItemsUrl}/${itemId}`);
    }

    getOrderItemsByOrder(orderId: string): Observable<OrderItem[]> {
        return this.http.get<OrderItem[]>(`${this.orderItemsUrl}/order/${orderId}`);
    }

    getOrderItemsByShop(shopId: string): Observable<OrderItem[]> {
        return this.http.get<OrderItem[]>(`${this.orderItemsUrl}/shop/${shopId}`);
    }

    getOrderItemsByShopAndStatus(shopId: string, status: string): Observable<OrderItem[]> {
        return this.http.get<OrderItem[]>(`${this.orderItemsUrl}/shop/${shopId}/status/${status}`);
    }

    updateOrderItem(itemId: string, updateData: OrderItemUpdateDto): Observable<OrderItem> {
        return this.http.put<OrderItem>(`${this.orderItemsUrl}/${itemId}`, updateData);
    }

    updateOrderItemStatus(itemId: string, status: string): Observable<OrderItem> {
        return this.http.patch<OrderItem>(`${this.orderItemsUrl}/${itemId}/status`, null, {
            params: { status }
        });
    }

    deleteOrderItem(itemId: string): Observable<void> {
        return this.http.delete<void>(`${this.orderItemsUrl}/${itemId}`);
    }

    calculateTotalValueByShopAndStatus(shopId: string, status: string): Observable<number> {
        return this.http.get<number>(`${this.orderItemsUrl}/shop/${shopId}/status/${status}/total`);
    }
}