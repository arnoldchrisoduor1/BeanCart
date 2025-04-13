import { Component } from '@angular/core';
import { OrdersComponent } from '../../../shared/components/orders/orders.component';
import { StatsBoxComponent } from "../../../shared/components/stats-box/stats-box.component";

@Component({
  selector: 'app-orders-page',
  standalone: true,
  imports: [OrdersComponent, StatsBoxComponent],
  templateUrl: './orders-page.component.html',
  styleUrl: './orders-page.component.css'
})
export class OrdersPageComponent {

}
