import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { PortfolioService } from '../../services/portfolio.service';
import { CalendarService, CalendarDto } from '../../services/calendar.service';
import { LanguageService } from '../../services/language.service';

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

  constructor(
    private portfolioSvc: PortfolioService, 
    private calendarSvc: CalendarService,
    public languageService: LanguageService
  ) {}

  ngOnInit() {
    this.portfolioSvc.getActivePortfolios().subscribe({
      next: (items) => (this.latestPortfolios = (items || []).slice(0, 6)),
      error: () => (this.latestPortfolios = [])
    });

    this.calendarSvc.getAvailableCalendars().subscribe({
      next: (events) => (this.upcomingEvents = (events || []).slice(0, 5)),
      error: () => (this.upcomingEvents = [])
    });
  }
}
