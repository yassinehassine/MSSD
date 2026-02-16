import { Component, OnInit, HostListener } from '@angular/core';
import { Router, NavigationEnd, RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { User } from '../model/user.model';

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
  styleUrl: './admin-layout.scss'
})
export class AdminLayout implements OnInit {
  currentUser: User | null = null;
  sidebarCollapsed = false;
  sidebarMobileOpen = false;
  showUserMenu = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  ngOnInit(): void {
    // Close mobile sidebar on navigation
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.sidebarMobileOpen = false;
      }
    });

    // Restore sidebar state
    const saved = localStorage.getItem('admin-sidebar-collapsed');
    if (saved === 'true' && window.innerWidth > 991) {
      this.sidebarCollapsed = true;
    }
  }

  toggleSidebar(): void {
    if (window.innerWidth <= 991) {
      this.sidebarMobileOpen = !this.sidebarMobileOpen;
    } else {
      this.sidebarCollapsed = !this.sidebarCollapsed;
      localStorage.setItem('admin-sidebar-collapsed', String(this.sidebarCollapsed));
    }
  }

  closeMobileSidebar(): void {
    this.sidebarMobileOpen = false;
  }

  toggleUserMenu(): void {
    this.showUserMenu = !this.showUserMenu;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.topbar-user') && !target.closest('.user-dropdown')) {
      this.showUserMenu = false;
    }
  }

  getUserInitials(): string {
    if (!this.currentUser?.username) return 'A';
    return this.currentUser.username.substring(0, 2).toUpperCase();
  }

  logout(): void {
    this.authService.logout();
  }
}
