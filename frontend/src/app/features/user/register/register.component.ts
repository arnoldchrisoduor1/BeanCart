import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputComponent } from '../../../shared/components/input/input.component';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { DropdownComponent } from '../../../shared/components/dropdown/dropdown.component';
import { Mail, Lock, UserCog  } from 'lucide-angular';

@Component({
  selector: 'app-register',
  imports: [FormsModule, InputComponent, ButtonComponent, DropdownComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  email: string = '';
  password: string = '';
  confirmPassword: string ='';
  role: string = '';
  isRegistering: boolean = false;
  selectedOption: string = '';

  readonly emailIcon = Mail;
  readonly passwordIcon = Lock;
  readonly userCog = UserCog;

  onSubmit()  {
    console.log("Registering attempt with: ", this.email);
    this.isRegistering = true;

    setTimeout(() => {
      this.isRegistering = false;
    }, 2000);

    console.log("done registering");
  }
}
