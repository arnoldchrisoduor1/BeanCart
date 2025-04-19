import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NgIf } from '@angular/common';
import { InputComponent } from '../../../shared/components/input/input.component';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { AuthService } from '../../../core/auth/auth.service';
import { Mail, Lock } from 'lucide-angular';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, InputComponent, ButtonComponent, NgIf, RouterModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  isLoggingIn: boolean = false;
  errorMessage: string = '';
  
  readonly emailIcon = Mail;
  readonly passwordIcon = Lock;
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}
  
  onSubmit() {
    // Reseting the error message
    this.errorMessage = '';
    
    // validating the form
    if (!this.email || !this.password) {
      this.errorMessage = "Please fill in all the fields";
      return;
    }
    
    console.log("Attempting to sign in user: ", this.email);
    this.isLoggingIn = true;
    
    // Preparing the request payload.
    const userData = {
      email: this.email,
      password: this.password
    };
    
    // making the sign in request.
    this.authService.login(userData).subscribe({
      next: (response) => {
        console.log("Login successful", response);
        this.isLoggingIn = false;
        // navigating to home page.
        this.router.navigate(['/user/profile']);
      },
      error: (error) => {
        console.error("Failed to sign in:", error);
        this.isLoggingIn = false;
        this.errorMessage = error.error?.message || 'Sign in failed. Please try again';
      }
    });
  }
}