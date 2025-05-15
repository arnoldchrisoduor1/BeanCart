import { Component, OnDestroy, OnInit, ViewChildren, QueryList, ElementRef } from '@angular/core';
import { Subscription } from 'rxjs';
import { FaqsComponent } from "../../shared/components/faqs/faqs.component";
import { ProductItemComponent } from "../../shared/components/product-item/product-item.component";
import { LucideAngularModule, ChevronLeft, ChevronRight } from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { Product } from '../../models/product.model';
import { Category } from '../../models/category.model';
import { ProductStateService } from '../../core/services/product/product-state.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FaqsComponent, ProductItemComponent, LucideAngularModule, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit, OnDestroy {

  categoryNames = ['Electronics', 'Clothing', 'Food', 'Books', 'Home', 'Other'];
  categories: Category[] = [];
  isLoading = true;

  readonly chevronLeft = ChevronLeft;
  readonly chevronRight = ChevronRight;

  @ViewChildren('categoryScrollContainer') scrollContainers!: QueryList<ElementRef>;
  
  private productsSubscription?: Subscription;

  constructor(
    private productState: ProductStateService
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.loadAllProducts();
  }

  ngOnDestroy(): void {
    // Clean up subscriptions
    if (this.productsSubscription) {
      this.productsSubscription.unsubscribe();
    }
  }

  loadAllProducts(): void {
    // Clear previous products
    this.categories = [];
    
    console.log("Loading all products");
    
    // Load all products
    this.productState.searchProducts('');
    
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