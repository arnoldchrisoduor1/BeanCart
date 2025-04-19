import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule } from 'lucide-angular';
import { Store, AlignLeft, Mail, Phone, MapPin, Camera } from 'lucide-angular';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { InputComponent } from '../../../shared/components/input/input.component';
import { ImageUploadService } from '../../../core/services/image-upload.service';
import { ShopService } from '../../../core/services/shop/shop.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-shop-create',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    LucideAngularModule,
    InputComponent,
    ButtonComponent
  ],
  templateUrl: './create-shop.component.html',
  styleUrls: ['./create-shop.component.css']
})
export class CreateShopComponent {
  // Icons
  readonly storeIcon = Store;
  readonly alignLeftIcon = AlignLeft;
  readonly mailIcon = Mail;
  readonly phoneIcon = Phone;
  readonly mapPinIcon = MapPin;
  readonly cameraIcon = Camera;

  // Form state
  formData = {
    name: '',
    description: '',
    logoUrl: '',
    address: '',
    phone: '',
    email: '',
  };

  isUploading = false;
  isSubmitting = false;
  errorMessage = '';

  constructor(
    private imageUploadService: ImageUploadService,
    private shopService: ShopService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  async onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    
    if (!file) return;

    this.isUploading = true;
    this.showSnackbar('Uploading shop logo...', 'loading');

    try {
      // Step 1: Get pre-signed URL from backend
      const urlResponse = await this.imageUploadService.getImageUploadUrl().toPromise();
      const uploadUrl = urlResponse?.uploadURL;
      
      if (!uploadUrl) throw new Error('No upload URL received');

      // Step 2: Upload image directly to S3
      await this.imageUploadService.uploadImageToS3(uploadUrl, file).toPromise();
      
      // Step 3: Get the final image URL (remove query params)
      const imageUrl = uploadUrl.split('?')[0];
      this.formData.logoUrl = imageUrl; // Update the logoUrl in the form data for immediate preview
      
      this.showSnackbar('Shop logo uploaded successfully', 'success');
    } catch (error) {
      console.error('Error uploading image:', error);
      this.showSnackbar('Failed to upload image', 'error');
    } finally {
      this.isUploading = false;
      // Dismiss any loading snackbar
      this.snackBar.dismiss();
    }
  }

  onSubmit() {
    this.errorMessage = '';
    this.isSubmitting = true;
    
    // Use the ShopService to create the shop
    this.shopService.createShop({
      name: this.formData.name,
      description: this.formData.description,
      phone: this.formData.phone,
      email: this.formData.email,
      address: this.formData.address,
      logoUrl: this.formData.logoUrl
    }).subscribe({
      next: (response) => {
        console.log('Shop created successfully:', response);
        this.isSubmitting = false;
        this.showSnackbar('Shop created successfully!', 'success');
        // Navigate to shop details or shops list page
        this.router.navigate(['/shop/shop-profile']);
      },
      error: (error) => {
        console.error('Failed to create shop:', error);
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Failed to create shop. Please try again later.';
        this.showSnackbar(this.errorMessage, 'error');
      }
    });
  }

  private showSnackbar(message: string, type: 'success' | 'error' | 'loading') {
    // Dismiss any existing snackbar
    this.snackBar.dismiss();
    
    // Show the new snackbar
    this.snackBar.open(message, 'Close', {
      duration: type === 'loading' ? 0 : 5000,
      panelClass: [`snackbar-${type}`]
    });
  }
}