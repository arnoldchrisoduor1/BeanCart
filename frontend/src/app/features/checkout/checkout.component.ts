import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { combineLatest, Subscription, forkJoin, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { ButtonComponent } from '../../shared/components/button/button.component';
import { InputComponent } from '../../shared/components/input/input.component';
import { StripePaymentComponent } from '../stripe-payment/stripe-payment.component';

import { CartStateService } from '../../core/services/cart/cart-state.service';
import { AuthStateService } from '../../core/auth/auth-state.service';
import { OrderStateService } from '../../core/services/Orders/order-state.service';

import { CartResponse } from '../../models/cart.model';
import { User } from '../../models/user.model';
import { CreateOrderDto, OrderItemCreateDto } from '../../models/order.models';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    ButtonComponent, 
    InputComponent,
    StripePaymentComponent
  ],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit, OnDestroy {
  currentStep = 1; // 1: Shipping, 2: Review, 3: Payment
  shippingForm: FormGroup;
  cart: CartResponse | null = null;
  currentUser: User | null = null;
  isLoading = false;
  error: string | null = null;
  createdOrderId: string | null = null;
  
  private subscriptions: Subscription[] = [];
  
  constructor(
    private fb: FormBuilder,
    private cartState: CartStateService,
    private authState: AuthStateService,
    private orderState: OrderStateService,
    private router: Router
  ) {
    this.shippingForm = this.fb.group({
      firstName: ['', Validators.required],
      addressLine1: ['', Validators.required],
      addressLine2: [''],
      city: ['', Validators.required],
      state: ['', Validators.required],
      zipCode: ['', Validators.required],
      phone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      shippingMethod: ['standard', Validators.required],
      notes: ['']
    });
  }
  
  ngOnInit(): void {
    // Use combineLatest to watch both loading and cart state
    this.cartState.loadCart();
    const cartSub = combineLatest([
      this.cartState.loading$,
      this.cartState.cart$
    ]).subscribe(([loading, cart]) => {
      // Only proceed when loading is false (completed) and cart exists
      if (!loading) {
        this.cart = cart;
        
        // If cart is null or empty after loading is complete, redirect to cart page
        if (!cart || cart.items.length === 0) {
          this.router.navigate(['/cart']);
        }
      }
    });
    
    this.subscriptions.push(cartSub);
    
    // Load the cart if not already loaded
    if (!this.cartState.getCartValue()) {
      this.cartState.loadCart();
    }
    
    // User data
    this.subscriptions.push(
      this.authState.currentUser$.subscribe(user => {
        this.currentUser = user;
        if (user && this.shippingForm) {
          this.shippingForm.patchValue({
            firstName: user.firstName + ' ' + user.lastName || '',
            email: user.email || '',
            phone: user.phone || ''
          });
        }
      })
    );
  
    // Order errors
    this.subscriptions.push(
      this.orderState.error$.subscribe(error => {
        if (error) {
          this.error = error;
        }
      })
    );
  }
  
  nextStep(): void {
    if (this.currentStep === 1 && this.shippingForm.valid) {
      this.currentStep = 2;
    } else if (this.currentStep === 2) {
      this.createOrder();
    }
  }
  
  previousStep(): void {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }
  
  createOrder(): void {
    if (!this.cart || !this.currentUser) {
      this.error = "Missing cart or user information";
      return;
    }
    
    this.isLoading = true;
    
    // Prepare shipping address string
    const form = this.shippingForm.value;
    const shippingAddress = `${form.firstName}, ${form.addressLine1}, ${form.addressLine2 ? form.addressLine2 + ', ' : ''}${form.city}, ${form.state} ${form.zipCode}`;
    
    // Create order DTO
    const orderData: CreateOrderDto = {
      buyerId: this.currentUser.userId,
      totalAmount: this.calculateTotalWithShipping(),
      status: 'pending',
      shippingAddress: shippingAddress,
      shippingMethod: form.shippingMethod,
      notes: form.notes || ''
    };
    
    // Call order service to create order
    this.orderState.createOrder(orderData)
      .pipe(
        switchMap(order => {
          if (!order || !this.cart) {
            return of(null);
          }
          
          // After order is created, create order items for each cart item
          const orderItemObservables = this.cart.items.map(cartItem => {
            const orderItem: OrderItemCreateDto = {
              orderId: order.id,
              productId: cartItem.productId,
              quantity: cartItem.quantity,
              unitPrice: cartItem.productPrice,
              shopId: cartItem.shopId,
              status: 'pending',
              // Add any other required fields for your OrderItemCreateDto
            };

            console.log("Creating order items with: ", orderItem);
            
            return this.orderState.createOrderItem(orderItem);
          });
          
          // Use forkJoin to wait for all order items to be created
          return forkJoin(orderItemObservables).pipe(
            switchMap(orderItems => {
              // Return the original order object
              return of(order);
            })
          );
        })
      )
      .subscribe({
        next: (order) => {
          if (order) {
            this.createdOrderId = order.id;
            this.currentStep = 3;
            this.error = null;
          } else {
            this.error = "Failed to create order";
          }
          this.isLoading = false;
        },
        error: (err) => {
          this.error = err.message || "An error occurred while creating your order";
          this.isLoading = false;
        }
      });
  }
  
  onPaymentProcessing(isProcessing: boolean): void {
    this.isLoading = isProcessing;
  }
  
  onPaymentSuccess(response: any): void {
    // Clear cart after successful payment
    this.cartState.clearCart().subscribe(() => {
      // Navigate to order confirmation page
      // this.router.navigate(['/order-confirmation', this.createdOrderId]);
      this.router.navigate(['/user/profile']);
    });
  }
  
  onPaymentError(errorMessage: string): void {
    this.error = errorMessage;
  }
  
  getShippingCost(): number {
    const method = this.shippingForm.get('shippingMethod')?.value;
    if (method === 'express') return 9.99;
    if (method === 'overnight') return 19.99;
    return 0; // standard shipping
  }
  
  calculateTotalWithShipping(): number {
    if (!this.cart) return 0;
    return this.cart.total + this.getShippingCost();
  }
  
  get formattedShippingAddress(): string {
    if (!this.shippingForm.valid) return '';
    
    const form = this.shippingForm.value;
    return `${form.firstName}, ${form.addressLine1}, ${form.addressLine2 ? form.addressLine2 + ', ' : ''}${form.city}, ${form.state} ${form.zipCode}`;
  }
  
  ngOnDestroy(): void {
    // Clean up subscriptions
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}