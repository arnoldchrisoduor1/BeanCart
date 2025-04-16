import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { 
  LucideAngularModule,
  Star,
  StarHalf,
  User,
  ChevronDown,
  ChevronUp,
  Send 
} from 'lucide-angular';

@Component({
  selector: 'app-product-reviews',
  standalone: true,
  imports: [LucideAngularModule, FormsModule, CommonModule],
  templateUrl: './product-reviews.component.html',
  styleUrls: ['./product-reviews.component.css']
})
export class ProductReviewsComponent {
  // Sample data - to be replaced with real data later
  reviews = [
    {
      id: 1,
      user: 'Alex Johnson',
      rating: 5,
      date: '2023-10-15',
      comment: 'Excellent product! Exceeded my expectations. The quality is outstanding and it arrived sooner than expected.'
    },
    {
      id: 2,
      user: 'Sam Wilson',
      rating: 4,
      date: '2023-09-28',
      comment: 'Very good product, but the color was slightly different than shown in the pictures.'
    },
    {
      id: 3,
      user: 'Taylor Smith',
      rating: 3,
      date: '2023-09-10',
      comment: 'Average product. Does the job but not exceptional.'
    }
  ];

  // Icons
  readonly starIcon = Star;
  readonly starHalfIcon = StarHalf;
  readonly userIcon = User;
  readonly chevronDownIcon = ChevronDown;
  readonly chevronUpIcon = ChevronUp;
  readonly sendIcon = Send;

  // Form model
  newReview = {
    rating: 0,
    comment: '',
    name: ''
  };

  // Temporary hover state for rating stars
  hoverRating = 0;
}