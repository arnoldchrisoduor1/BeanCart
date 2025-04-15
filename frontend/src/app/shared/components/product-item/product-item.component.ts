import { Component } from '@angular/core';
import { LucideAngularModule, Star, Heart, ShoppingCart, Expand} from 'lucide-angular';


@Component({
  selector: 'app-product-item',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './product-item.component.html',
  styleUrl: './product-item.component.css'
})
export class ProductItemComponent {

  readonly reviewStar = Star;
  readonly heartIcon = Heart;
  readonly shoppingCart = ShoppingCart;
  readonly expandIcon = Expand;

  ProductImage :string  = 'assets/images/product1.jpg';

}
