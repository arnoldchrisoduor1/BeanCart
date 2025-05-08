import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartStateService } from '../../core/services/cart/cart-state.service';
import { CartResponse, CartItemUpdateDto } from '../../models/cart.model';
import { CartItemComponent } from '../../shared/components/cart-item/cart-item.component';
import { StatsBoxComponent } from '../../shared/components/stats-box/stats-box.component';
import { ButtonComponent } from "../../shared/components/button/button.component";

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, CartItemComponent, StatsBoxComponent, ButtonComponent],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
  isLoading = false;
  error: string | null = null;
  cart: CartResponse | null = null;

  constructor(public cartState: CartStateService) {}

  ngOnInit(): void {
    this.cartState.loadCart();
    
    this.cartState.loading$.subscribe(loading => {
      this.isLoading = loading;
    });
    
    this.cartState.error.subscribe(err => {
      this.error = err;
    });
    
    this.cartState.cart$.subscribe(cartData => {
      this.cart = cartData;
      console.log("Cart: ", this.cart);
    });
  }


  onDeleteItem(itemId: string): void {
    console.log("Attempting to delete item", itemId);
    this.cartState.removeItem(itemId).subscribe({
      next: (cart) => {
        console.log('Item removed successfully', cart);
      },
      error: (err) => {
        console.error('Error removing item:', err);
      }
    });
  }

  onIncreaseQuantity(itemId: string): void {
    console.log("Attempting to increase item", itemId);
    const item = this.cart?.items.find(i => i.id === itemId);
    if (item) {
      const update: CartItemUpdateDto = {
        quantity: item.quantity + 1
      };
      
      this.cartState.updateItem(itemId, update).subscribe({
        next: (cart) => {
          console.log('Quantity increased successfully', cart);
        },
        error: (err) => {
          console.error('Error increasing quantity:', err);
        }
      });
    }
  }

  onDecreaseQuantity(itemId: string): void {
    console.log("Attempting to decrease item", itemId);
    const item = this.cart?.items.find(i => i.id === itemId);
    if (item && item.quantity > 1) {
      const update: CartItemUpdateDto = {
        quantity: item.quantity - 1
      };
      
      this.cartState.updateItem(itemId, update).subscribe({
        next: (cart) => {
          console.log('Quantity decreased successfully', cart);
        },
        error: (err) => {
          console.error('Error decreasing quantity:', err);
        }
      });
    }
  }

  getTotalCartPrice(): number {
    console.log("Getting total cart price");
    if (!this.cart || !this.cart.items || this.cart.items.length === 0) {
      return 0;
    }
    
    return this.cart.items.reduce((sum, item) => sum + item.totalPrice, 0);
  }
}