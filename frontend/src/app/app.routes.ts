import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { UserComponent } from './features/user/user.component';
import { LoginComponent } from './features/user/login/login.component';
import { RegisterComponent } from './features/user/register/register.component';

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
      { path: '', redirectTo: 'login', pathMatch: 'full' }
    ]
  },
  
  // Wildcard route for 404 page (optional)
  { path: '**', redirectTo: '' }
];