import { Component } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { PortfolioService } from '../../services/portfolio.service';
import { CalendarService, CalendarDto } from '../../services/calendar.service';
import { PaginationComponent } from '../../shared/pagination/pagination.component';
import { Portfolio } from '../../model/portfolio.model';
import { TranslationService } from '../../services/translation.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, NgOptimizedImage],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {
  // Getter for total event pages
  get totalEventPages(): number {
    return Math.ceil(this.eventsTotalItems / this.eventsItemsPerPage) || 1;
  }
  // Portfolio section (backoffice integration)
  portfolios: Portfolio[] = [];
  isPortfolioScrollPaused: boolean = false;

  // Events section
  upcomingEvents: CalendarDto[] = [];
  paginatedEvents: CalendarDto[] = [];
  eventsCurrentPage = 1;
  eventsItemsPerPage = 3; // Show only 3 events at a time
  eventsTotalItems = 0;

  constructor(
  private portfolioSvc: PortfolioService,
    private calendarSvc: CalendarService,
    public translationService: TranslationService
  ) {}

  t(key: string): string {
    return this.translationService.translate(key);
  }

  ngOnInit() {
    console.log('🏠 Home Page: Initializing...');
    
    // Load portfolios (same as backoffice)
    this.portfolioSvc.getActivePortfolios().subscribe({
      next: (items) => {
        this.portfolios = items || [];
        console.log(`✅ Home Page: Loaded ${this.portfolios.length} portfolios for display`);
      },
      error: (err) => {
        console.error('❌ Home Page: Error loading portfolios', err);
        this.portfolios = [];
      }
    });

    // Load calendar events
    this.calendarSvc.getAvailableCalendars().subscribe({
      next: (events) => {
        this.upcomingEvents = events || [];
        this.eventsTotalItems = this.upcomingEvents.length;
        this.updatePaginatedEvents();
        console.log(`✅ Home Page: Loaded ${this.upcomingEvents.length} events`);
      },
      error: (err) => {
        console.error('❌ Home Page: Error loading events', err);
        this.upcomingEvents = [];
        this.eventsTotalItems = 0;
        this.updatePaginatedEvents();
      }
    });
  }



  // Events pagination methods
  updatePaginatedEvents() {
    const startIndex = (this.eventsCurrentPage - 1) * this.eventsItemsPerPage;
    const endIndex = startIndex + this.eventsItemsPerPage;
    this.paginatedEvents = this.upcomingEvents.slice(startIndex, endIndex);
  }

  onEventsPageChange(page: number) {
    this.eventsCurrentPage = page;
    this.updatePaginatedEvents();
    // Scroll to events section
    const eventsSection = document.getElementById('recent-posts');
    if (eventsSection) {
      eventsSection.scrollIntoView({ behavior: 'smooth' });
    }
  }

  // (Removed: getDisplayLogos, as only new portfolio items are used)

  // Portfolio scroll control methods
  pausePortfolioScroll(): void {
    this.isPortfolioScrollPaused = true;
  }

  resumePortfolioScroll(): void {
    this.isPortfolioScrollPaused = false;
  }

  // Get proper image URL for portfolio items
  getPortfolioImageUrl(portfolio: Portfolio): string {
    // Check for logoUrl field (new PortfolioItem structure)
    if (portfolio.logoUrl && portfolio.logoUrl.trim() !== '') {
      // If it's already a full URL, use it directly
      if (portfolio.logoUrl.startsWith('http')) {
        return portfolio.logoUrl;
      }
      // Otherwise, construct the URL via proxy
      return `/api/files/${portfolio.logoUrl}`;
    }

    // Final fallback to default logo
    return 'assets/img/logo1.png';
  }

  // Get proper image URL for logos (legacy method)
  getImageUrl(portfolio: Portfolio): string {
    return this.getPortfolioImageUrl(portfolio);
  }

}
