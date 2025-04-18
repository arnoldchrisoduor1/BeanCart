// src/app/shared/components/profile-photo-upload/profile-photo-upload.component.ts
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ImageUploadService } from '../../../core/services/image-upload.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-photo-upload',
  standalone: true,
  templateUrl: './photo-upload.component.html',
  styleUrls: ['./photo-upload.component.scss']
})
export class ProfilePhotoUploadComponent {
  @Input() profilePhoto: string | null = null;
  @Output() photoChanged = new EventEmitter<string>();
  
  isHovered = false;
  isLoading = false;

  constructor(
    private imageUploadService: ImageUploadService,
    private snackBar: MatSnackBar
  ) {}

  async onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    
    if (!file) return;

    this.isLoading = true;
    this.showSnackbar('Uploading profile image...', 'loading');

    try {
      // Step 1: Get pre-signed URL from backend
      const urlResponse = await this.imageUploadService.getImageUploadUrl().toPromise();
      const uploadUrl = urlResponse?.uploadURL;
      
      if (!uploadUrl) throw new Error('No upload URL received');

      // Step 2: Upload image directly to S3
      await this.imageUploadService.uploadImageToS3(uploadUrl, file).toPromise();
      
      // Step 3: Get the final image URL (remove query params)
      const imageUrl = uploadUrl.split('?')[0];
      
      // Update parent component
      this.photoChanged.emit(imageUrl);
      this.profilePhoto = imageUrl;
      
      this.showSnackbar('Profile image uploaded. Click update to save.', 'success');
    } catch (error) {
      console.error('Error uploading image:', error);
      this.showSnackbar('Failed to upload image', 'error');
    } finally {
      this.isLoading = false;
    }
  }

  private showSnackbar(message: string, type: 'success' | 'error' | 'loading') {
    this.snackBar.open(message, 'Close', {
      duration: type === 'loading' ? 0 : 5000,
      panelClass: [`snackbar-${type}`]
    });
  }
}