import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LucideAngularModule, Package, Trash2, MapPinned } from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { Shop } from '../../../models/shop.model';

@Component({
  selector: 'app-shop-tile',
  standalone: true,
  imports: [LucideAngularModule, CommonModule],
  templateUrl: './shop-tile.component.html',
  styleUrl: './shop-tile.component.css'
})
export class ShopTileComponent {
  readonly package = Package;
  readonly trash = Trash2;
  readonly map = MapPinned;

  @Input() shop!: Shop;
  @Input() isSelected: boolean = false;
  @Output() deleteShop = new EventEmitter<string>();

  onDeleteClick(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    if (this.shop && this.shop.id) {
      this.deleteShop.emit(this.shop.id);
    }
  }
}