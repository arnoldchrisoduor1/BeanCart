import { Component, OnInit, OnDestroy, ViewChildren, QueryList, ElementRef } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ProductStateService } from '../../../core/services/product/product-state.service';
import { ShopStateService } from '../../../core/services/shop/shop-state.service';
import { Product } from '../../../models/product.model';
import { Category } from '../../../models/category.model';
import { StatsBoxComponent } from "../../../shared/components/stats-box/stats-box.component";
import { ProductItemComponent } from "../../../shared/components/product-item/product-item.component";
import { LucideAngularModule, MapPin, ChevronLeft, ChevronRight } from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-shop-profile',
  templateUrl: './shop-profile.component.html',
  styleUrls: ['./shop-profile.component.css'],
  standalone: true,
  imports: [
    StatsBoxComponent, 
    ProductItemComponent,
    LucideAngularModule,
    CommonModule,
    RouterModule
  ]
})
export class ShopProfileComponent implements OnInit, OnDestroy {
  shopBanner = 'https://via.placeholder.com/1200x400';
  shopId: string = '';
  shopProfile: any = null;
  categoryNames = ['Electronics', 'Clothing', 'Food', 'Books', 'Home', 'Other'];
  categories: Category[] = [];
  isLoading = true;
  
  readonly mapIcon = MapPin;
  readonly chevronLeft = ChevronLeft;
  readonly chevronRight = ChevronRight;

  @ViewChildren('categoryScrollContainer') scrollContainers!: QueryList<ElementRef>;
  
  private productsSubscription?: Subscription;

  constructor(
    private route: ActivatedRoute,
    private productState: ProductStateService,
    private shopState: ShopStateService
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    
    // Get shop from state
    const currentShop = this.shopState.currentShopValue;
        
    if (currentShop) {
      this.shopId = currentShop.id;
      this.shopProfile = currentShop;
      this.loadShopProducts();
    } else {
      // // If no shop in state, try to get shop ID from route
      // this.route.params.subscribe(params => {
      //   if (params['id']) {
      //     this.shopId = params['id'];
      //     // Load shop details first
      //     this.shopState.getShopById(this.shopId).subscribe(shop => {
      //       if (shop) {
      //         this.shopProfile = shop;
      //         this.loadShopProducts();
      //       } else {
      //         console.warn('Shop not found');
      //         this.isLoading = false;
      //       }
      //     });
      //   } else {
      //     console.warn('No shop ID found in route');
      //     this.isLoading = false;
      //   }
      // });
    }
  }

  ngOnDestroy(): void {
    // Clean up subscriptions
    if (this.productsSubscription) {
      this.productsSubscription.unsubscribe();
    }
  }

  loadShopProducts(): void {
    // Clear previous products
    this.categories = [];
    
    console.log("Loading products for shop ID:", this.shopId);
    
    // Load products for this shop
    this.productState.searchProductsInShop(this.shopId, '');
    
    // Unsubscribe from previous subscription if it exists
    if (this.productsSubscription) {
      this.productsSubscription.unsubscribe();
    }
    
    // Subscribe to products changes
    this.productsSubscription = this.productState.products$.subscribe((products: Product[]) => {
      console.log("Products loaded:", products.length);
      console.log("Product loaded details", products);
      if (products && products.length > 0) {
        this.organizeProductsByCategory(products);
      } else {
        // Clear categories if no products
        this.categories = [];
      }
      this.isLoading = false;
    });
  }

  organizeProductsByCategory(products: Product[]): void {
    // Initialize categories with empty products array
    this.categories = this.categoryNames.map(name => ({
      name,
      products: []
    }));
    
    // Group products by category
    products.forEach(product => {
      let category = this.categories.find(c => c.name === product.category);
      
      if (!category) {
        category = this.categories.find(c => c.name === 'Other');
      }
      
      if (category) {
        category.products.push(product);
      }
    });
    
    // Filter out categories with no products
    this.categories = this.categories.filter(c => c.products.length > 0);
    
    console.log("Categories after organization:", this.categories.map(c => `${c.name}: ${c.products.length}`));
  }

  scrollLeft(container: HTMLElement): void {
    container.scrollBy({ left: -300, behavior: 'smooth' });
  }

  scrollRight(container: HTMLElement): void {
    container.scrollBy({ left: 300, behavior: 'smooth' });
  }
}