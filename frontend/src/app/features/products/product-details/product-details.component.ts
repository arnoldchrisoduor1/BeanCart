import { Component } from '@angular/core';
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
export class ProductDetailsComponent {
  readonly starIcon = Star;
  readonly chevronDownIcon = ChevronDown;
  readonly cartIcon = ShoppingCart;
  readonly heartIcon = Heart;

  Product2: string = 'assets/images/product2.jpg';
  activeTab: 'description' | 'reviews' | 'faqs' = 'description';
  selectedSize: string | null = null;
  selectedColor: string | null = null;
  selectedStyle: string | null = null;

  tabs = [
    { id: 'description' as const, label: "What's Inside" },
    { id: 'reviews' as const, label: "Reviews" },
    { id: 'faqs' as const, label: "FAQs" }
  ];

  // Pricing information
  originalPrice = 89.99;
  discountedPrice = 59.99;
  discountPercentage = Math.round((1 - this.discountedPrice / this.originalPrice) * 100);

  

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