import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule, ChevronDown } from 'lucide-angular';

@Component({
  selector: 'app-dropdown',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule, NgFor],
  templateUrl: './dropdown.component.html',
  styleUrl: './dropdown.component.css'
})
export class DropdownComponent {

  @Input() title: string = 'Select';
  @Input() options: any[] = [];
  @Input() selectedValue: any = null;

  @Output() selectedValueChange = new EventEmitter<any>();

  isOpen: boolean = false;
  readonly chevronDown = ChevronDown;

  toggleDropDown() {
    this.isOpen = !this.isOpen;
  }

  selectOption(option: any) {
    this.selectedValue = option;
    this.selectedValueChange.emit(option);
    this.isOpen = false;
  }

}
