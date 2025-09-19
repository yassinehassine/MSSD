import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  private currentLanguageSubject = new BehaviorSubject<string>('fr');
  public currentLanguage$ = this.currentLanguageSubject.asObservable();

  constructor() {
    // Get saved language from localStorage or default to French
    const savedLanguage = localStorage.getItem('mssd-language') || 'fr';
    this.currentLanguageSubject.next(savedLanguage);
  }

  getCurrentLanguage(): string {
    return this.currentLanguageSubject.value;
  }

  toggleLanguage(): void {
    const currentLang = this.getCurrentLanguage();
    const newLang = currentLang === 'fr' ? 'en' : 'fr';
    this.setLanguage(newLang);
  }

  setLanguage(language: string): void {
    this.currentLanguageSubject.next(language);
    localStorage.setItem('mssd-language', language);
    // Update document language attribute
    document.documentElement.lang = language;
  }

  isEnglish(): boolean {
    return this.getCurrentLanguage() === 'en';
  }

  isFrench(): boolean {
    return this.getCurrentLanguage() === 'fr';
  }
}