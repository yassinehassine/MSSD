import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ThemeService } from '../../services/theme.service';
import { Theme } from '../../model/annexes.model';
import { PaginationComponent } from '../../shared/pagination/pagination.component';

@Component({
  selector: 'app-annexes',
  standalone: true,
  imports: [CommonModule, RouterModule, PaginationComponent],
  templateUrl: './annexes.html',
  styleUrls: ['./annexes.scss']
})
export class Annexes implements OnInit {
  themes: Theme[] = [];
  paginatedThemes: Theme[] = [];
  loading = true;
  error = '';

  // Pagination
  currentPage = 1;
  itemsPerPage = 9; // 3x3 grid
  totalItems = 0;

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {
    this.loadThemesWithFormations();
  }

  loadThemesWithFormations(): void {
    this.loading = true;
    console.log('🔍 Loading themes with formations...');
    this.themeService.getThemesWithFormations().subscribe({
      next: (themes) => {
        console.log('✅ Themes loaded:', themes);
        console.log('📊 Number of themes:', themes.length);
        this.themes = themes;
        this.totalItems = themes.length;
        this.updatePaginatedThemes();
        this.loading = false;
      },
      error: (err) => {
        console.error('❌ Error loading themes:', err);
        console.error('Response status:', err.status);
        console.error('Response message:', err.message);
        this.error = 'Erreur lors du chargement des formations.';
        this.loading = false;
      }
    });
  }

  getLevelLabel(level: string): string {
    switch (level) {
      case 'BEGINNER': return 'Débutant';
      case 'INTERMEDIATE': return 'Intermédiaire';
      case 'EXPERT': return 'Expert';
      default: return level;
    }
  }

  getLevelClass(level: string): string {
    switch (level) {
      case 'BEGINNER': return 'badge bg-success';
      case 'INTERMEDIATE': return 'badge bg-warning';
      case 'EXPERT': return 'badge bg-danger';
      default: return 'badge bg-secondary';
    }
  }

  getActiveFormationsCount(theme: Theme): number {
    // Since we only receive active/published formations from the API,
    // all formations in the theme are considered active
    return theme.formations?.length || 0;
  }

  getFormationLevels(theme: Theme): number {
    if (!theme.formations) return 0;
    const levels = new Set(theme.formations.map(f => f.level));
    return levels.size;
  }

  updatePaginatedThemes() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedThemes = this.themes.slice(startIndex, endIndex);
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.updatePaginatedThemes();
    // Scroll to top of annexes section
    const annexesSection = document.getElementById('hero-annexes');
    if (annexesSection) {
      annexesSection.scrollIntoView({ behavior: 'smooth' });
    }
  }

  debugThemes(): void {
    console.log('🐛 Starting debug...');
    this.themeService.getAllThemes().subscribe({
      next: (themes) => {
        console.log('🎯 All themes (simple call):', themes);
        alert(`Debug: Trouvé ${themes.length} thèmes. Voir la console pour plus de détails.`);
      },
      error: (err) => {
        console.error('🐛 Debug error:', err);
        alert('Erreur de debug - voir la console');
      }
    });
  }

  fixThemes(): void {
    console.log('🔧 Starting theme fix...');
    this.themeService.fixThemes().subscribe({
      next: (result) => {
        console.log('✅ Fix result:', result);
        alert(`Correction terminée: ${result.message || 'Success'}`);
        // Reload themes after fix
        this.loadThemesWithFormations();
      },
      error: (err) => {
        console.error('❌ Fix error:', err);
        alert('Erreur lors de la correction - voir la console');
      }
    });
  }
}