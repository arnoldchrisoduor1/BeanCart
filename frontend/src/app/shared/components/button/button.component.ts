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
  
  return `w-full bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 text-white font-medium py-3 px-6 rounded-lg shadow-md hover:shadow-lg transition-all duration-300 transform hover:-translate-y-0.5 ${colorClasses} ${
    (this.disabled || this.isLoading) ? 'cursor-not-allowed' : ''
  }`;
}

  onClick() {
    if (!this.disabled && !this.isLoading) {
      this.clicked.emit();
    }
  }
}