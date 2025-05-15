import { Component, OnDestroy, OnInit, ViewChildren, QueryList, ElementRef } from '@angular/core';
import { Subscription } from 'rxjs';
import { FaqsComponent } from "../../shared/components/faqs/faqs.component";
import { ProductItemComponent } from "../../shared/components/product-item/product-item.component";
import { 
  LucideAngularModule,
  ChevronDown,
  ChevronRight,
  ChevronLeft,
  MapPin,
  Phone,
  Mail,
  Send,
  Facebook,
  Twitter,
  Instagram,
  Linkedin,
  Heart,
  ShoppingCart,
  Expand,
  Star,
  ShoppingBag,
  Bitcoin,
  Zap,
  Shield,
  Eye,
  Globe,
  Clock,
  ChevronRight as ChevronRightIcon,
  Star as StarIcon,
  Home,
  Smartphone,
  Shirt,
  Book,
  Utensils,
  Gem,
  Music,
  Camera
} from 'lucide-angular';
import { CommonModule } from '@angular/common';
import { Product } from '../../models/product.model';
import { Category } from '../../models/category.model';
import { ProductStateService } from '../../core/services/product/product-state.service';
import { CtaComponent } from "../../shared/components/cta/cta.component";

interface FlashSaleProduct extends Product {
  // No additional properties needed as we'll use the full Product interface
}

interface RecommendedProduct extends Product {
  // No additional properties needed as we'll use the full Product interface
}

interface DigitalAsset {
  id: string;
  name: string;
  type: string;
  price: number;
  image: string;
  description: string;
}

interface FeaturedSeller {
  id: string;
  name: string;
  avatar: string;
  rating: number;
  reviews: number;
  description: string;
  tags: string[];
}

interface CategoryItem {
  name: string;
  count: number;
  icon: any; // Using any for the icon type
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FaqsComponent, ProductItemComponent, LucideAngularModule, CommonModule, CtaComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit, OnDestroy {

  readonly chevronDown = ChevronDown;
  readonly chevronRight = ChevronRight;
  readonly chevronLeft = ChevronLeft;
  readonly mapPin = MapPin;
  readonly phone = Phone;
  readonly mail = Mail;
  readonly send = Send;
  readonly facebook = Facebook;
  readonly twitter = Twitter;
  readonly instagram = Instagram;
  readonly linkedin = Linkedin;
  readonly heartIcon = Heart;
  readonly shoppingCart = ShoppingCart;
  readonly expandIcon = Expand;
  readonly reviewStar = Star;
  readonly star = StarIcon;
  readonly shoppingBag = ShoppingBag;
  readonly bitcoin = Bitcoin;
  readonly zap = Zap;
  readonly shield = Shield;
  readonly eye = Eye;
  readonly globe = Globe;
  readonly clock = Clock;
  readonly smartphone = Smartphone;
  readonly home = Home;
  readonly shirt = Shirt;
  readonly book = Book;
  readonly utensils = Utensils;
  readonly gem = Gem;
  readonly music = Music;
  readonly camera = Camera;

  flashSaleProducts: Product[] = [
    {
      id: 'fs1',
      shopId: 'shop1',
      name: 'Wireless Headphones',
      description: 'High-quality wireless headphones with noise cancellation',
      price: 199.99,
      discount: 50,
      category: 'Electronics',
      imageUrl: 'assets/images/headphones.jpg',
      quantity: 100,
      isFeatured: true,
      active: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      rating: 4.5,
      reviews: 128
    },
    {
      id: 'fs2',
      shopId: 'shop1',
      name: 'Smart Watch Pro',
      description: 'Next generation smartwatch with health monitoring',
      price: 249.99,
      discount: 30,
      category: 'Electronics',
      imageUrl: 'assets/images/smartwatch.jpg',
      quantity: 75,
      isFeatured: true,
      active: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      rating: 4.2,
      reviews: 86
    },
    {
      id: 'fs3',
      shopId: 'shop2',
      name: 'Organic Cotton T-Shirt',
      description: 'Premium organic cotton t-shirt, eco-friendly and comfortable',
      price: 29.99,
      discount: 20,
      category: 'Clothing',
      imageUrl: 'assets/images/tshirt.jpg',
      quantity: 200,
      isFeatured: true,
      active: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      rating: 4.7,
      reviews: 215
    },
    {
      id: 'fs4',
      shopId: 'shop3',
      name: 'Stainless Steel Water Bottle',
      description: 'Eco-friendly stainless steel water bottle, keeps drinks cold for 24 hours',
      price: 24.99,
      discount: 15,
      category: 'Home',
      imageUrl: 'assets/images/water-bottle.jpg',
      quantity: 150,
      isFeatured: true,
      active: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      rating: 4.8,
      reviews: 342
    }
  ];

  // Dummy data for digital assets
  digitalAssets: DigitalAsset[] = [
    {
      id: 'da1',
      name: 'CryptoPunk #1234',
      type: 'NFT',
      price: 2.5,
      image: 'assets/images/nft1.jpg',
      description: 'Rare collectible from the original CryptoPunks collection'
    },
    {
      id: 'da2',
      name: 'DeFi Protocol Token',
      type: 'Token',
      price: 0.75,
      image: 'assets/images/defi.jpg',
      description: 'Governance token for leading DeFi platform'
    },
    {
      id: 'da3',
      name: 'Metaverse Land Parcel',
      type: 'Virtual Land',
      price: 1.2,
      image: 'assets/images/metaverse.jpg',
      description: 'Prime location in the blockchain metaverse'
    }
  ];

  // Dummy data for recommended products
  recommendedProducts: Product[] = [
    {
      id: 'rec1',
      shopId: 'shop1',
      name: 'Bluetooth Speaker',
      description: 'Portable Bluetooth speaker with 20-hour battery life',
      price: 89.99,
      category: 'Electronics',
      imageUrl: 'assets/images/speaker.jpg',
      quantity: 80,
      isFeatured: true,
      active: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      rating: 4.3,
      reviews: 92
    },
    {
      id: 'rec2',
      shopId: 'shop4',
      name: 'Yoga Mat',
      description: 'Premium non-slip yoga mat, eco-friendly materials',
      price: 39.99,
      category: 'Fitness',
      imageUrl: 'assets/images/yoga-mat.jpg',
      quantity: 120,
      isFeatured: true,
      active: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      rating: 4.6,
      reviews: 178
    },
    {
      id: 'rec3',
      shopId: 'shop3',
      name: 'Coffee Maker',
      description: 'Programmable coffee maker with thermal carafe',
      price: 129.99,
      category: 'Home',
      imageUrl: 'assets/images/coffee-maker.jpg',
      quantity: 60,
      isFeatured: true,
      active: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      rating: 4.4,
      reviews: 204
    },
    {
      id: 'rec4',
      shopId: 'shop2',
      name: 'Backpack',
      description: 'Durable waterproof backpack with laptop compartment',
      price: 59.99,
      category: 'Accessories',
      imageUrl: 'assets/images/backpack.jpg',
      quantity: 90,
      isFeatured: true,
      active: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      rating: 4.7,
      reviews: 156
    }
  ];

  // Category items with icon mapping
  categoryItems: CategoryItem[] = [
    { name: 'Electronics', count: 128, icon: this.smartphone },
    { name: 'Clothing', count: 342, icon: this.shirt },
    { name: 'Home', count: 215, icon: this.home },
    { name: 'Books', count: 87, icon: this.book },
    { name: 'Kitchen', count: 156, icon: this.utensils },
    { name: 'Jewelry', count: 64, icon: this.gem },
    { name: 'Music', count: 92, icon: this.music },
    { name: 'Photography', count: 43, icon: this.camera }
  ];

  // Dummy data for featured sellers
  featuredSellers: FeaturedSeller[] = [
    {
      id: 'seller1',
      name: 'TechGadgets Pro',
      avatar: 'assets/images/seller1.jpg',
      rating: 4.8,
      reviews: 1245,
      description: 'Leading provider of cutting-edge technology and gadgets',
      tags: ['Electronics', 'Gadgets', 'Verified']
    },
    {
      id: 'seller2',
      name: 'DigitalArt Collective',
      avatar: 'assets/images/seller2.jpg',
      rating: 4.9,
      reviews: 876,
      description: 'Curated selection of premium digital art and NFTs',
      tags: ['Digital Art', 'NFTs', 'Premium']
    },
    {
      id: 'seller3',
      name: 'EcoLiving',
      avatar: 'assets/images/seller3.jpg',
      rating: 4.7,
      reviews: 1562,
      description: 'Sustainable products for eco-conscious living',
      tags: ['Eco-Friendly', 'Sustainable', 'Organic']
    }
  ];

  categoryNames = ['Electronics', 'Clothing', 'Food', 'Books', 'Home', 'Other'];
  categories: Category[] = [];
  isLoading = true;

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

  // Helper method to get icon for a category name
  getCategoryIcon(categoryName: string): any {
    const foundCategory = this.categoryItems.find(c => c.name === categoryName);
    return foundCategory ? foundCategory.icon : this.home; // Default to home icon
  }
}