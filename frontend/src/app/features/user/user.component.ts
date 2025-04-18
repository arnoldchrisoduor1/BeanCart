import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <div class="p-4">
      <router-outlet></router-outlet>
    </div>
  `
})
export class UserComponent {}