import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { PortfolioService } from '../../services/portfolio.service';
import { CalendarService, CalendarDto } from '../../services/calendar.service';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home implements OnDestroy {
  latestPortfolios: any[] = [];
  upcomingEvents: CalendarDto[] = [];
  private refreshSubscription?: Subscription;

  constructor(private portfolioSvc: PortfolioService, private calendarSvc: CalendarService) {}

  ngOnInit() {
    this.portfolioSvc.getActivePortfolios().subscribe({
      next: (items) => (this.latestPortfolios = (items || []).slice(0, 6)),
      error: () => (this.latestPortfolios = [])
    });

    // Load initial events
    this.loadUpcomingEvents();

    // Set up automatic refresh every 5 minutes (300000 ms)
    this.refreshSubscription = interval(300000).subscribe(() => {
      this.loadUpcomingEvents();
    });
  }

  ngOnDestroy() {
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
    }
  }

  private loadUpcomingEvents() {
    this.calendarSvc.getNextTwoWeeksEvents().subscribe({
      next: (events) => {
        // Filter only available events and sort by start time
        this.upcomingEvents = (events || [])
          .filter(event => event.status === 'AVAILABLE')
          .sort((a, b) => new Date(a.startTime).getTime() - new Date(b.startTime).getTime());
      },
      error: () => (this.upcomingEvents = [])
    });
  }
}
