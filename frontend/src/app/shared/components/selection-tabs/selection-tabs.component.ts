import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-selection-tabs',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './selection-tabs.component.html',
  styleUrl: './selection-tabs.component.css'
})
export class SelectionTabsComponent {
  @Input() option :string = 'option'
  @Input() isActive: boolean = false;


  @Output() colorSelected = new EventEmitter<string>();
}
