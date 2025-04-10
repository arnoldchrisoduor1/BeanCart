import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule], // Import CommonModule for ngClass
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.css']
})
export class ButtonComponent {
  @Input() type: 'button' | 'submit' | 'reset' = 'button';
  @Input() disabled: boolean = false;
  @Input() isLoading: boolean = false;
  @Input() defaultText: string = 'Submit';
  @Input() loadingText: string = 'Processing...';
  @Input() color: string = 'indigo';
  
  @Output() clicked = new EventEmitter<void>();

  // In button.component.ts
get buttonClasses() {
  let colorClasses = '';
  
  if (this.color === 'indigo') {
    colorClasses = this.disabled || this.isLoading 
      ? 'bg-indigo-300' 
      : 'bg-indigo-500 hover:bg-indigo-600';
  }
  // Add other color options as needed
  
  return `w-full p-2 rounded transition-colors duration-200 text-white ${colorClasses} ${
    (this.disabled || this.isLoading) ? 'cursor-not-allowed' : ''
  }`;
}

  onClick() {
    if (!this.disabled && !this.isLoading) {
      this.clicked.emit();
    }
  }
}