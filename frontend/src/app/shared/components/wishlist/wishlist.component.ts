import { Component, Input } from '@angular/core';
import { ShoppingCart, Package  } from 'lucide-angular';
import { LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-wishlist',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './wishlist.component.html',
  styleUrl: './wishlist.component.css'
})
export class WishlistComponent {
  @Input() product: string = 'Product';
  @Input() price: number = 0.4;

  readonly shoppingCart = ShoppingCart;
  readonly package = Package;

}
