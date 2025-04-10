import { Component } from '@angular/core';
import { HeroComponent } from './hero/hero.component';
import { FeaturedProductsComponent } from './featured-products/featured-products.component';
import { CategoriesComponent } from './categories/categories.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [HeroComponent, FeaturedProductsComponent, CategoriesComponent],
  template: `
    <div class="space-y-8">
      <app-hero></app-hero>
      <app-featured-products></app-featured-products>
      <app-categories></app-categories>
    </div>
  `
})
export class HomeComponent {}