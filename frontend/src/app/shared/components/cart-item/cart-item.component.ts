import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LucideAngularModule, Trash2, Package, MapPinned, Plus, Minus} from 'lucide-angular';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cart-item',
  standalone: true,
  imports: [LucideAngularModule, CommonModule],
  templateUrl: './cart-item.component.html',
  styleUrl: './cart-item.component.css'
})
export class CartItemComponent {
  @Input() productName!: string;
  @Input() price!: number;
  @Input() quantity!: number;
  @Input() imageUrl!: string;
  @Input() totalPrice!: number;
  @Input() itemId!: string;

  // This will emit the events to the parent component.
  @Output() deleteItem = new EventEmitter<string>();
  @Output() increaseQuantity = new EventEmitter<string>();
  @Output() decreaseQuantity = new EventEmitter<string>();

  readonly package = Package;
  readonly trash = Trash2;
  readonly map = MapPinned;
  readonly plus = Plus;
  readonly minus = Minus;

  onDeleteClick(): void {
    console.log("Sending the delete quantity event to cart parent");
    this.deleteItem.emit(this.itemId);
  }

  onIncreaseQuantity(): void {
    console.log("Sending the increase quantity event to cart parent");
    this.increaseQuantity.emit(this.itemId);
  }

  onDecreaseQuantity(): void {
    console.log("Sending the decrease quantity event to cart parent");
    if (this.quantity > 1) {
      this.decreaseQuantity.emit(this.itemId);
    }
  }
}