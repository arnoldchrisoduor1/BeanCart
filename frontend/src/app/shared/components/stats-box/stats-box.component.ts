import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-stats-box',
  imports: [CommonModule],
  templateUrl: './stats-box.component.html',
  styleUrl: './stats-box.component.css'
})
export class StatsBoxComponent {
  @Input() title: string = 'Title';
  @Input() value: number = 0;
  @Input() status: string = '';
}
