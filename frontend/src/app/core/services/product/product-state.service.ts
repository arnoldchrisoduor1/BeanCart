import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, map, of, tap } from 'rxjs';
import { Product, ProductCreateDto, ProductUpdateDto } from '../../../models/product.model';
import { ProductService } from './product.service';

@Injectable({
  providedIn: 'root'
})
export class ProductStateService {
  private productsSubject = new BehaviorSubject<Product[]>([]);
  private currentProductSubject = new BehaviorSubject<Product | null>(null);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private errorSubject = new BehaviorSubject<string | null>(null);

  public products$ = this.productsSubject.asObservable();
  public currentProduct$ = this.currentProductSubject.asObservable();
  public loading$ = this.loadingSubject.asObservable();
  public error$ = this.errorSubject.asObservable();

  constructor(private productService: ProductService) {}

  // Load products for a specific shop
  loadShopProducts(shopId: string): void {
    this.loadingSubject.next(true);
    this.productService.getProductsByShop(shopId).pipe(
      tap(products => {
        this.productsSubject.next(products);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to load products');
        return of([]);
      })
    ).subscribe();
  }

  // Load active products for a specific shop
  loadActiveShopProducts(shopId: string): void {
    this.loadingSubject.next(true);
    this.productService.getActiveProductsByShop(shopId).pipe(
      tap(products => {
        this.productsSubject.next(products);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to load active products');
        return of([]);
      })
    ).subscribe();
  }

  // Load a single product by ID
  loadProduct(productId: string): void {
    this.loadingSubject.next(true);
    this.productService.getProductById(productId).pipe(
      tap(product => {
        this.currentProductSubject.next(product);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to load product');
        return of(null);
      })
    ).subscribe();
  }

  // Create a new product
  createProduct(product: ProductCreateDto): Observable<Product | null> {
    this.loadingSubject.next(true);
    return this.productService.createProduct(product).pipe(
      map(newProduct => {
        const currentProducts = this.productsSubject.value;
        this.productsSubject.next([...currentProducts, newProduct]);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
        console.log("New creaed product: ", newProduct);
        return newProduct;
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to create product');
        return of(null);
      })
    );
  }

  // Update an existing product
  updateProduct(productId: string, productUpdate: ProductUpdateDto): Observable<Product | null> {
    this.loadingSubject.next(true);
    return this.productService.updateProduct(productId, productUpdate).pipe(
      map(updatedProduct => {
        const currentProducts = this.productsSubject.value;
        const updatedProducts = currentProducts.map(p => 
          p.id === updatedProduct.id ? updatedProduct : p
        );
        this.productsSubject.next(updatedProducts);
        
        if (this.currentProductSubject.value?.id === updatedProduct.id) {
          this.currentProductSubject.next(updatedProduct);
        }
        
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
        return updatedProduct;
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to update product');
        return of(null);
      })
    );
  }

  // Delete a product
  deleteProduct(productId: string): Observable<boolean> {
    this.loadingSubject.next(true);
    return this.productService.deleteProduct(productId).pipe(
      map(() => {
        const currentProducts = this.productsSubject.value;
        const updatedProducts = currentProducts.filter(p => p.id !== productId);
        this.productsSubject.next(updatedProducts);
        
        if (this.currentProductSubject.value?.id === productId) {
          this.currentProductSubject.next(null);
        }
        
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
        return true;
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to delete product');
        return of(false);
      })
    );
  }

  // Toggle featured status
  toggleFeaturedStatus(productId: string): Observable<Product | null> {
    this.loadingSubject.next(true);
    return this.productService.toggleFeaturedStatus(productId).pipe(
      map(updatedProduct => {
        this.updateProductInState(updatedProduct);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
        return updatedProduct;
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to toggle featured status');
        return of(null);
      })
    );
  }

  // Toggle active status
  toggleActiveStatus(productId: string): Observable<Product | null> {
    this.loadingSubject.next(true);
    return this.productService.toggleActiveStatus(productId).pipe(
      map(updatedProduct => {
        this.updateProductInState(updatedProduct);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
        return updatedProduct;
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to toggle active status');
        return of(null);
      })
    );
  }

  // Update product quantity
  updateProductQuantity(productId: string, quantity: number): Observable<Product | null> {
    this.loadingSubject.next(true);
    return this.productService.updateProductQuantity(productId, quantity).pipe(
      map(updatedProduct => {
        this.updateProductInState(updatedProduct);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
        return updatedProduct;
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to update product quantity');
        return of(null);
      })
    );
  }

  // Search products
  searchProducts(keyword: string): void {
    this.loadingSubject.next(true);
    this.productService.searchProducts(keyword).pipe(
      tap(products => {
        this.productsSubject.next(products);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to search products');
        return of([]);
      })
    ).subscribe();
  }

  // Search products in a shop
  searchProductsInShop(shopId: string, keyword: string): void {
    this.loadingSubject.next(true);
    this.productService.searchProductsByShop(shopId, keyword).pipe(
      tap(products => {
        this.productsSubject.next(products);
        this.loadingSubject.next(false);
        this.errorSubject.next(null);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Failed to search products in shop');
        return of([]);
      })
    ).subscribe();
  }

  // Helper method to update a product in the state
  private updateProductInState(updatedProduct: Product): void {
    const currentProducts = this.productsSubject.value;
    const updatedProducts = currentProducts.map(p => 
      p.id === updatedProduct.id ? updatedProduct : p
    );
    this.productsSubject.next(updatedProducts);
    
    if (this.currentProductSubject.value?.id === updatedProduct.id) {
      this.currentProductSubject.next(updatedProduct);
    }
  }

  // Helper to clear current errors
  clearError(): void {
    this.errorSubject.next(null);
  }

  // Reset state
  resetState(): void {
    this.productsSubject.next([]);
    this.currentProductSubject.next(null);
    this.loadingSubject.next(false);
    this.errorSubject.next(null);
  }
}