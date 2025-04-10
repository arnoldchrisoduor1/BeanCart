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
  @Input() value: string = '';
  @Input() icon: any = null;
  
  @Output() valueChange = new EventEmitter<string>();

  showPassword: boolean = false;
  readonly eyeIcon = Eye;
  readonly eyeOffIcon = EyeOff;

  onValueChange(newValue: string) {
    this.value = newValue;
    this.valueChange.emit(newValue);
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}