import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, catchError, of, tap } from "rxjs";
import { 
  Order, 
  OrderItem, 
  OrderItemCreateDto, 
  OrderItemUpdateDto, 
  OrderResponse, 
  OrderUpdateDto, 
  CreateOrderDto 
} from "../../../models/order.models";
import { OrderService } from "./order.service";

@Injectable({
  providedIn: 'root'
})
export class OrderStateService {
  // Main order state subjects
  private ordersSubject = new BehaviorSubject<Order[]>([]);
  private currentOrderSubject = new BehaviorSubject<Order | null>(null);
  private orderItemsSubject = new BehaviorSubject<OrderItem[]>([]);
  
  // Utility subjects
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private errorSubject = new BehaviorSubject<string | null>(null);

  // Public observables
  public orders$ = this.ordersSubject.asObservable();
  public currentOrder$ = this.currentOrderSubject.asObservable();
  public orderItems$ = this.orderItemsSubject.asObservable();
  public loading$ = this.loadingSubject.asObservable();
  public error$ = this.errorSubject.asObservable();

  constructor(private orderService: OrderService) {}

  // =============== Order Methods ===============

  createOrder(orderData: CreateOrderDto): Observable<Order | null> {
    this.loadingSubject.next(true);
    return this.orderService.createOrder(orderData).pipe(
      tap(order => {
        const currentOrders = this.ordersSubject.value;
        this.ordersSubject.next([...currentOrders, order]);
        this.currentOrderSubject.next(order);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to create order');
        return of(null);
      })
    );
  }

  loadOrder(orderId: string): void {
    this.loadingSubject.next(true);
    this.orderService.getOrderById(orderId).pipe(
      tap(order => {
        this.currentOrderSubject.next(order);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to load order');
        return of(null);
      })
    ).subscribe();
  }

  loadOrders(): void {
    this.loadingSubject.next(true);
    this.orderService.getAllOrders().pipe(
      tap(orders => {
        this.ordersSubject.next(orders);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to load orders');
        return of(null);
      })
    ).subscribe();
  }

  loadOrdersByBuyer(buyerId: string): void {
    this.loadingSubject.next(true);
    this.orderService.getOrdersByBuyer(buyerId).pipe(
      tap(orders => {
        this.ordersSubject.next(orders);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to load buyer orders');
        return of(null);
      })
    ).subscribe();
  }

  updateOrder(orderId: string, updateData: OrderUpdateDto): Observable<Order | null> {
    this.loadingSubject.next(true);
    return this.orderService.updateOrder(orderId, updateData).pipe(
      tap(order => {
        // Update in orders list
        const orders = this.ordersSubject.value.map(o => 
          o.id === orderId ? order : o
        );
        this.ordersSubject.next(orders);
        
        // Update current order if it's the one being updated
        if (this.currentOrderSubject.value?.id === orderId) {
          this.currentOrderSubject.next(order);
        }
        
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to update order');
        return of(null);
      })
    );
  }

  deleteOrder(orderId: string): Observable<void> {
    this.loadingSubject.next(true);
    return this.orderService.deleteOrder(orderId).pipe(
      tap(() => {
        // Remove from orders list
        const orders = this.ordersSubject.value.filter(o => o.id !== orderId);
        this.ordersSubject.next(orders);
        
        // Clear current order if it's the one being deleted
        if (this.currentOrderSubject.value?.id === orderId) {
          this.currentOrderSubject.next(null);
        }
        
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to delete order');
        return of(void 0);
      })
    );
  }

  // =============== Order Item Methods ===============

  loadOrderItems(orderId: string): void {
    this.loadingSubject.next(true);
    this.orderService.getOrderItemsByOrder(orderId).pipe(
      tap(items => {
        this.orderItemsSubject.next(items);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to load order items');
        return of(null);
      })
    ).subscribe();
  }

  createOrderItem(itemData: OrderItemCreateDto): Observable<OrderItem | null> {
    this.loadingSubject.next(true);
    return this.orderService.createOrderItem(itemData).pipe(
      tap(item => {
        const currentItems = this.orderItemsSubject.value;
        this.orderItemsSubject.next([...currentItems, item]);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to create order item');
        return of(null);
      })
    );
  }

  updateOrderItem(itemId: string, updateData: OrderItemUpdateDto): Observable<OrderItem | null> {
    this.loadingSubject.next(true);
    return this.orderService.updateOrderItem(itemId, updateData).pipe(
      tap(item => {
        const items = this.orderItemsSubject.value.map(i => 
          i.id === itemId ? item : i
        );
        this.orderItemsSubject.next(items);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to update order item');
        return of(null);
      })
    );
  }

  deleteOrderItem(itemId: string): Observable<void> {
    this.loadingSubject.next(true);
    return this.orderService.deleteOrderItem(itemId).pipe(
      tap(() => {
        const items = this.orderItemsSubject.value.filter(i => i.id !== itemId);
        this.orderItemsSubject.next(items);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to delete order item');
        return of(void 0);
      })
    );
  }

  // =============== Utility Methods ===============

  getCurrentOrderValue(): Order | null {
    return this.currentOrderSubject.value;
  }

  getOrdersValue(): Order[] {
    return this.ordersSubject.value;
  }

  getOrderItemsValue(): OrderItem[] {
    return this.orderItemsSubject.value;
  }

  clearError(): void {
    this.errorSubject.next(null);
  }

  resetState(): void {
    this.ordersSubject.next([]);
    this.currentOrderSubject.next(null);
    this.orderItemsSubject.next([]);
    this.loadingSubject.next(false);
    this.errorSubject.next(null);
  }
}