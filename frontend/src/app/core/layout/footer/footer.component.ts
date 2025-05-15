import { Component } from '@angular/core';
import { 
  LucideAngularModule,
  ChevronRight,
  MapPin,
  Phone,
  Mail,
  Send,
  Facebook,
  Twitter,
  Instagram,
  Linkedin,
  Heart
} from 'lucide-angular';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {
  // Quick Links icons
  readonly chevronRight = ChevronRight;

  email = 'info@beancart.com';
  
  // Contact Info icons
  readonly mapPin = MapPin;
  readonly phone = Phone;
  readonly mail = Mail;
  
  // Newsletter icons
  readonly send = Send;
  
  // Social Media icons
  readonly facebook = Facebook;
  readonly twitter = Twitter;
  readonly instagram = Instagram;
  readonly linkedin = Linkedin;
  
  // Crafted with love icon
  readonly heart = Heart;
}