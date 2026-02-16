import { Component, OnInit, OnDestroy } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { PortfolioService } from '../../services/portfolio.service';
import { CalendarService, CalendarDto } from '../../services/calendar.service';
import { FormationService, Formation } from '../../services/formation.service';
import { PaginationComponent } from '../../shared/pagination/pagination.component';
import { Portfolio } from '../../model/portfolio.model';
import { TranslationService } from '../../services/translation.service';

interface StatCounter {
  icon: string;
  label: string;
  target: number;
  current: number;
  suffix: string;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, NgOptimizedImage],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home implements OnInit, OnDestroy {
  // Getter for total event pages
  get totalEventPages(): number {
    return Math.ceil(this.eventsTotalItems / this.eventsItemsPerPage) || 1;
  }

  // Portfolio section
  portfolios: Portfolio[] = [];
  isPortfolioScrollPaused: boolean = false;

  // Events section
  upcomingEvents: CalendarDto[] = [];
  paginatedEvents: CalendarDto[] = [];
  eventsCurrentPage = 1;
  eventsItemsPerPage = 3;
  eventsTotalItems = 0;

  // Stats counters
  statsCounters: StatCounter[] = [
    { icon: 'bi bi-mortarboard-fill', label: 'Formations', target: 50, current: 0, suffix: '+' },
    { icon: 'bi bi-building', label: 'Entreprises formées', target: 200, current: 0, suffix: '+' },
    { icon: 'bi bi-people-fill', label: 'Apprenants', target: 5000, current: 0, suffix: '+' },
    { icon: 'bi bi-star-fill', label: 'Satisfaction', target: 98, current: 0, suffix: '%' }
  ];
  private statsAnimated = false;
  private statsObserver!: IntersectionObserver;

  // Dynamic formations
  topFormations: Formation[] = [];

  constructor(
    private portfolioSvc: PortfolioService,
    private calendarSvc: CalendarService,
    private formationSvc: FormationService,
    public translationService: TranslationService
  ) {}

  t(key: string): string {
    return this.translationService.translate(key);
  }

  ngOnInit() {
    // Load portfolios
    this.portfolioSvc.getActivePortfolios().subscribe({
      next: (items) => {
        this.portfolios = items || [];
      },
      error: () => { this.portfolios = []; }
    });

    // Load calendar events
    this.calendarSvc.getAvailableCalendars().subscribe({
      next: (events) => {
        this.upcomingEvents = events || [];
        this.eventsTotalItems = this.upcomingEvents.length;
        this.updatePaginatedEvents();
      },
      error: () => {
        this.upcomingEvents = [];
        this.eventsTotalItems = 0;
        this.updatePaginatedEvents();
      }
    });

    // Load formations dynamically
    this.formationSvc.getAllFormations().subscribe({
      next: (formations) => {
        this.topFormations = (formations || [])
          .filter(f => f.published)
          .slice(0, 3);
      },
      error: () => { this.topFormations = []; }
    });

    // Setup stats counter observer
    this.setupStatsObserver();
  }

  ngOnDestroy(): void {
    this.statsObserver?.disconnect();
  }

  private setupStatsObserver(): void {
    setTimeout(() => {
      const statsSection = document.querySelector('.stats-section');
      if (!statsSection) return;

      this.statsObserver = new IntersectionObserver(
        (entries) => {
          if (entries[0].isIntersecting && !this.statsAnimated) {
            this.statsAnimated = true;
            this.animateCounters();
          }
        },
        { threshold: 0.3 }
      );
      this.statsObserver.observe(statsSection);
    }, 500);
  }

  private animateCounters(): void {
    this.statsCounters.forEach(stat => {
      const duration = 2000;
      const steps = 60;
      const increment = stat.target / steps;
      let current = 0;
      const interval = setInterval(() => {
        current += increment;
        if (current >= stat.target) {
          stat.current = stat.target;
          clearInterval(interval);
        } else {
          stat.current = Math.floor(current);
        }
      }, duration / steps);
    });
  }

  getFormationIcon(category: string): string {
    const icons: { [key: string]: string } = {
      'Management': 'bi bi-people',
      'Marketing': 'bi bi-graph-up',
      'Development': 'bi bi-code-slash',
      'Analytics': 'bi bi-bar-chart-line',
      'Sales': 'bi bi-trophy',
    };
    return icons[category] || 'bi bi-book';
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
      const path = portfolio.logoUrl.startsWith('uploads/') ? portfolio.logoUrl.substring(8) : portfolio.logoUrl;
      return `/api/files/${path}`;
    }

    // Final fallback to default logo
    return 'assets/img/logo1.png';
  }

  // Get proper image URL for logos (legacy method)
  getImageUrl(portfolio: Portfolio): string {
    return this.getPortfolioImageUrl(portfolio);
  }

}
