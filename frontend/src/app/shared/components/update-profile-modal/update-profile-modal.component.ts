import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LucideAngularModule } from 'lucide-angular';
import { User, X, UserRoundPen, Camera } from 'lucide-angular';
import { InputComponent } from '../input/input.component';
import { ButtonComponent } from "../button/button.component";
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { AuthStateService } from '../../../core/auth/auth-state.service';
import { AuthService } from '../../../core/auth/auth.service';
import { FormsModule } from '@angular/forms';
import { ImageUploadService } from '../../../core/services/image-upload.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-update-profile-modal',
  standalone: true,
  imports: [LucideAngularModule, InputComponent, ButtonComponent, CommonModule, FormsModule],
  templateUrl: './update-profile-modal.component.html',
  styleUrl: './update-profile-modal.component.css'
})
export class UpdateProfileModalComponent {
  @Input() isVisible: boolean = false;
  @Output() closed = new EventEmitter<void>();

  userProfile: any = null;
  private authSubscription!: Subscription;
  isLoggedIn = false;
  isUpdating = false;
  isUploading = false;
  errorMessage: string = '';

  readonly userIcon = User;
  readonly closeIcon = X;
  readonly userPen = UserRoundPen;
  readonly cameraIcon = Camera;

  firstName: string = '';
  lastName: string = '';
  profileImageUrl: string = '';
  selectedFile: File | null = null;

  constructor(
    private authStateService: AuthStateService,
    private authService: AuthService,
    private imageUploadService: ImageUploadService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.authSubscription = this.authStateService.currentUser$.subscribe(user => {
      this.isLoggedIn = !!user;
      this.userProfile = user;
      // initializing the user details.
      this.firstName = user?.firstName || '';
      this.lastName = user?.lastName || '';
      this.profileImageUrl = user?.profileImageUrl || '';
    });
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  closeModal() {
    console.log("child emitting close event");
    this.closed.emit();
  }

  async onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    
    if (!file) return;

    this.isUploading = true;
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
      this.profileImageUrl = imageUrl;
      
      this.showSnackbar('Profile image uploaded. Click update to save.', 'success');
    } catch (error) {
      console.error('Error uploading image:', error);
      this.showSnackbar('Failed to upload image', 'error');
    } finally {
      this.isUploading = false;
    }
  }

  private showSnackbar(message: string, type: 'success' | 'error' | 'loading') {
    this.snackBar.open(message, 'Close', {
      duration: type === 'loading' ? 0 : 5000,
      panelClass: [`snackbar-${type}`]
    });
  }

  onSubmit() {
    // Reset error message
    this.errorMessage = '';
    
    console.log("Attempting to Update the userProfile");
    this.isUpdating = true;
    
    console.log("Updating Profile with: =========== ", this.firstName, this.lastName, this.profileImageUrl);
    const updateData = {
      firstName: this.firstName,
      lastName: this.lastName,
      profileImageUrl: this.profileImageUrl
    };

    this.authService.update(updateData).subscribe({
      next: (response) => {
        console.log("Successfully updated the profile", response);
        
        // After successful update, refresh profile data
        this.authService.getProfile().subscribe({
          next: () => {
            console.log("Profile refreshed after update");
            this.isUpdating = false;
            this.closeModal(); // Close modal only after everything is complete
          },
          error: (err) => {
            console.error("Failed to refresh profile", err);
            this.isUpdating = false;
          }
        });
      },
      error: (error) => {
        console.error("Failed to update User Profile", error);
        this.isUpdating = false;
        this.errorMessage = error.error?.message || 'Update Profile Failed. Please try again later';
      }
    });
  }
}