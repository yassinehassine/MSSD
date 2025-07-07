import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-flexstart-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.flexstart-layout.html',
  styleUrls: ['./app.flexstart-layout.scss']
})
export class AppFlexstartLayout {} 