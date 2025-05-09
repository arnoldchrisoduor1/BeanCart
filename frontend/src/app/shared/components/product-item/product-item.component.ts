import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { LucideAngularModule, Star, Heart, ShoppingCart, Expand } from 'lucide-angular';
import { Product } from '../../../models/product.model';
import { CommonModule } from '@angular/common';
import { NumberPipe } from '../../pipes/number.pipe';
import { Router } from '@angular/router';
import { CartItemDto } from '../../../models/cart.model';
import { CartStateService } from '../../../core/services/cart/cart-state.service';
import { Observable, map, firstValueFrom  } from 'rxjs';

@Component({
  selector: 'app-product-item',
  standalone: true,
  imports: [LucideAngularModule, CommonModule, NumberPipe],
  templateUrl: './product-item.component.html',
  styleUrl: './product-item.component.css'
})
export class ProductItemComponent implements OnInit, OnChanges {
  @Input() product!: Product;
  quantity = 1;
  isInCart$!: Observable<boolean>;

  // Icons
  readonly reviewStar = Star;
  readonly heartIcon = Heart;
  readonly shoppingCart = ShoppingCart;
  readonly expandIcon = Expand;

  constructor(private router: Router, private cartState: CartStateService) {}

  ngOnInit() {
    // Optional: safe default if product already exists
    if (this.product) {
      this.setIsInCartObservable();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['product'] && changes['product'].currentValue) {
      this.setIsInCartObservable();
    }
  }

  private setIsInCartObservable() {
    this.isInCart$ = this.cartState.cart$.pipe(
      map(cart => {
        if (!cart || !this.product?.id) return false;
        return cart.items.some(item => item.productId === this.product.id);
      })
    );
  }

  async toggleCart(event: Event) {
    event.stopPropagation();
  
    const isInCart = await firstValueFrom(this.isInCart$);
  
    if (isInCart) {
      const cart = this.cartState.getCartValue();
      const item = cart?.items.find(i => i.productId === this.product.id);
      if (item) {
        console.log("Removing from cart");
        this.cartState.removeItem(item.id).subscribe();
        console.log("Removed from cart");
      }
    } else {
      console.log("Adding to cart");
      const item: CartItemDto = {
        productId: this.product.id,
        shopId: this.product.shopId,
        quantity: this.quantity
      };
      console.log("Adding item to cart with: ", item);
      this.cartState.addItem(item).subscribe();
      console.log("Added to cart");
    }
  }

  // Default image if product doesn't have one
  get productImage(): string {
    return this.product?.imageUrl || 'assets/images/default-product.jpg';
  }

  // Calculate discount percentage if discount exists
  get discountPercentage(): number | null {
    if (this.product?.discount && this.product?.price) {
      return Math.round((this.product.discount / this.product.price) * 100);
    }
    return null;
  }

  // Calculate discounted price
  get discountedPrice(): number | null {
    if (this.product?.discount && this.product?.price) {
      return this.product.price - this.product.discount;
    }
    return null;
  }

  navigateToProductDetails() {
    this.router.navigate(['/products/product-details', this.product.id]);
  }
}