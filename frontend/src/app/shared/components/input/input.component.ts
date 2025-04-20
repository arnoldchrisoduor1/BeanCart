import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LucideAngularModule, Eye, EyeOff } from 'lucide-angular';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [FormsModule, LucideAngularModule, NgIf],
  templateUrl: './input.component.html',
  styleUrl: './input.component.css'
})
export class InputComponent {
  @Input() type: string = 'text';
  @Input() id: string = '';
  @Input() name: string = '';
  @Input() placeholder: string = '';
  @Input() required: boolean = false;
  @Input() value: string | number | undefined = '';  // Changed to accept string or number
  @Input() icon: any = null;
  
  @Output() valueChange = new EventEmitter<string | number>();  // Updated to emit string or number

  showPassword: boolean = false;
  readonly eyeIcon = Eye;
  readonly eyeOffIcon = EyeOff;

  onValueChange(newValue: string) {
    let emittedValue: string | number = newValue;
    
    // Convert to number if input type is number and value is not empty
    if (this.type === 'number' && newValue !== '') {
      emittedValue = newValue.includes('.') ? parseFloat(newValue) : parseInt(newValue, 10);
    }
    
    this.value = emittedValue;
    this.valueChange.emit(emittedValue);
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}