import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { PortfolioService } from '../../services/portfolio.service';
import { CalendarService, CalendarDto } from '../../services/calendar.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {
  latestPortfolios: any[] = [];
  upcomingEvents: CalendarDto[] = [];

  constructor(private portfolioSvc: PortfolioService, private calendarSvc: CalendarService) {}

  ngOnInit() {
    this.portfolioSvc.getActivePortfolios().subscribe({
      next: (items) => (this.latestPortfolios = (items || []).slice(0, 6)),
      error: () => (this.latestPortfolios = [])
    });

    // Get events for the next 2 weeks
    this.calendarSvc.getUpcomingEventsNext2Weeks().subscribe({
      next: (events) => (this.upcomingEvents = (events || []).slice(0, 5)),
      error: () => (this.upcomingEvents = [])
    });
  }

  // Get company logos for horizontal scrolling animation
  getCompanyLogos(): string[] {
    const logos = this.latestPortfolios
      .filter(p => p.companyLogo && p.companyLogo.trim() !== '')
      .map(p => this.getLogoUrl(p.companyLogo!));
    // Remove duplicates
    return [...new Set(logos)];
  }

  // Get logo URL similar to image URL handling
  getLogoUrl(logoPath: string | undefined): string {
    if (!logoPath || logoPath.trim() === '') {
      // Use a default company logo or return empty string
      return '';
    }
    return this.portfolioSvc.getImageUrl(logoPath);
  }

  // Handle logo loading errors
  onLogoError(event: any): void {
    console.error('Company logo failed to load:', event.target.src);
    // Hide the logo if it fails to load
    (event.target as HTMLElement).style.display = 'none';
  }
}
