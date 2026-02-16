import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { TranslationService } from './services/translation.service';

@Component({
  selector: 'app-flexstart-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.flexstart-layout.html',
  styleUrls: ['./app.flexstart-layout.scss']
})
export class AppFlexstartLayout {
  currentLanguage: string = 'fr';
  isMobileMenuOpen: boolean = false;
  dropdowns: { [key: string]: boolean } = {};
  mobileSections: { [key: string]: boolean } = {};
  private hideTimeouts: { [key: string]: any } = {};

  constructor(public translationService: TranslationService) {
    this.translationService.currentLanguage$.subscribe(lang => {
      this.currentLanguage = lang;
    });
  }

  toggleLanguage(): void {
    const newLang = this.currentLanguage === 'fr' ? 'en' : 'fr';
    this.translationService.switchLanguage(newLang);
  }

  t(key: string): string {
    return this.translationService.translate(key);
  }

  // Mobile Menu Methods
  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  closeMobileMenu(): void {
    this.isMobileMenuOpen = false;
  }

  // Dropdown Methods
  showDropdown(name: string): void {
    // Clear any existing timeout for this specific dropdown
    if (this.hideTimeouts[name]) {
      clearTimeout(this.hideTimeouts[name]);
      this.hideTimeouts[name] = null;
    }
    this.dropdowns[name] = true;
  }

  hideDropdown(name: string): void {
    // Set a small delay to prevent flickering when moving between dropdown elements
    this.hideTimeouts[name] = setTimeout(() => {
      this.dropdowns[name] = false;
      this.hideTimeouts[name] = null;
    }, 100);
  }

  // Method to immediately close dropdown (for click events)
  closeDropdown(name: string): void {
    if (this.hideTimeouts[name]) {
      clearTimeout(this.hideTimeouts[name]);
      this.hideTimeouts[name] = null;
    }
    this.dropdowns[name] = false;
  }

  // Mobile Section Methods
  toggleMobileSection(name: string): void {
    this.mobileSections[name] = !this.mobileSections[name];
  }
} 