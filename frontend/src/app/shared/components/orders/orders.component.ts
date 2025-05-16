import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule } from 'lucide-angular';
import { User, MoveRight, ShoppingCart, Package, Clock10, Truck, CircleCheckBig } from 'lucide-angular';


@Component({
  selector: 'app-orders',
  imports: [LucideAngularModule, CommonModule],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent {
  @Input() product: string = 'Product';
  @Input() price: number = 0.4;
  @Input() status: string = 'Delivered';

  readonly package = Package;
  readonly clock = Clock10;
  readonly truck = Truck;
  readonly checkcircle = CircleCheckBig;
}
