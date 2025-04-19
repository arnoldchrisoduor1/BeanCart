import { Component, Input } from '@angular/core';
import { StatsBoxComponent } from '../../../shared/components/stats-box/stats-box.component';
import { ProductItemComponent } from '../../../shared/components/product-item/product-item.component';
import { LucideAngularModule, MapPinned, ChevronLeft, ChevronRight } from 'lucide-angular';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-shop-profile',
  standalone: true,
  imports: [StatsBoxComponent, ProductItemComponent, LucideAngularModule, CommonModule],
  templateUrl: './shop-profile.component.html',
  styleUrl: './shop-profile.component.css'
})
export class ShopProfileComponent {
  shopBanner: string = 'assets/images/shop-banner.jpg';
  readonly mapIcon = MapPinned;
  readonly chevronLeft = ChevronLeft;
  readonly chevronRight = ChevronRight;

  @Input() ProductImage: string = '';
@Input() productName: string = '';
@Input() productCategory: string = '';
@Input() currentPrice: number = 0;
@Input() originalPrice: number = 0;
@Input() discount: number = 0;
@Input() rating: number = 0;

  // Define your product categories with sample products
  categories = [
    {
      id: 1,
      name: 'Featured Products',
      products: [
        {
          id: 101,
          name: 'SilkSculpt Serum',
          category: 'Skin Care',
          price: 35.05,
          originalPrice: 40.99,
          discount: 50,
          rating: 4.3,
          image: 'assets/images/product1.jpg'
        },
        {
          id: 102,
          name: 'Wireless Earbuds',
          category: 'Electronics',
          price: 89.99,
          originalPrice: 129.99,
          discount: 30,
          rating: 4.7,
          image: 'assets/images/product2.jpg'
        },
        {
          id: 103,
          name: 'Organic Face Cream',
          category: 'Skin Care',
          price: 24.99,
          originalPrice: 34.99,
          discount: 28,
          rating: 4.2,
          image: 'assets/images/product3.jpg'
        },
        {
          id: 104,
          name: 'Smart Watch',
          category: 'Electronics',
          price: 199.99,
          originalPrice: 249.99,
          discount: 20,
          rating: 4.5,
          image: 'assets/images/product4.jpg'
        }
      ]
    },
    {
      id: 2,
      name: 'Electronics',
      products: [
        {
          id: 201,
          name: 'Bluetooth Speaker',
          category: 'Electronics',
          price: 59.99,
          originalPrice: 79.99,
          discount: 25,
          rating: 4.4,
          image: 'assets/images/product5.jpg'
        },
        {
          id: 202,
          name: 'Wireless Charger',
          category: 'Electronics',
          price: 29.99,
          originalPrice: 39.99,
          discount: 25,
          rating: 4.1,
          image: 'assets/images/product6.jpg'
        },
        {
          id: 203,
          name: 'VR Headset',
          category: 'Electronics',
          price: 299.99,
          originalPrice: 399.99,
          discount: 25,
          rating: 4.6,
          image: 'assets/images/product7.jpg'
        }
      ]
    },
    {
      id: 3,
      name: 'Skin Care',
      products: [
        {
          id: 301,
          name: 'Night Repair Cream',
          category: 'Skin Care',
          price: 45.99,
          originalPrice: 59.99,
          discount: 23,
          rating: 4.8,
          image: 'assets/images/product8.jpg'
        },
        {
          id: 302,
          name: 'Vitamin C Serum',
          category: 'Skin Care',
          price: 32.50,
          originalPrice: 45.00,
          discount: 28,
          rating: 4.3,
          image: 'assets/images/product9.jpg'
        }
      ]
    }
  ];

  // Function to scroll carousels
  scrollLeft(element: HTMLElement) {
    element.scrollBy({ left: -300, behavior: 'smooth' });
  }

  scrollRight(element: HTMLElement) {
    element.scrollBy({ left: 300, behavior: 'smooth' });
  }
}