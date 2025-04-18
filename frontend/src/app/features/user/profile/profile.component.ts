import { Component } from '@angular/core';
import { AuthStateService } from '../../../core/auth/auth-state.service';
import { Subscription } from 'rxjs';
import { LucideAngularModule } from 'lucide-angular';
import { User, MoveRight, ShoppingCart, Package  } from 'lucide-angular';
import { WishlistComponent } from '../../../shared/components/wishlist/wishlist.component';
import { OrdersComponent } from '../../../shared/components/orders/orders.component';
import { UpdateProfileModalComponent } from '../../../shared/components/update-profile-modal/update-profile-modal.component';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-profile',
  imports: [LucideAngularModule, WishlistComponent, OrdersComponent, UpdateProfileModalComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

  userProfile: any = null;
  private authSubsription!: Subscription;
  isLoggedIn = false;

  readonly userIcon = User;
  readonly moveRight = MoveRight ;
  readonly shoppingCart = ShoppingCart;
  readonly package = Package;

  isModalVisible = false;

  openModal() {
    console.log("Opening modal");
    this.isModalVisible = true;
  }
  
  closeModal() {
    console.log("Closing modal");
    this.isModalVisible = false;
  }

  constructor(
    private authStateService: AuthStateService,
    private authService: AuthService
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
