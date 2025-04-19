import { Component, Input } from '@angular/core';
import { LucideAngularModule, Package, Trash2, MapPinned } from 'lucide-angular';
import { CommonModule } from '@angular/common';

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

  @Input() shop :string = 'Shop';
  @Input() imageUrl :string = 'Shop';
  @Input() isVerified :Boolean = false;
  @Input() description :string = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam in dui mauris';
}
