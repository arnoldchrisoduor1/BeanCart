import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-stats-box',
  imports: [],
  templateUrl: './stats-box.component.html',
  styleUrl: './stats-box.component.css'
})
export class StatsBoxComponent {
  @Input() title: string = 'Title';
  @Input() value: number = 0;
}
