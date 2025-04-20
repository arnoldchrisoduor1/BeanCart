import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product, ProductCreateDto, ProductUpdateDto } from '../../../models/product.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}/api/products`;

  constructor(private http: HttpClient) {}

  // Create a new product
  createProduct(product: ProductCreateDto): Observable<Product> {
    console.log('Product-Service:== creating new product with', product);
    return this.http.post<Product>(this.apiUrl, product);
  }

  // Get a product by ID
  getProductById(productId: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${productId}`);
  }

  // Update a product
  updateProduct(productId: string, product: ProductUpdateDto): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${productId}`, product);
  }

  // Delete a product
  deleteProduct(productId: string): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.apiUrl}/${productId}`);
  }

  // Get all products with pagination
  getAllProducts(page = 0, size = 10, sortBy = 'createdAt', direction = 'desc'): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);
    
    return this.http.get<any>(this.apiUrl, { params });
  }

  // Get active products
  getActiveProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/active`);
  }

  // Get products by shop ID
  getProductsByShop(shopId: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/shop/${shopId}`);
  }

  // Get active products by shop ID
  getActiveProductsByShop(shopId: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/shop/${shopId}/active`);
  }

  // Get products by category
  getProductsByCategory(category: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/category/${category}`);
  }

  // Get products by shop and category
  getProductsByShopAndCategory(shopId: string, category: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/shop/${shopId}/category/${category}`);
  }

  // Get featured products
  getFeaturedProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/featured`);
  }

  // Get featured products by shop
  getFeaturedProductsByShop(shopId: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/shop/${shopId}/featured`);
  }

  // Search products by name
  searchProductsByName(keyword: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/search/name`, {
      params: new HttpParams().set('keyword', keyword)
    });
  }

  // Search products (name, description, category)
  searchProducts(keyword: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/search`, {
      params: new HttpParams().set('keyword', keyword)
    });
  }

  // Search products within a shop
  searchProductsByShop(shopId: string, keyword: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/shop/${shopId}/search`, {
      params: new HttpParams().set('keyword', keyword)
    });
  }

  // Get products by price range
  getProductsByPriceRange(minPrice: number, maxPrice: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/price-range`, {
      params: new HttpParams()
        .set('minPrice', minPrice.toString())
        .set('maxPrice', maxPrice.toString())
    });
  }

  // Get discounted products
  getProductsWithDiscount(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/discounted`);
  }

  // Get discounted products by shop
  getProductsWithDiscountByShop(shopId: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/shop/${shopId}/discounted`);
  }

  // Update product quantity
  updateProductQuantity(productId: string, quantity: number): Observable<Product> {
    return this.http.patch<Product>(`${this.apiUrl}/${productId}/quantity`, null, {
      params: new HttpParams().set('quantity', quantity.toString())
    });
  }

  // Toggle product featured status
  toggleFeaturedStatus(productId: string): Observable<Product> {
    return this.http.patch<Product>(`${this.apiUrl}/${productId}/toggle-featured`, null);
  }

  // Toggle product active status
  toggleActiveStatus(productId: string): Observable<Product> {
    return this.http.patch<Product>(`${this.apiUrl}/${productId}/toggle-active`, null);
  }

  // Get recent products
  getRecentProducts(page = 0, size = 10): Observable<Product[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Product[]>(`${this.apiUrl}/recent`, { params });
  }

  // Get best selling products
  getBestSellingProducts(page = 0, size = 10): Observable<Product[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<Product[]>(`${this.apiUrl}/best-selling`, { params });
  }
}