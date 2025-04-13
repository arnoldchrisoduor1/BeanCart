import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LucideAngularModule } from 'lucide-angular';
import { User, X, UserRoundPen  } from 'lucide-angular';
import { InputComponent } from '../input/input.component';
import { ButtonComponent } from "../button/button.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-update-profile-modal',
  standalone: true,
  imports: [LucideAngularModule, InputComponent, ButtonComponent, CommonModule],
  templateUrl: './update-profile-modal.component.html',
  styleUrl: './update-profile-modal.component.css'
})
export class UpdateProfileModalComponent {

  @Input() isVisible: boolean = false;
  @Output() closed = new EventEmitter<void>();

  readonly userIcon = User;
  readonly closeIcon = X;
  readonly userPen = UserRoundPen;

  firstName: string = ''
  lastName: string = ''

  closeModal() {
    console.log("child emitting clode event")
    this.closed.emit();
  }

}
