import { Component } from '@angular/core';
import { StatsBoxComponent } from '../../../shared/components/stats-box/stats-box.component';
import { ProductItemComponent } from '../../../shared/components/product-item/product-item.component';
import { LucideAngularModule, MapPinned  } from 'lucide-angular';

@Component({
  selector: 'app-shop-profile',
  standalone: true,
  imports: [StatsBoxComponent, ProductItemComponent, LucideAngularModule],
  templateUrl: './shop-profile.component.html',
  styleUrl: './shop-profile.component.css'
})
export class ShopProfileComponent {

  shopBanner :string  = 'assets/images/shop-banner.jpg';
  readonly mapIcon = MapPinned;

}
