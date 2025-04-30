import { Component, Input } from '@angular/core';
import { LucideAngularModule, Star, Heart, ShoppingCart, Expand } from 'lucide-angular';
import { Product } from '../../../models/product.model';
import { CommonModule } from '@angular/common';
import { NumberPipe } from '../../pipes/number.pipe';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-item',
  standalone: true,
  imports: [LucideAngularModule, CommonModule, NumberPipe],
  templateUrl: './product-item.component.html',
  styleUrl: './product-item.component.css'
})
export class ProductItemComponent {
  @Input() product!: Product; // This will be passed from the parent component

  // Icons
  readonly reviewStar = Star;
  readonly heartIcon = Heart;
  readonly shoppingCart = ShoppingCart;
  readonly expandIcon = Expand;

  constructor(private router: Router) {}

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