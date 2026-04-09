import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ThemeService } from '../services/theme.service';
import { ContactService } from '../services/contact.service';
import { BlogService } from '../services/blog.service';
import { PortfolioService } from '../services/portfolio.service';
import { CalendarService } from '../services/calendar.service';
import { AnnexRequestService } from '../services/annex-request.service';
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
  contactCount: number | null = null;
  blogCount: number | null = null;
  portfolioCount: number | null = null;
  calendarCount: number | null = null;
  pendingRequestsCount: number | null = null;
  loading = true;

  constructor(
    private themeService: ThemeService,
    private contactService: ContactService,
    private blogService: BlogService,
    private portfolioService: PortfolioService,
    private calendarService: CalendarService,
    private annexRequestService: AnnexRequestService
  ) {}

  ngOnInit(): void {
    this.loadThemeStats();
    this.loadContactCount();
    this.loadBlogCount();
    this.loadPortfolioCount();
    this.loadCalendarCount();
    this.loadPendingRequestsCount();
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

  loadContactCount(): void {
    this.contactService.getAllContacts().subscribe({
      next: (contacts) => { this.contactCount = contacts.length; },
      error: () => { this.contactCount = 0; }
    });
  }

  loadBlogCount(): void {
    this.blogService.getAllBlogsForAdmin().subscribe({
      next: (blogs: any[]) => { this.blogCount = blogs.length; },
      error: () => { this.blogCount = 0; }
    });
  }

  loadPortfolioCount(): void {
    this.portfolioService.getAllPortfolios().subscribe({
      next: (items) => { this.portfolioCount = items.length; },
      error: () => { this.portfolioCount = 0; }
    });
  }

  loadCalendarCount(): void {
    this.calendarService.getAllCalendars().subscribe({
      next: (events) => { this.calendarCount = events.length; },
      error: () => { this.calendarCount = 0; }
    });
  }

  loadPendingRequestsCount(): void {
    this.annexRequestService.getAllRequests().subscribe({
      next: (requests) => {
        this.pendingRequestsCount = requests.filter((r: any) => r.status === 'PENDING').length;
      },
      error: () => { this.pendingRequestsCount = 0; }
    });
  }
}
