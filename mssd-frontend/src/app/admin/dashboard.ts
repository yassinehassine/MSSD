import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ThemeService } from '../services/theme.service';
import { Theme } from '../model/annexes.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  themes: Theme[] = [];
  themeStats = {
    total: 0,
    active: 0,
    totalFormations: 0
  };
  loading = true;

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {
    this.loadThemeStats();
  }

  loadThemeStats(): void {
    this.themeService.getAllThemesAdmin().subscribe({
      next: (themes) => {
        this.themes = themes;
        this.themeStats = {
          total: themes.length,
          active: themes.filter(t => t.active).length,
          totalFormations: themes.reduce((sum, t) => sum + (t.formations?.length || 0), 0)
        };
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading theme stats:', err);
        this.loading = false;
      }
    });
  }
}
