import { Component } from '@angular/core';
import { CartItemComponent } from "../../shared/components/cart-item/cart-item.component";
import { StatsBoxComponent } from '../../shared/components/stats-box/stats-box.component';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CartItemComponent, StatsBoxComponent],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {

}
