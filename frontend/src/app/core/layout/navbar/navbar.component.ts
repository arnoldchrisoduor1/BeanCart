// src/app/core/layout/navbar/navbar.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { RouterLink, Router, NavigationEnd } from '@angular/router';
import { NgClass, NgIf, CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { AuthStateService } from '../../auth/auth-state.service';
import { Subscription } from 'rxjs';
import { trigger, transition, style, animate, state } from '@angular/animations';
import { 
  LucideAngularModule,
  User,
  ChevronDown,
  LogIn,
  LogOut,
  ShoppingBag,
  Menu,
  X,
  Home,
  ShoppingCart,
  UserPlus,
  Heart,
  Info,
  Mail,
  Bell
} from 'lucide-angular';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, NgIf, NgClass, CommonModule, LucideAngularModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  animations: [
    trigger('slideInOut', [
      state('in', style({
        transform: 'translateX(0)'
      })),
      state('out', style({
        transform: 'translateX(100%)'
      })),
      transition('in => out', animate('300ms ease-in-out')),
      transition('out => in', animate('300ms ease-in-out'))
    ]),
    trigger('fadeInOut', [
      state('in', style({
        opacity: 1
      })),
      state('out', style({
        opacity: 0
      })),
      transition('in => out', animate('300ms ease-in-out')),
      transition('out => in', animate('300ms ease-in-out'))
    ])
  ]
})
export class NavbarComponent implements OnInit, OnDestroy {
  // Icons
  readonly userIcon = User;
  readonly chevronDown = ChevronDown;
  readonly logIn = LogIn;
  readonly logOut = LogOut;
  readonly shoppingBag = ShoppingBag;
  readonly menu = Menu;
  readonly x = X;
  readonly homeIcon = Home;
  readonly cartIcon = ShoppingCart;
  readonly registerIcon = UserPlus;
  readonly wishlistIcon = Heart;
  readonly aboutIcon = Info;
  readonly contactIcon = Mail;
  readonly bell = Bell;

  isMenuOpen = false;
  activeRoute: string = '';
  isLoggedIn = false;
  userProfile: any = null;
  notificationCount = 3;
  private authSubscription!: Subscription;
  private dropdownCloseTimer: any;

  get menuState() {
    return this.isMenuOpen ? 'in' : 'out';
  }

  get overlayState() {
    return this.isMenuOpen ? 'in' : 'out';
  }

  // Organized menu items with icons
  get menuItems() {
    const baseItems = [
      { path: '/', name: 'Home', icon: this.homeIcon },
      { path: '/products/product-list', name: 'Products', icon: this.shoppingBag },
      { path: '/cart', name: 'Cart', icon: this.cartIcon }
    ];

    const guestItems = [
      { path: '/user/login', name: 'Login', icon: this.logIn },
      { path: '/user/register', name: 'Register', icon: this.registerIcon }
    ];

    const authItems = [
      { path: '/user/profile', name: 'Profile', icon: this.userIcon },
      { path: '/user/orders', name: 'Orders', icon: this.shoppingBag },
      { path: '/user/wishes', name: 'Wishlist', icon: this.wishlistIcon }
    ];

    const commonItems = [
      { path: '/about', name: 'About', icon: this.aboutIcon },
      { path: '/contact', name: 'Contact', icon: this.contactIcon }
    ];

    return [
      ...baseItems,
      ...(this.isLoggedIn ? authItems : guestItems),
      ...commonItems
    ];
  }

  constructor(
    private router: Router,
    private authStateService: AuthStateService
  ) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.activeRoute = event.urlAfterRedirects || event.url;
      this.isMenuOpen = false; // Close menu on route change
    });
  }

  ngOnInit() {
    this.authSubscription = this.authStateService.currentUser$.subscribe(user => {
      this.isLoggedIn = !!user;
      this.userProfile = user;
    });
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
    // Disable scrolling when menu is open
    document.body.style.overflow = this.isMenuOpen ? 'hidden' : '';
  }

// Add this after your other methods
onDropdownLeave(event: MouseEvent) {
  // Get the dropdown element
  const dropdown = (event.target as HTMLElement).closest('.group')?.querySelector('[role="dropdown"]');
  
  // If the mouse re-enters before the timer completes, clear the timer
  if (this.dropdownCloseTimer) {
    clearTimeout(this.dropdownCloseTimer);
  }
  
  // Set a timer to close the dropdown after a short delay
  this.dropdownCloseTimer = setTimeout(() => {
    if (dropdown) {
      dropdown.classList.add('hidden');
    }
  }, 300); // 300ms delay
}

// Add this to handle mouse re-entry
onDropdownEnter() {
  // Clear any pending close operations
  if (this.dropdownCloseTimer) {
    clearTimeout(this.dropdownCloseTimer);
  }
}

  login() {
    this.router.navigate(['/user/login']);
  }

  logout() {
    this.authStateService.logout();
    this.router.navigate(['/']);
    this.isMenuOpen = false;
  }

  isActive(route: string): boolean {
    // Check if current route starts with the menu item path
    // This handles nested routes like /products/product-list
    return this.activeRoute.startsWith(route);
  }
}