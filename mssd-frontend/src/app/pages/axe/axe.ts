import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ThemeService } from '../../services/theme.service';
import { Theme } from '../../model/theme.model';

@Component({
  selector: 'app-axe',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './axe.html',
  styleUrl: './axe.scss'
})
export class Axe implements OnInit {
  themes: Theme[] = [];
  isLoading = true;
  error = '';

  constructor(private themeService: ThemeService) {}

  ngOnInit() {
    this.loadPublishedThemes();
  }

  loadPublishedThemes() {
    this.isLoading = true;
    this.themeService.getPublishedThemes().subscribe({
      next: (data) => {
        this.themes = data;
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Failed to load themes.';
        this.isLoading = false;
      }
    });
  }
}