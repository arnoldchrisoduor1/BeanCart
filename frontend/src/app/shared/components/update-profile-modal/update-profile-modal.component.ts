import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LucideAngularModule } from 'lucide-angular';
import { User, X, UserRoundPen  } from 'lucide-angular';
import { InputComponent } from '../input/input.component';
import { ButtonComponent } from "../button/button.component";
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { AuthStateService } from '../../../core/auth/auth-state.service';
import { AuthService } from '../../../core/auth/auth.service';
import { FormsModule } from '@angular/forms';

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
  errorMessage: string = '';

  readonly userIcon = User;
  readonly closeIcon = X;
  readonly userPen = UserRoundPen;

  firstName: string = ''
  lastName: string = ''

  constructor(
    private authStateService: AuthStateService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.authSubscription = this.authStateService.currentUser$.subscribe(user => {
      this.isLoggedIn = !!user;
      this.userProfile = user;
      // initializing the user details.
      this.firstName = user?.firstName || '';
      this.lastName = user?.lastName || '';
    })
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  closeModal() {
    console.log("child emitting clode event")
    this.closed.emit();
  }


  // this will be used to update the user - firstname and lastname.
  onSubmit() {
    // Reset error message
    this.errorMessage = '';
    
    console.log("Attempting to Update the userProfile");
    this.isUpdating = true;
    
    console.log("Updating Profile with: =========== ", this.firstName, this.lastName);
    const updateData = {
      firstName: this.firstName,
      lastName: this.lastName
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
