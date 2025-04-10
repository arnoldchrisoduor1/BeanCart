import { Component } from '@angular/core';
import { RouterLink, Router, NavigationEnd } from '@angular/router';
import { NgClass, NgIf } from '@angular/common';
import { LucideAngularModule, X, Menu } from 'lucide-angular';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, NgClass, NgIf, LucideAngularModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})


export class NavbarComponent {
  readonly menuIcon = Menu;
  readonly closeIcon = X;
  isMenuOpen = false;
  activeRoute: string = '';
  menuItems = [ 
    { path: '/', name: 'Home' },
    { path: '/products', name: 'Products' },
    { path: '/cart', name: 'Cart' },
    { path: '/user/login', name: 'Login' },
    { path: '/user/register', name: 'Register' },
  ]

  constructor(private router: Router) {
    // Subscribe to router events to track the current route
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.activeRoute = event.url;
    });
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
    
    // Prevent scrolling when menu is open
    if (this.isMenuOpen) {
      document.body.classList.add('overflow-hidden');
    } else {
      document.body.classList.remove('overflow-hidden');
    }
  }

  // Helper method to check if a route is active
  isActive(route: string): boolean {
    return this.activeRoute === route;
  }

  getItemDelay(index: number): string {
    return `${index * 0.1}s`;
  }
}