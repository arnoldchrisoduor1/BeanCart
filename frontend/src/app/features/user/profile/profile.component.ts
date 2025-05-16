import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthStateService } from '../../../core/auth/auth-state.service';
import { Subscription } from 'rxjs';
import { LucideAngularModule } from 'lucide-angular';
import { User, MoveRight, ShoppingCart, Package, Smartphone, CreditCard, ChevronRight, Store } from 'lucide-angular';
import { WishlistComponent } from '../../../shared/components/wishlist/wishlist.component';
import { OrdersComponent } from '../../../shared/components/orders/orders.component';
import { UpdateProfileModalComponent } from '../../../shared/components/update-profile-modal/update-profile-modal.component';
import { AuthService } from '../../../core/auth/auth.service';
import { RouterLink } from '@angular/router';
import { StatsBoxComponent } from "../../../shared/components/stats-box/stats-box.component";
import { ShopTileComponent } from "../../../shared/components/shop-tile/shop-tile.component";
import { ShopService } from '../../../core/services/shop/shop.service';
import { ShopStateService } from '../../../core/services/shop/shop-state.service';
import { Shop } from '../../../models/shop.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile',
  imports: [
    LucideAngularModule, 
    WishlistComponent, 
    OrdersComponent, 
    UpdateProfileModalComponent, 
    RouterLink, 
    StatsBoxComponent, 
    ShopTileComponent,
    CommonModule
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit, OnDestroy {
  userProfile: any = null;
  private authSubscription!: Subscription;
  private shopSubscription!: Subscription;
  isLoggedIn = false;
  isLoadingProfile = true;
  
  // Add properties for shops
  userShops: Shop[] = [];
  currentShop: Shop | null = null;
  isLoadingShops = false;

  readonly userIcon = User;
  readonly moveRight = MoveRight;
  readonly shoppingCart = ShoppingCart;
  readonly package = Package;
  readonly smartphone = Smartphone;
  readonly creditCard = CreditCard;
  readonly chevronRight = ChevronRight;
  readonly store = Store;

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
    private authService: AuthService,
    private shopService: ShopService,
    private shopStateService: ShopStateService
  ) {}

  ngOnInit() {
    if (!this.isLoggedIn) {
      this.isLoadingProfile = true;
    }
    this.authSubscription = this.authStateService.currentUser$.subscribe(user => {
      this.isLoggedIn = !!user;
      this.userProfile = user;
      
      
      // When user is logged in, fetch their shops
      if (this.isLoggedIn) {
        this.isLoadingProfile = false;
        this.fetchUserShops();
      }
    });

    // Subscribe to user shops from state
    this.shopSubscription = this.shopStateService.userShops$.subscribe(shops => {
      this.userShops = shops;
    });

    // Subscribe to current shop from state
    this.shopStateService.currentShop$.subscribe(shop => {
      this.currentShop = shop;
    });
  }

  fetchUserShops() {
    this.isLoadingShops = true;
    this.shopService.getCurrentUserShops().subscribe({
      next: () => {
        // No need to set userShops here as it's handled by the subscription
        this.isLoadingShops = false;
      },
      error: (err) => {
        console.error('Error fetching user shops:', err);
        this.isLoadingShops = false;
      }
    });
  }

  selectShop(shop: Shop) {
    this.shopStateService.setShop(shop);
  }

  onDeleteShop(shopId: string) {
    if (confirm('Are you sure you want to delete this shop?')) {
      this.shopService.deleteShop({ shopId }).subscribe({
        next: () => {
          // After successful deletion, refresh the shop list
          this.fetchUserShops();
          
          // If the deleted shop was the current shop, reset the current shop
          if (this.currentShop?.id === shopId) {
            this.shopStateService.setShop(null as any);
          }
        },
        error: (err) => {
          console.error('Error deleting shop:', err);
        }
      });
    }
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
    if (this.shopSubscription) {
      this.shopSubscription.unsubscribe();
    }
  }
}