import { Component, Input } from '@angular/core';
import { LucideAngularModule, Trash2, Package, MapPinned, Plus, Minus} from 'lucide-angular';


@Component({
  selector: 'app-cart-item',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './cart-item.component.html',
  styleUrl: './cart-item.component.css'
})
export class CartItemComponent {
  @Input() product: string = 'Product';
  @Input() price: number = 1.99;

  readonly package = Package;
  readonly trash = Trash2;
  readonly map = MapPinned;
  readonly plus = Plus;
  readonly minus = Minus

  onDeleteClick(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    
  }

}
