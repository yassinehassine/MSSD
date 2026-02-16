# Calendar System Documentation

## Overview

The calendar system provides both admin and visitor interfaces for managing and viewing calendar events. The system includes:

- **Admin Backoffice**: Full CRUD operations for calendar events
- **Visitor Frontoffice**: Beautiful calendar interface for viewing events
- **RESTful API**: Complete backend API for calendar management

## Features

### Admin Backoffice Features
- ✅ Create, Read, Update, Delete calendar events
- ✅ Filter events by status, location, and date
- ✅ Real-time capacity tracking with progress bars
- ✅ Status management (Available, Full, Cancelled, Completed)
- ✅ Responsive design with modern UI
- ✅ Form validation and error handling

### Visitor Frontoffice Features
- ✅ Beautiful month view calendar
- ✅ List view for detailed event information
- ✅ Search and filter functionality
- ✅ Event capacity indicators
- ✅ Responsive design with smooth animations
- ✅ Real-time status updates

### Backend API Features
- ✅ RESTful endpoints for all CRUD operations
- ✅ Date range filtering
- ✅ Location-based filtering
- ✅ Available events filtering
- ✅ Automatic capacity calculation
- ✅ Data validation and error handling

## API Endpoints

### Calendar Management
```
GET    /api/calendars              - Get all calendar events
GET    /api/calendars/{id}         - Get calendar event by ID
GET    /api/calendars/available    - Get available calendar events
GET    /api/calendars/range        - Get events by date range
GET    /api/calendars/location/{location} - Get events by location
POST   /api/calendars              - Create new calendar event
PUT    /api/calendars/{id}         - Update calendar event
DELETE /api/calendars/{id}         - Delete calendar event
```

## Data Model

### Calendar Entity
```java
public class Calendar {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private int maxCapacity;
    private int currentCapacity;
    private CalendarStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

public enum CalendarStatus {
    AVAILABLE, FULL, CANCELLED, COMPLETED
}
```

## Frontend Components

### Admin Calendar Component
- **File**: `mssd-frontend/src/app/admin/admin-calendar.ts`
- **Template**: `mssd-frontend/src/app/admin/admin-calendar.html`
- **Styles**: `mssd-frontend/src/app/admin/admin-calendar.scss`

### Visitor Calendar Component
- **File**: `mssd-frontend/src/app/pages/calendar/calendar.ts`
- **Template**: `mssd-frontend/src/app/pages/calendar/calendar.html`
- **Styles**: `mssd-frontend/src/app/pages/calendar/calendar.scss`

### Calendar Service
- **File**: `mssd-frontend/src/app/services/calendar.service.ts`

## Routes

### Admin Routes
```
/admin/calendar - Calendar management interface
```

### Visitor Routes
```
/calendar - Public calendar view
```

## Navigation

### Admin Navigation
The calendar management link has been added to the admin sidebar:
- Icon: `fas fa-calendar-alt`
- Text: "Calendar Management"
- Route: `/admin/calendar`

### Visitor Navigation
The calendar link has been added to the main navigation:
- Text: "Calendar"
- Route: `/calendar`

## Sample Data

The system includes sample calendar events that are automatically seeded:
- Leadership Workshop (7 days from now)
- Sales Training Session (14 days from now)
- Team Building Event (21 days from now)
- Professional Development Seminar (30 days from now)

## Usage Instructions

### For Administrators

1. **Access Calendar Management**:
   - Navigate to `/admin/calendar`
   - Or click "Calendar Management" in the admin sidebar

2. **Create New Event**:
   - Click "Add New Event" button
   - Fill in all required fields (title, start/end time, location, capacity)
   - Click "Create" to save

3. **Edit Existing Event**:
   - Click the edit icon (pencil) next to any event
   - Modify the fields as needed
   - Click "Update" to save changes

4. **Delete Event**:
   - Click the delete icon (trash) next to any event
   - Confirm the deletion

5. **Filter Events**:
   - Use the filter panel to search by status, location, or date
   - Click "Clear Filters" to reset

### For Visitors

1. **Access Calendar**:
   - Navigate to `/calendar`
   - Or click "Calendar" in the main navigation

2. **View Events**:
   - **Month View**: See events in a traditional calendar layout
   - **List View**: See detailed event information in a list format

3. **Filter Events**:
   - Use the search box to find events by title or description
   - Filter by location or status
   - Click "Clear" to reset filters

4. **View Event Details**:
   - Click on any date with events to see details
   - In list view, all event details are visible directly

## Technical Implementation

### Backend Implementation
- **Controller**: `CalendarController.java`
- **Service**: `CalendarService.java` and `CalendarServiceImpl.java`
- **Repository**: `CalendarRepository.java`
- **DTOs**: `CalendarDto.java` and `CalendarRequestDto.java`
- **Model**: `Calendar.java`

### Frontend Implementation
- **Service**: Angular service for API communication
- **Components**: Standalone Angular components
- **Routing**: Integrated with existing routing system
- **Styling**: Modern SCSS with responsive design

## Features in Detail

### Admin Features
1. **Event Management**:
   - Create events with title, description, time, location, and capacity
   - Edit existing events
   - Delete events with confirmation
   - View all events in a table format

2. **Filtering and Search**:
   - Filter by status (Available, Full, Cancelled, Completed)
   - Filter by location
   - Filter by date
   - Real-time filtering

3. **Capacity Management**:
   - Visual progress bars showing capacity usage
   - Available spots calculation
   - Status updates based on capacity

4. **Responsive Design**:
   - Works on desktop, tablet, and mobile
   - Modern UI with Bootstrap components
   - Loading states and error handling

### Visitor Features
1. **Calendar Views**:
   - **Month View**: Traditional calendar grid with event indicators
   - **List View**: Detailed event cards with all information

2. **Interactive Elements**:
   - Click on dates to see events
   - Navigate between months
   - Switch between view modes
   - Search and filter functionality

3. **Event Information**:
   - Event title and description
   - Date and time information
   - Location details
   - Capacity and availability status
   - Join event buttons (disabled when full)

4. **Visual Design**:
   - Beautiful gradient backgrounds
   - Smooth animations and transitions
   - Modern card-based layout
   - Color-coded status indicators

## Security and Validation

### Backend Validation
- Required field validation
- Date validation (future dates only)
- Capacity validation (positive numbers)
- Time validation (end time after start time)

### Frontend Validation
- Form validation before submission
- Real-time validation feedback
- Error message display
- Loading states during operations

## Future Enhancements

Potential improvements for the calendar system:

1. **Event Registration**:
   - Allow visitors to register for events
   - Email notifications for registrations
   - Waitlist functionality

2. **Advanced Features**:
   - Recurring events
   - Event categories/tags
   - Calendar export (iCal format)
   - Email reminders

3. **Integration**:
   - Google Calendar integration
   - Outlook calendar sync
   - Mobile app support

4. **Analytics**:
   - Event popularity tracking
   - Attendance statistics
   - Capacity utilization reports

## Troubleshooting

### Common Issues

1. **Events not loading**:
   - Check backend server is running
   - Verify API endpoints are accessible
   - Check browser console for errors

2. **Date formatting issues**:
   - Ensure proper date format in forms
   - Check timezone settings
   - Verify date validation rules

3. **Styling issues**:
   - Clear browser cache
   - Check SCSS compilation
   - Verify Bootstrap CSS is loaded

### Development Notes

- The calendar system uses Angular standalone components
- All styles are scoped to prevent conflicts
- The backend uses Spring Boot with JPA/Hibernate
- Database seeding provides sample data for testing

## Conclusion

The calendar system provides a complete solution for both administrators and visitors to manage and view calendar events. With its modern design, responsive layout, and comprehensive functionality, it serves as a robust foundation for event management within the MSSD application. 