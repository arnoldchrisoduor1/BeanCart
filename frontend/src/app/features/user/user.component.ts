import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <div class="">
      <router-outlet></router-outlet>
    </div>
  `
})
export class UserComponent {}