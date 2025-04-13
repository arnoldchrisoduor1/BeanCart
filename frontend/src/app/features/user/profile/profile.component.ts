import { Component } from '@angular/core';
import { AuthStateService } from '../../../core/auth/auth-state.service';
import { Subscription } from 'rxjs';
import { LucideAngularModule } from 'lucide-angular';
import { User, MoveRight, ShoppingCart, Package  } from 'lucide-angular';
import { WishlistComponent } from '../../../shared/components/wishlist/wishlist.component';
import { OrdersComponent } from '../../../shared/components/orders/orders.component';

@Component({
  selector: 'app-profile',
  imports: [LucideAngularModule, WishlistComponent, OrdersComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

  readonly userIcon = User;
  readonly moveRight = MoveRight ;
  readonly shoppingCart = ShoppingCart;
  readonly package = Package;

  userProfile: any = null;
  private authSubsription!: Subscription;
  isLoggedIn = false;

  constructor(
    private authStateService: AuthStateService
  ) {}

  ngOnInit() {
    this.authSubsription = this.authStateService.currentUser$.subscribe(user => {
      this.isLoggedIn = !!user;
      this.userProfile = user;
    })
  }

  ngOnDestroy() {
    if (this.authSubsription) {
      this.authSubsription.unsubscribe();
    }
  }

}
