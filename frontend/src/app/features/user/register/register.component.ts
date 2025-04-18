import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { InputComponent } from '../../../shared/components/input/input.component';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { DropdownComponent } from '../../../shared/components/dropdown/dropdown.component';
import { Mail, Lock, UserCog } from 'lucide-angular';
import { AuthService } from '../../../core/auth/auth.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [FormsModule, InputComponent, ButtonComponent, DropdownComponent, NgIf, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  email: string = '';
  password: string = '';
  confirmPassword: string = '';
  isRegistering: boolean = false;
  selectedOption: string = '';
  errorMessage: string = '';

  readonly emailIcon = Mail;
  readonly passwordIcon = Lock;
  readonly userCog = UserCog;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit() {
    // Reset error message
    this.errorMessage = '';
    
    // Validate form
    if (!this.email || !this.password || !this.confirmPassword || !this.selectedOption) {
      this.errorMessage = 'Please fill in all fields';
      return;
    }

    // Validate passwords match
    if (this.password !== this.confirmPassword) {
      this.errorMessage = "Passwords don't match";
      return;
    }

    console.log("Registering attempt with: ", this.email);
    this.isRegistering = true;

    // Prepare the request payload
    const userData = {
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword,
      role: this.selectedOption
    };

    // Make the registration request
    this.authService.register(userData).subscribe({
      next: (response) => {
        console.log('Registration successful', response);
        this.isRegistering = false;
        // Navigate to login page after successful registration
        this.router.navigate(['/user/login']);
      },
      error: (error) => {
        console.error('Registration failed', error);
        this.isRegistering = false;
        this.errorMessage = error.error?.message || 'Registration failed. Please try again.';
      }
    });
  }
}