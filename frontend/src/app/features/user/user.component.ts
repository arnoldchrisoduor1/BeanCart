import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <div class="max-w-md mx-auto mt-8 p-6 bg-white rounded shadow">
      <router-outlet></router-outlet>
    </div>
  `
})
export class UserComponent {}