import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputComponent } from '../../../shared/components/input/input.component';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { Mail, Lock } from 'lucide-angular';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, InputComponent, ButtonComponent],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  isLoggingIn: boolean = false;

  readonly emailIcon = Mail;
  readonly passwordIcon = Lock;

  onSubmit() {
    console.log('Login attempt with:', this.email);
    // You would typically call an auth service here

    this.isLoggingIn = true;

    setTimeout(() => {
      this.isLoggingIn = false;
    }, 2000);

    console.log("done timing out");
  }
}