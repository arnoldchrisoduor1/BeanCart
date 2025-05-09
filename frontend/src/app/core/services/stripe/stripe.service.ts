import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, from } from 'rxjs';
import { environment } from '../../../../environments/environment';

export interface PaymentIntentResponse {
  clientSecret: string;
  paymentIntentId: string;
}

export interface PaymentConfirmation {
  paymentIntentId: string;
  orderId: string;
}

@Injectable({
  providedIn: 'root'
})
export class StripeService {
  private stripe: any = null;
  private baseUrl = `${environment.apiUrl}/api/payments`;
  
  constructor(private http: HttpClient) {
    // Don't initialize Stripe in constructor
    // We'll load it on demand
  }

  /**
   * Get Stripe instance (lazy-loading)
   */
  async loadStripe(): Promise<any> {
    if (this.stripe) {
      return this.stripe;
    }

    // Check if Stripe is already loaded
    if (window.hasOwnProperty('Stripe')) {
      this.stripe = (window as any).Stripe(environment.stripe.publishableKey);
      return this.stripe;
    }

    // If Stripe is not loaded, load it dynamically
    return new Promise((resolve, reject) => {
      const script = document.createElement('script');
      script.src = 'https://js.stripe.com/v3/';
      script.async = true;
      script.onload = () => {
        this.stripe = (window as any).Stripe(environment.stripe.publishableKey);
        resolve(this.stripe);
      };
      script.onerror = (error) => {
        reject(new Error('Failed to load Stripe.js'));
      };
      document.body.appendChild(script);
    });
  }

  /**
   * Get Stripe instance
   */
  async getStripe(): Promise<any> {
    return this.loadStripe();
  }

  /**
   * Create a payment intent on the server
   */
  createPaymentIntent(amount: number, orderId: string): Observable<PaymentIntentResponse> {
    return this.http.post<PaymentIntentResponse>(`${this.baseUrl}/create-intent`, {
      amount,
      orderId,
      currency: 'usd'  // You can make this configurable if needed
    });
  }

  /**
   * Confirm payment was successful
   */
  confirmPayment(paymentData: PaymentConfirmation): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/confirm`, paymentData);
  }

  /**
   * Get payment status
   */
  getPaymentStatus(paymentIntentId: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/status/${paymentIntentId}`);
  }
}