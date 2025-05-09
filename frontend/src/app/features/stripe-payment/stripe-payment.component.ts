import { Component, OnInit, ViewChild, ElementRef, Input, Output, EventEmitter, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ButtonComponent } from '../../shared/components/button/button.component';
import { StripeService } from '../../core/services/stripe/stripe.service';

@Component({
  selector: 'app-stripe-payment',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ButtonComponent],
  template: `
    <div class="mb-6">
      <h3 class="text-lg font-medium mb-3">Card Information</h3>
      
      <!-- Payment method selection -->
      <div class="mb-4">
        <div class="flex flex-wrap gap-3 mb-4">
          <div 
            class="border rounded-md p-3 flex gap-2 items-center cursor-pointer transition-all"
            [class.border-indigo-500]="paymentMethod === 'card'"
            [class.bg-indigo-50]="paymentMethod === 'card'"
            [class.border-gray-300]="paymentMethod !== 'card'"
            (click)="selectPaymentMethod('card')">
            <div class="h-6 w-6 flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
                <rect x="1" y="4" width="22" height="16" rx="2" ry="2"></rect>
                <line x1="1" y1="10" x2="23" y2="10"></line>
              </svg>
            </div>
            <span>Credit Card</span>
          </div>
          
          <div 
            class="border rounded-md p-3 flex gap-2 items-center cursor-pointer transition-all text-gray-500"
            [class.border-gray-300]="true">
            <div class="h-6 w-6 flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
                <path d="M12 2v20M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
              </svg>
            </div>
            <span>Other methods (coming soon)</span>
          </div>
        </div>
      </div>

      <!-- Card form -->
      <div *ngIf="paymentMethod === 'card'">
        <div class="mb-4">
          <label for="card-element" class="block text-sm font-medium text-gray-700 mb-1">Card details</label>
          <div 
            id="card-element" 
            #cardElement 
            class="border border-gray-300 rounded-md p-3 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
            <!-- Stripe Elements will be inserted here -->
          </div>
          <div id="card-errors" class="text-red-500 text-sm mt-1" #cardErrors></div>
        </div>
        
        <div class="mb-4">
          <div class="flex items-center">
            <input
              type="checkbox"
              id="save-card"
              [formControl]="saveCardControl"
              class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
            />
            <label for="save-card" class="ml-2 block text-sm text-gray-900">
              Save this card for future purchases
            </label>
          </div>
        </div>
      </div>
    </div>
    
    <app-button 
      [defaultText]="'Pay Now'"
      [loadingText]="'Processing...'"
      [color]="'indigo'" 
      [isLoading]="isLoading"
      [disabled]="!isFormValid || isLoading"
      (clicked)="processPayment()">
    </app-button>
  `,
  styles: []
})
export class StripePaymentComponent implements OnInit, AfterViewInit {
  @ViewChild('cardElement') cardElement!: ElementRef;
  @ViewChild('cardErrors') cardErrors!: ElementRef;
  
  @Input() amount: number = 0;
  @Input() orderId: string = '';
  
  @Output() paymentProcessing = new EventEmitter<boolean>();
  @Output() paymentSuccess = new EventEmitter<any>();
  @Output() paymentError = new EventEmitter<string>();
  
  card: any;
  stripe: any;
  isLoading: boolean = false;
  isFormValid: boolean = false;
  clientSecret: string = '';
  paymentIntentId: string = '';
  paymentMethod: string = 'card';
  saveCardControl!: ReturnType<FormBuilder['control']>;
  
  constructor(
    private stripeService: StripeService,
    private fb: FormBuilder
  ) {}
  
  ngOnInit(): void {
    this.saveCardControl = this.fb.control(false);
    
    // Load Stripe and ensure it's available
    this.loadStripe().then(() => {
      // Create payment intent after Stripe is loaded
      this.createPaymentIntent();
    });
  }
  
  async loadStripe(): Promise<void> {
    try {
      // Get Stripe instance
      this.stripe = await this.stripeService.getStripe();
      
      // Verify that we have a proper Stripe instance
      if (!this.stripe || typeof this.stripe.elements !== 'function') {
        console.error('Stripe failed to load properly. Check your Stripe service implementation.');
        this.paymentError.emit('Payment system is unavailable. Please try again later.');
      }
    } catch (error) {
      console.error('Error loading Stripe:', error);
      this.paymentError.emit('Payment system is unavailable. Please try again later.');
    }
  }
  
  ngAfterViewInit(): void {
    // Initialize Stripe Elements after view is initialized and DOM is ready
    if (this.clientSecret) {
      this.initStripeElements();
    } else {
      // If clientSecret isn't ready yet, wait for it
      const checkInterval = setInterval(() => {
        if (this.clientSecret) {
          this.initStripeElements();
          clearInterval(checkInterval);
        }
      }, 100);
      
      // Clear interval after 10 seconds to prevent infinite checking
      setTimeout(() => clearInterval(checkInterval), 10000);
    }
  }
  
  createPaymentIntent(): void {
    console.log("----creating payment intent-----");
    if (!this.amount || !this.orderId) {
      console.error('Amount and orderId are required');
      return;
    }
    
    this.isLoading = true;
    this.paymentProcessing.emit(true);
    
    this.stripeService.createPaymentIntent(this.amount, this.orderId)
      .subscribe({
        next: (response) => {
          this.clientSecret = response.clientSecret;
          this.paymentIntentId = response.paymentIntentId;
          this.isLoading = false;
          this.paymentProcessing.emit(false);
          
          // Initialize Stripe Elements once we have the client secret
          if (this.cardElement?.nativeElement) {
            this.initStripeElements();
          }
        },
        error: (error) => {
          console.error('Error creating payment intent:', error);
          this.paymentError.emit('Failed to initialize payment. Please try again.');
          this.isLoading = false;
          this.paymentProcessing.emit(false);
        }
      });
  }
  
  initStripeElements(): void {
    console.log("----initializing stripe elements-------");
    if (!this.stripe || !this.cardElement?.nativeElement) {
      console.error('Stripe or cardElement not available');
      return;
    }
    
    // Check if Stripe is properly initialized
    if (typeof this.stripe.elements !== 'function') {
      console.error('Stripe is not properly initialized. Elements function is missing.');
      this.paymentError.emit('Payment system is unavailable. Please try again later.');
      return;
    }
    
    const elements = this.stripe.elements();
    
    // Create card element
    this.card = elements.create('card', {
      style: {
        base: {
          fontSize: '16px',
          color: '#32325d',
          fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
          fontSmoothing: 'antialiased',
          '::placeholder': {
            color: '#aab7c4'
          }
        },
        invalid: {
          color: '#fa755a',
          iconColor: '#fa755a'
        }
      }
    });
    
    // Mount card element
    this.card.mount(this.cardElement.nativeElement);
    
    // Handle real-time validation errors from the card element
    this.card.addEventListener('change', (event: any) => {
      if (event.error) {
        this.cardErrors.nativeElement.textContent = event.error.message;
        this.isFormValid = false;
      } else {
        this.cardErrors.nativeElement.textContent = '';
        this.isFormValid = true;
      }
    });
  }
  
  selectPaymentMethod(method: string): void {
    this.paymentMethod = method;
    
    // Re-initialize card element if switching to card payment
    if (method === 'card' && !this.card && this.cardElement?.nativeElement) {
      setTimeout(() => this.initStripeElements(), 0);
    }
    console.log('====payment method selected====');
  }
  
  processPayment(): void {
    console.log("----- Stripe Payment processing started----")
    if (!this.isFormValid || !this.clientSecret) {
      return;
    }
    
    this.isLoading = true;
    this.paymentProcessing.emit(true);
    
    console.log("----confirming card payment for stripe------");
    this.stripe.confirmCardPayment(this.clientSecret, {
      payment_method: {
        card: this.card,
        billing_details: {
          // You can add billing details from form if needed
        }
      },
      setup_future_usage: this.saveCardControl.value ? 'off_session' : undefined
    })
    .then((result: any) => {
      this.isLoading = false;
      this.paymentProcessing.emit(false);
      
      if (result.error) {
        // Show error to your customer
        this.cardErrors.nativeElement.textContent = result.error.message;
        this.paymentError.emit(result.error.message);
      } else {
        // Payment succeeded!
        console.log("=====Payment processing succeeded======");
        if (result.paymentIntent.status === 'succeeded') {
          // Confirm payment on server
          this.confirmPayment();
        }
      }
    });
  }
  
  confirmPayment(): void {
    console.log("------ Confirming payment--------");
    this.stripeService.confirmPayment({
      paymentIntentId: this.paymentIntentId,
      orderId: this.orderId
    }).subscribe({
      next: (response) => {
        // Emit success event with payment response
        console.log("=======payment was successfully confirmed===========");
        this.paymentSuccess.emit(response);
      },
      error: (error) => {
        console.error('Error confirming payment:', error);
        this.paymentError.emit('Payment was processed but confirmation failed. Please contact support.');
      }
    });
  }
  
  ngOnDestroy(): void {
    // Clean up Stripe Elements
    if (this.card) {
      this.card.destroy();
    }
  }
}