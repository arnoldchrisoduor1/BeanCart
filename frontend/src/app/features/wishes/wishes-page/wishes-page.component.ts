import { Component } from '@angular/core';
import { WishlistComponent } from "../../../shared/components/wishlist/wishlist.component";
import { StatsBoxComponent } from "../../../shared/components/stats-box/stats-box.component";

@Component({
  selector: 'app-wishes-page',
  imports: [WishlistComponent, StatsBoxComponent],
  templateUrl: './wishes-page.component.html',
  styleUrl: './wishes-page.component.css'
})
export class WishesPageComponent {

}
