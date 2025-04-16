import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-color-selection',
  standalone: true,
  templateUrl: './color-selection.component.html',
  styleUrls: ['./color-selection.component.css']
})
export class ColorSelectionComponent {
  colors = ['indigo-500', 'emerald-500', 'rose-500', 'amber-500', 'violet-500'];
  selectedColor: string | null = null;

  @Output() colorSelected = new EventEmitter<string>();

  selectColor(color: string) {
    this.selectedColor = color;
    this.colorSelected.emit(color);
  }
}