import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { 
  LucideAngularModule, 
  Star,
  ChevronDown,
  ShoppingCart,
  Heart
} from 'lucide-angular';
import { SelectionTabsComponent } from '../../../shared/components/selection-tabs/selection-tabs.component';
import { ColorSelectionComponent } from '../../../shared/components/color-selection/color-selection.component';
import { ProductDescriptionComponent } from "../../../shared/components/product-description/product-description.component";
import { FaqsComponent } from "../../../shared/components/faqs/faqs.component";
import { ProductReviewsComponent } from "../../../shared/components/product-reviews/product-reviews.component";
import { ActivatedRoute } from '@angular/router';
import { ProductStateService } from '../../../core/services/product/product-state.service';
import { Product } from '../../../models/product.model';

@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [
    LucideAngularModule, 
    SelectionTabsComponent, 
    ColorSelectionComponent, 
    ProductDescriptionComponent, 
    FaqsComponent, 
    ProductReviewsComponent,
    CommonModule
  ],
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {
  readonly starIcon = Star;
  readonly chevronDownIcon = ChevronDown;
  readonly cartIcon = ShoppingCart;
  readonly heartIcon = Heart;

  activeTab: 'description' | 'reviews' | 'faqs' = 'description';
  selectedSize: string | null = null;
  selectedColor: string | null = null;
  selectedStyle: string | null = null;

  tabs = [
    { id: 'description' as const, label: "What's Inside" },
    { id: 'reviews' as const, label: "Reviews" },
    { id: 'faqs' as const, label: "FAQs" }
  ];

  product: Product | null = null;
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private productState: ProductStateService
  ) {}

  ngOnInit() {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.productState.loadProduct(productId);
      this.productState.currentProduct$.subscribe((product: Product | null) => {
        this.product = product;
        this.loading = false;
      });
    }
  }

  get productImage(): string {
    return this.product?.imageUrl || 'assets/images/product2.jpg';
  }

  get originalPrice(): number {
    return this.product?.price || 0;
  }

  get discountedPrice(): number {
    if (this.product?.discount) {
      return this.originalPrice - this.product.discount;
    }
    return this.originalPrice;
  }

  get discountPercentage(): number {
    if (this.product?.discount) {
      return Math.round((this.product.discount / this.originalPrice) * 100);
    }
    return 0;
  }

  selectSize(size: string) {
    this.selectedSize = size;
  }

  selectColor(color: string) {
    this.selectedColor = color;
  }

  selectStyle(style: string) {
    this.selectedStyle = style;
  }

  setActiveTab(tab: 'description' | 'reviews' | 'faqs') {
    this.activeTab = tab;
  }

  onColorSelected(color: string) {
    this.selectedColor = color;
    console.log('Selected color:', color);
  }
}