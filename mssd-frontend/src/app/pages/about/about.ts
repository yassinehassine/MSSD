import { Component } from '@angular/core';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [],
  templateUrl: './about.html',
  styleUrl: './about.scss'
})
export class About {
  // Primary and fallback images (absolute from web root)
  // Use your requested image name (place it in src/assets/img/about/about-us.jpg)
  // Add a cache-busting query so the browser fetches the latest file after you copy it
  aboutImage = `/assets/img/about/about-us.jpg?v=${Date.now()}`;
  private readonly fallbackImage = '/assets/img/about.jpg';

  onImgError(e: Event) {
    const img = e.target as HTMLImageElement;
    if (img && img.src.indexOf(this.fallbackImage) === -1) {
      img.src = this.fallbackImage;
    }
  }
}
