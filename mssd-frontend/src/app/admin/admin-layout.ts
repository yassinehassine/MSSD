import { Component, AfterViewInit } from '@angular/core';
import { Router, NavigationEnd, RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { User } from '../model/user.model';

declare var bootstrap: any;

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  templateUrl: './admin-layout.html',
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive
  ],
  styleUrl: './dashboard.scss'
})
export class AdminLayout implements AfterViewInit {
  currentUser: User | null = null;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    // Subscribe to current user changes
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  ngAfterViewInit() {
    this.initSidebarToggle();
    this.initDropdowns();
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.initSidebarToggle();
        this.initDropdowns();
      }
    });
  }

  private initSidebarToggle() {
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle && !sidebarToggle.hasAttribute('data-initialized')) {
      sidebarToggle.setAttribute('data-initialized', 'true');
      sidebarToggle.addEventListener('click', event => {
        event.preventDefault();
        document.body.classList.toggle('sb-sidenav-toggled');
        localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled') ? 'true' : 'false');
      });
      console.log('Sidebar toggle initialized');
    }
  }

  private initDropdowns() {
    const dropdownElements = [].slice.call(document.querySelectorAll('.dropdown-toggle'));
    dropdownElements.forEach((dropdownToggleEl: any) => {
      if (!dropdownToggleEl.hasAttribute('data-bs-initialized')) {
        dropdownToggleEl.setAttribute('data-bs-initialized', 'true');
        new bootstrap.Dropdown(dropdownToggleEl);
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
