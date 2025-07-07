import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
// import { CommonModule } from '@angular/common'; // Uncomment if you use *ngIf, *ngFor

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: '<router-outlet></router-outlet>'
})
export class AppComponent {}
