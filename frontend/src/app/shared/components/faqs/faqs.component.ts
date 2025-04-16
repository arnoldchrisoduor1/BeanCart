import { Component, Input } from '@angular/core';
import { 
  LucideAngularModule, 
  ChevronDown, 
  ChevronUp 
} from 'lucide-angular';

@Component({
  selector: 'app-faqs',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './faqs.component.html',
  styleUrl: './faqs.component.css'
})
export class FaqsComponent {
  @Input() question: string = 'Question';
  @Input() answer: string = 'Description / answer';
  isExpanded: boolean = false;

  // Define icons as readonly properties
  readonly chevronDown = ChevronDown;
  readonly chevronUp = ChevronUp;

  toggleAnswer() {
    this.isExpanded = !this.isExpanded;
  }
}