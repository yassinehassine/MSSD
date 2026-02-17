import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarService, CalendarDto } from '../../services/calendar.service';
import { FormsModule } from '@angular/forms';
import { CalendarReservationService, CalendarReservation } from '../../services/calendar-reservation.service';
import { PaginationComponent } from '../../shared/pagination/pagination.component';

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent],
  templateUrl: './calendar.html',
  styleUrls: ['./calendar.scss']
})
export class Calendar implements OnInit {
  calendars: CalendarDto[] = [];
  filteredCalendars: CalendarDto[] = [];
  loading = false;
  errorMessage = '';

  // Calendar view state
  currentMonth = new Date();
  selectedDate: Date | null = null;
  viewMode: 'month' | 'list' = 'month';

  // Filter options
  locationFilter = '';
  statusFilter = '';
  searchTerm = '';

  // Pagination properties
  currentPage = 1;
  itemsPerPage = 6; // 2 columns x 3 rows
  paginatedCalendars: CalendarDto[] = [];

  // Reservation modal state
  showReservationModal = false;
  reservationEvent: CalendarDto | null = null;
  reservationForm = { calendarId: 0, visitorName: '', visitorEmail: '', visitorPhone: '', numberOfPeople: 1 };
  reservationLoading = false;
  reservationSuccess = '';
  reservationError = '';

  constructor(
    private calendarService: CalendarService,
    private calendarReservationService: CalendarReservationService
  ) {}

  ngOnInit(): void {
    this.loadCalendars();
  }

  loadCalendars(): void {
    this.loading = true;
    this.calendarService.getAllCalendars().subscribe({
      next: (calendars) => {
        this.calendars = calendars;
        this.applyFilters();
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Error loading calendar events: ' + error.message;
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    let filtered = this.calendars;
    if (this.searchTerm) {
      filtered = filtered.filter(calendar =>
        calendar.title.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        calendar.description.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
    if (this.locationFilter) {
      filtered = filtered.filter(calendar =>
        calendar.location.toLowerCase().includes(this.locationFilter.toLowerCase())
      );
    }
    if (this.statusFilter) {
      filtered = filtered.filter(calendar => calendar.status === this.statusFilter);
    }
    this.filteredCalendars = filtered;
    this.currentPage = 1; // Reset to first page when filtering
    this.updatePaginatedCalendars();
  }

  updatePaginatedCalendars(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedCalendars = this.filteredCalendars.slice(startIndex, endIndex);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.updatePaginatedCalendars();
    // Smooth scroll to top of calendar section
    document.querySelector('.calendar-container')?.scrollIntoView({ behavior: 'smooth' });
  }

  previousMonth(): void {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, 1);
  }

  nextMonth(): void {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, 1);
  }

  goToToday(): void {
    this.currentMonth = new Date();
    this.selectedDate = new Date();
  }

  getCalendarDays(): Date[] {
    const year = this.currentMonth.getFullYear();
    const month = this.currentMonth.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const startDate = new Date(firstDay);
    startDate.setDate(startDate.getDate() - firstDay.getDay());
    const days: Date[] = [];
    const currentDate = new Date(startDate);
    while (currentDate <= lastDay || currentDate.getDay() !== 0) {
      days.push(new Date(currentDate));
      currentDate.setDate(currentDate.getDate() + 1);
    }
    return days;
  }

  getEventsForDate(date: Date): CalendarDto[] {
    if (!date) return [];
    return this.filteredCalendars.filter(calendar => {
      const eventDate = new Date(calendar.startTime);
      return eventDate.toDateString() === date.toDateString();
    });
  }

  hasEvents(date: Date): boolean {
    if (!date) return false;
    return this.getEventsForDate(date).length > 0;
  }

  isToday(date: Date): boolean {
    if (!date) return false;
    return date.toDateString() === new Date().toDateString();
  }

  isCurrentMonth(date: Date): boolean {
    if (!date) return false;
    return date.getMonth() === this.currentMonth.getMonth();
  }

  isSelected(date: Date): boolean {
    return this.selectedDate !== null && date.toDateString() === this.selectedDate.toDateString();
  }

  selectDate(date: Date): void {
    this.selectedDate = new Date(date);
  }

  formatDate(date: Date): string {
    if (!date) return '';
    return date.getDate().toString();
  }

  formatTime(dateTimeString: string): string {
    const date = new Date(dateTimeString);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }

  formatDateForDisplay(dateTimeString: string): string {
    const date = new Date(dateTimeString);
    return date.toLocaleDateString();
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'AVAILABLE': return 'badge bg-success';
      case 'FULL': return 'badge bg-warning';
      case 'CANCELLED': return 'badge bg-danger';
      case 'COMPLETED': return 'badge bg-info';
      default: return 'badge bg-secondary';
    }
  }

  getCapacityPercentage(calendar: CalendarDto): number {
    return (calendar.currentCapacity / calendar.maxCapacity) * 100;
  }

  getCapacityColor(calendar: CalendarDto): string {
    const percentage = this.getCapacityPercentage(calendar);
    if (percentage >= 90) return 'text-danger';
    if (percentage >= 75) return 'text-warning';
    return 'text-success';
  }

  toggleViewMode(): void {
    this.viewMode = this.viewMode === 'month' ? 'list' : 'month';
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.locationFilter = '';
    this.statusFilter = '';
    this.applyFilters();
  }

  getMonthName(): string {
    return this.currentMonth.toLocaleDateString('en-US', { month: 'long', year: 'numeric' });
  }

  getDayNames(): string[] {
    return ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  }

  joinEvent(event: CalendarDto) {
    if (event.status !== 'AVAILABLE' || event.availableSpots === 0) return;
    this.reservationEvent = event;
    this.reservationForm = { calendarId: event.id, visitorName: '', visitorEmail: '', visitorPhone: '', numberOfPeople: 1 };
    this.reservationSuccess = '';
    this.reservationError = '';
    this.showReservationModal = true;
  }

  submitReservation() {
    if (!this.reservationForm.visitorName.trim() || !this.reservationForm.visitorEmail.trim()) {
      this.reservationError = 'Name and email are required.';
      return;
    }
    this.reservationLoading = true;
    this.reservationError = '';
    this.reservationSuccess = '';
    
    // Create calendar reservation
    const calendarReservation: CalendarReservation = {
      calendarId: this.reservationEvent?.id,
      clientName: this.reservationForm.visitorName,
      clientEmail: this.reservationForm.visitorEmail,
      clientPhone: this.reservationForm.visitorPhone || '',
      eventTitle: this.reservationEvent?.title || 'Événement',
      eventDescription: this.reservationEvent?.description || '',
      eventDate: this.reservationEvent?.startTime || new Date().toISOString(),
      location: this.reservationEvent?.location || '',
      status: 'PENDING'
    };

    this.calendarReservationService.createReservation(calendarReservation).subscribe({
      next: (request: any) => {
        this.reservationSuccess = 'Réservation soumise avec succès!';
        this.reservationLoading = false;
        setTimeout(() => this.closeReservationModal(), 1200);
      },
      error: (err: any) => {
        this.reservationError = err.error?.message || 'Échec de la réservation.';
        this.reservationLoading = false;
      }
    });
  }

  closeReservationModal() {
    this.showReservationModal = false;
    this.reservationEvent = null;
    this.reservationForm = { calendarId: 0, visitorName: '', visitorEmail: '', visitorPhone: '', numberOfPeople: 1 };
    this.reservationSuccess = '';
    this.reservationError = '';
    this.reservationLoading = false;
  }
}
