import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CalendarService, CalendarDto, CalendarRequestDto } from '../services/calendar.service';

@Component({
  selector: 'app-admin-calendar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-calendar.html',
  styleUrls: ['./admin-calendar.scss']
})
export class AdminCalendar implements OnInit {
  calendars: CalendarDto[] = [];
  selectedCalendar: CalendarDto | null = null;
  isEditing = false;
  isCreating = false;
  showModal = false;
  loading = false;
  errorMessage = '';
  successMessage = '';

  // Form data
  formData: CalendarRequestDto = {
    title: '',
    description: '',
    startTime: '',
    endTime: '',
    location: '',
    maxCapacity: 0
  };

  // Filter options
  statusFilter = '';
  locationFilter = '';
  dateFilter = '';

  constructor(private calendarService: CalendarService) {}

  ngOnInit(): void {
    this.loadCalendars();
  }

  loadCalendars(): void {
    this.loading = true;
    this.calendarService.getAllCalendars().subscribe({
      next: (calendars) => {
        this.calendars = calendars;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Error loading calendars: ' + error.message;
        this.loading = false;
      }
    });
  }

  openCreateModal(): void {
    this.isCreating = true;
    this.isEditing = false;
    this.selectedCalendar = null;
    this.resetForm();
    this.showModal = true;
  }

  openEditModal(calendar: CalendarDto): void {
    this.isEditing = true;
    this.isCreating = false;
    this.selectedCalendar = calendar;
    this.formData = {
      title: calendar.title,
      description: calendar.description,
      startTime: this.formatDateTimeForInput(calendar.startTime),
      endTime: this.formatDateTimeForInput(calendar.endTime),
      location: calendar.location,
      maxCapacity: calendar.maxCapacity,
      status: calendar.status
    };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.isCreating = false;
    this.isEditing = false;
    this.selectedCalendar = null;
    this.resetForm();
    this.errorMessage = '';
    this.successMessage = '';
  }

  resetForm(): void {
    this.formData = {
      title: '',
      description: '',
      startTime: '',
      endTime: '',
      location: '',
      maxCapacity: 0
    };
  }

  onSubmit(): void {
    if (!this.validateForm()) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.isCreating) {
      this.calendarService.createCalendar(this.formData).subscribe({
        next: (createdCalendar) => {
          this.calendars.push(createdCalendar);
          this.successMessage = 'Calendar event created successfully!';
          this.loading = false;
          setTimeout(() => this.closeModal(), 1500);
        },
        error: (error) => {
          this.errorMessage = 'Error creating calendar: ' + error.message;
          this.loading = false;
        }
      });
    } else if (this.isEditing && this.selectedCalendar) {
      this.calendarService.updateCalendar(this.selectedCalendar.id, this.formData).subscribe({
        next: (updatedCalendar) => {
          const index = this.calendars.findIndex(c => c.id === updatedCalendar.id);
          if (index !== -1) {
            this.calendars[index] = updatedCalendar;
          }
          this.successMessage = 'Calendar event updated successfully!';
          this.loading = false;
          setTimeout(() => this.closeModal(), 1500);
        },
        error: (error) => {
          this.errorMessage = 'Error updating calendar: ' + error.message;
          this.loading = false;
        }
      });
    }
  }

  deleteCalendar(id: number): void {
    if (confirm('Are you sure you want to delete this calendar event?')) {
      this.loading = true;
      this.calendarService.deleteCalendar(id).subscribe({
        next: () => {
          this.calendars = this.calendars.filter(c => c.id !== id);
          this.successMessage = 'Calendar event deleted successfully!';
          this.loading = false;
          setTimeout(() => this.successMessage = '', 3000);
        },
        error: (error) => {
          this.errorMessage = 'Error deleting calendar: ' + error.message;
          this.loading = false;
        }
      });
    }
  }

  validateForm(): boolean {
    if (!this.formData.title.trim()) {
      this.errorMessage = 'Title is required';
      return false;
    }
    if (!this.formData.startTime) {
      this.errorMessage = 'Start time is required';
      return false;
    }
    if (!this.formData.endTime) {
      this.errorMessage = 'End time is required';
      return false;
    }
    if (!this.formData.location.trim()) {
      this.errorMessage = 'Location is required';
      return false;
    }
    if (this.formData.maxCapacity <= 0) {
      this.errorMessage = 'Maximum capacity must be greater than 0';
      return false;
    }
    if (new Date(this.formData.startTime) >= new Date(this.formData.endTime)) {
      this.errorMessage = 'End time must be after start time';
      return false;
    }
    return true;
  }

  formatDateTimeForInput(dateTimeString: string): string {
    const date = new Date(dateTimeString);
    return date.toISOString().slice(0, 16); // Format: YYYY-MM-DDTHH:MM
  }

  formatDateTime(dateTimeString: string): string {
    const date = new Date(dateTimeString);
    return date.toLocaleString();
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

  getFilteredCalendars(): CalendarDto[] {
    let filtered = this.calendars;

    if (this.statusFilter) {
      filtered = filtered.filter(c => c.status === this.statusFilter);
    }

    if (this.locationFilter) {
      filtered = filtered.filter(c => 
        c.location.toLowerCase().includes(this.locationFilter.toLowerCase())
      );
    }

    if (this.dateFilter) {
      const filterDate = new Date(this.dateFilter);
      filtered = filtered.filter(c => {
        const eventDate = new Date(c.startTime);
        return eventDate.toDateString() === filterDate.toDateString();
      });
    }

    return filtered;
  }

  clearFilters(): void {
    this.statusFilter = '';
    this.locationFilter = '';
    this.dateFilter = '';
  }
} 