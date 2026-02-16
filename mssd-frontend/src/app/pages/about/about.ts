import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './about.html',
  styleUrl: './about.scss'
})
export class About {
  aboutImage = `/assets/img/about/about-us.jpg?v=${Date.now()}`;
  private readonly fallbackImage = '/assets/img/about.jpg';

  onImgError(e: Event) {
    const img = e.target as HTMLImageElement;
    if (img && img.src.indexOf(this.fallbackImage) === -1) {
      img.src = this.fallbackImage;
    }
  }
}
