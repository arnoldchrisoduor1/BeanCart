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
import { ProductsComponent } from './features/products/products.component';
import { ProductListComponent } from './features/products/product-list/product-list.component';
import { ShopComponent } from './features/shop/shop.component';
import { ShopProfileComponent } from './features/shop/shop-profile/shop-profile.component';
import { ProductDetailsComponent } from './features/products/product-details/product-details.component';
import { CreateShopComponent } from './features/shop/create-shop/create-shop.component';
import { ProductCreateComponent } from './features/products/product-create/product-create.component';
import { CartComponent } from './features/cart/cart.component';
import { CheckoutComponent } from './features/checkout/checkout.component';

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
    ]
  },
  {
    path: 'products',
    component: ProductsComponent,
    children : [
      {
        path: 'product-list', component: ProductListComponent,
      },
      {
        path: 'product-details/:id', component: ProductDetailsComponent,
      },
      {
        path: 'product-create', component: ProductCreateComponent,
      }
    ]
  },
  {
    path: 'shop',
    component: ShopComponent,
    children: [
      {
        path: 'shop-profile', component: ShopProfileComponent
      }, {
        path: 'create-shop', component: CreateShopComponent
      }
    ]
  },
  {
    path: 'cart',
    component: CartComponent,
    children: [
      
    ]
  },
  {
    path: 'checkout',
    component: CheckoutComponent,
    children: []
  }
  
];