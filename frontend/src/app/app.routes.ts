// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { UserComponent } from './features/user/user.component';
import { LoginComponent } from './features/user/login/login.component';
import { RegisterComponent } from './features/user/register/register.component';
import { AuthGuard } from './core/auth/auth.guard';
import { ProfileComponent } from './features/user/profile/profile.component';
import { OrdersPageComponent } from './features/orders/orders-page/orders-page.component';
import { WishesPageComponent } from './features/wishes/wishes-page/wishes-page.component';

export const routes: Routes = [
  // Home route
  { path: '', component: HomeComponent },
  
  // User routes with child routes for login and registration
  { 
    path: 'user', 
    component: UserComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
      {path: 'profile', component: ProfileComponent},
      {path: 'orders', component: OrdersPageComponent},
      {path: 'wishes', component: WishesPageComponent},
      // { 
      //   path: 'profile', 
      //   loadChildren: () => import('./features/user/profile/profile.routes').then(m => m.PROFILE_ROUTES),
      //   canActivate: [AuthGuard]
      // },
      // { 
      //   path: 'orders', 
      //   loadChildren: () => import('./features/user/orders/orders.routes').then(m => m.ORDERS_ROUTES),
      //   canActivate: [AuthGuard]
      // },
      // { path: '', redirectTo: 'login', pathMatch: 'full' }
    ]
  },
  
  // // Products route
  // { 
  //   path: 'products',
  //   loadChildren: () => import('./features/products/products.routes').then(m => m.PRODUCTS_ROUTES)
  // },
  
  // // Cart route
  // { 
  //   path: 'cart',
  //   loadChildren: () => import('./features/cart/cart.routes').then(m => m.CART_ROUTES)
  // },
  
  // // Checkout route (protected)
  // { 
  //   path: 'checkout',
  //   loadChildren: () => import('./features/checkout/checkout.routes').then(m => m.CHECKOUT_ROUTES),
  //   canActivate: [AuthGuard]
  // },
  
  // // Wildcard route for 404 page
  // { path: '**', redirectTo: '' }
];