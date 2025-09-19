import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { ThemeService } from '../../services/theme.service';
import { FormationService } from '../../services/formation.service';
import { Theme, Formation } from '../../model/theme.model';

@Component({
  selector: 'app-theme-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './theme-detail.html',
  styleUrl: './theme-detail.scss'
})
export class ThemeDetail implements OnInit {
  theme: Theme | null = null;
  formations: Formation[] = [];
  isLoading = true;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private themeService: ThemeService,
    private formationService: FormationService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      const slug = params['slug'];
      if (slug) {
        this.loadThemeBySlug(slug);
      }
    });
  }

  loadThemeBySlug(slug: string) {
    this.isLoading = true;
    this.themeService.getThemeBySlug(slug).subscribe({
      next: (theme) => {
        this.theme = theme;
        this.loadFormationsForTheme(theme.id);
      },
      error: () => {
        this.error = 'Thème non trouvé.';
        this.isLoading = false;
      }
    });
  }

  loadFormationsForTheme(themeId: number) {
    this.formationService.getPublishedFormationsByTheme(themeId).subscribe({
      next: (formations) => {
        this.formations = formations;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Erreur lors du chargement des formations.';
        this.isLoading = false;
      }
    });
  }
}