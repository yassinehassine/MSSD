import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TranslationService } from '../../services/translation.service';

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

  constructor(public translationService: TranslationService) {}

  t(key: string): string {
    return this.translationService.translate(key);
  }

  onImgError(e: Event) {
    const img = e.target as HTMLImageElement;
    if (img && img.src.indexOf(this.fallbackImage) === -1) {
      img.src = this.fallbackImage;
    }
  }
}
