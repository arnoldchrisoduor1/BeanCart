import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule } from 'lucide-angular';
import { Package, AlignLeft, DollarSign, Percent, Box, Tag, Image, Camera } from 'lucide-angular';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { InputComponent } from '../../../shared/components/input/input.component';
import { ImageUploadService } from '../../../core/services/image-upload.service';
import { ProductStateService } from '../../../core/services/product/product-state.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ProductCreateDto } from '../../../models/product.model';
import { ShopStateService } from '../../../core/services/shop/shop-state.service';

@Component({
  selector: 'app-product-create',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    LucideAngularModule,
    InputComponent,
    ButtonComponent
  ],
  templateUrl: './product-create.component.html',
  styleUrls: ['./product-create.component.css']
})
export class ProductCreateComponent {
  // Icons
  readonly packageIcon = Package;
  readonly alignLeftIcon = AlignLeft;
  readonly dollarSignIcon = DollarSign;
  readonly percentIcon = Percent;
  readonly boxIcon = Box;
  readonly tagIcon = Tag;
  readonly imageIcon = Image;
  readonly cameraIcon = Camera;

  // Categories - you might want to fetch these from a service
  categories = ['Electronics', 'Clothing', 'Food', 'Books', 'Home', 'Other'];

  // Form state
  formData: ProductCreateDto = {
    shopId: '',
    name: '',
    description: '',
    price: 0,
    quantity: 1,
    discount: 0,
    category: '',
    imageUrl: ''
  };

  isUploading = false;
  isSubmitting = false;
  errorMessage = '';

  constructor(
    private imageUploadService: ImageUploadService,
    private productState: ProductStateService,
    private snackBar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute,
    private shopState: ShopStateService
  ) {
    // well try to get shopId from route params.
    this.route.params.subscribe(params => {
      if (params['shopId']) {
        this.formData.shopId = params['shopId'];
      } else {
        // Fallback to current shop in state.
        const currentShop = this.shopState.currentShopValue;
        if (currentShop) {
          this.formData.shopId = currentShop.id;
        } else {
          console.warn('No shopId found in route or shop state');
        }
      }
    })
  }

  async onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    
    if (!file) return;

    this.isUploading = true;
    this.showSnackbar('Uploading product image...', 'loading');

    try {
      // Step 1: Get pre-signed URL from backend
      const urlResponse = await this.imageUploadService.getImageUploadUrl().toPromise();
      const uploadUrl = urlResponse?.uploadURL;
      
      if (!uploadUrl) throw new Error('No upload URL received');

      // Step 2: Upload image directly to S3
      await this.imageUploadService.uploadImageToS3(uploadUrl, file).toPromise();
      
      // Step 3: Get the final image URL (remove query params)
      const imageUrl = uploadUrl.split('?')[0];
      this.formData.imageUrl = imageUrl;
      
      this.showSnackbar('Product image uploaded successfully', 'success');
    } catch (error) {
      console.error('Error uploading image:', error);
      this.showSnackbar('Failed to upload image', 'error');
    } finally {
      this.isUploading = false;
      this.snackBar.dismiss();
    }
  }

  onSubmit() {
    this.errorMessage = '';
    this.isSubmitting = true;
    console.log("ceationPage ===: Product creation initialized with, ", this.formData);
    this.showSnackbar('Adding Product...', 'loading');
    // Use the ProductStateService to create the product
    this.productState.createProduct(this.formData).subscribe({
      next: (product) => {
        if (product) {
          this.isSubmitting = false;
          this.showSnackbar('Product created successfully!', 'success');
          // Navigate to product details or products list page
          this.router.navigate(['/shop/shop-profile']);
        }
      },
      error: (error) => {
        console.error('Failed to create product:', error);
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Failed to create product. Please try again later.';
        this.showSnackbar(this.errorMessage, 'error');
      }
    });
  }

  private showSnackbar(message: string, type: 'success' | 'error' | 'loading') {
    this.snackBar.dismiss();
    this.snackBar.open(message, 'Close', {
      duration: type === 'loading' ? 0 : 5000,
      panelClass: [`snackbar-${type}`]
    });
  }
}