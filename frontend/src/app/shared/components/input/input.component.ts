import { Component, Input, Output, EventEmitter, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { LucideAngularModule, Eye, EyeOff } from 'lucide-angular';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [FormsModule, LucideAngularModule, NgIf],
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true
    }
  ]
})
export class InputComponent implements ControlValueAccessor {
  @Input() type: string = 'text';
  @Input() id: string = '';
  @Input() name: string = '';
  @Input() placeholder: string = '';
  @Input() required: boolean = false;
  @Input() value: string | number | undefined = '';  
  @Input() icon: any = null;

  @Output() valueChange = new EventEmitter<string | number>();

  showPassword: boolean = false;
  readonly eyeIcon = Eye;
  readonly eyeOffIcon = EyeOff;

  private onChange: (value: string | number) => void = () => {};
  private onTouched: () => void = () => {};

  onValueChange(newValue: string) {
    let emittedValue: string | number = newValue;
    if (this.type === 'number' && newValue !== '') {
      emittedValue = newValue.includes('.') ? parseFloat(newValue) : parseInt(newValue, 10);
    }

    this.value = emittedValue;
    this.onChange(emittedValue);  // Notify the parent form control
    this.valueChange.emit(emittedValue);
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  // Implement ControlValueAccessor methods
  writeValue(value: string | number): void {
    this.value = value;
  }

  registerOnChange(fn: (value: string | number) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    // Optional: Handle disabled state if needed
  }
}
