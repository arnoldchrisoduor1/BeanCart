// src/app/core/layout/navbar/navbar.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { RouterLink, Router, NavigationEnd } from '@angular/router';
import { NgClass, NgIf, CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { AuthStateService } from '../../auth/auth-state.service';
import { Subscription } from 'rxjs';
import { trigger, transition, style, animate, query, stagger } from '@angular/animations';
import { LucideAngularModule } from 'lucide-angular';
import { Menu, X, User } from 'lucide-angular';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, NgIf, CommonModule, LucideAngularModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  animations: [
    trigger('slideInOut', [
      transition(':enter', [
        style({ transform: 'translateX(100%)' }),
        animate('300ms cubic-bezier(0.16, 1, 0.3, 1)', style({ transform: 'translateX(0)' }))
      ]),
      transition(':leave', [
        animate('300ms cubic-bezier(0.16, 1, 0.3, 1)', style({ transform: 'translateX(100%)' }))
      ])
    ]),
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms cubic-bezier(0.16, 1, 0.3, 1)', style({ opacity: 1 }))
      ]),
      transition(':leave', [
        animate('300ms cubic-bezier(0.16, 1, 0.3, 1)', style({ opacity: 0 }))
      ])
    ]),
    trigger('staggerSlideIn', [
      transition('* => *', [
        query(':enter', [
          style({ opacity: 0, transform: 'translateX(20px)' }),
          stagger('50ms', [
            animate('300ms cubic-bezier(0.16, 1, 0.3, 1)', 
              style({ opacity: 1, transform: 'translateX(0)' }))
          ])
        ], { optional: true })
      ])
    ])
  ]
})
export class NavbarComponent implements OnInit, OnDestroy {
  readonly menuIcon = Menu;
  readonly closeIcon = X;
  readonly userIcon = User;

  isMenuOpen = false;
  activeRoute: string = '';
  isLoggedIn = false;
  userProfile: any = null;
  private authSubscription!: Subscription;

  get menuItems() {
    const guestMenuItems = [
      { path: '/', name: 'Home' },
      { path: '/products', name: 'Products' },
      { path: '/cart', name: 'Cart' },
      { path: '/user/login', name: 'Login' },
      { path: '/user/register', name: 'Register' },
      { path: '/user/profile', name: 'Profile' },
      { path: '/user/orders', name: 'Orders' },
      { path: '/user/wishes', name: 'Wishes' },
    ];
    
    const authenticatedMenuItems = [
      { path: '/', name: 'Home' },
      { path: '/products', name: 'Products' },
      { path: '/cart', name: 'Cart' },
      { path: '/user/profile', name: 'Profile' },
      { path: '/user/orders', name: 'Orders' },
      { path: '/user/wishes', name: 'Wishes' },
    ];
    
    return this.isLoggedIn ? authenticatedMenuItems : guestMenuItems;
  }
    
  constructor(
    private router: Router,
    private authStateService: AuthStateService
  ) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.activeRoute = event.url;
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
  }
    
  logout() {
    this.authStateService.logout();
    this.router.navigate(['/']);
  }
    
  isActive(route: string): boolean {
    return this.activeRoute === route;
  }
}