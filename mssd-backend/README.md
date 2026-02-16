# MSSD Backend - Calendar & Reservation System

A complete Spring Boot backend application for managing calendar events and visitor reservations.

## 🏗️ Project Structure

```
mssd-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mssd/
│   │   │       ├── MssdApplication.java          # Main Spring Boot application
│   │   │       ├── config/                       # Configuration classes
│   │   │       ├── controller/                   # REST API controllers
│   │   │       ├── dto/                          # Data Transfer Objects
│   │   │       ├── exception/                    # Exception handling
│   │   │       ├── mapper/                       # Entity-DTO mappers
│   │   │       ├── model/                        # JPA entities
│   │   │       ├── repository/                   # Data access layer
│   │   │       └── service/                      # Business logic layer
│   │   │           └── impl/                     # Service implementations
│   │   └── resources/
│   │       ├── application.properties            # Application configuration
│   │       └── static/                           # Static web resources
│   │           ├── admin-calendar.html           # Admin interface
│   │           └── visitor-reservation.html      # Visitor reservation form
│   └── test/
│       └── java/
│           └── com/mssd/
│               ├── MssdApplicationTests.java     # Basic application test
│               └── controller/                   # Controller tests
├── pom.xml                                       # Maven configuration
└── README.md                                     # This file
```

## 🚀 Features

### Calendar Management (Admin)
- ✅ Create, Read, Update, Delete calendar events
- ✅ Set event capacity and location
- ✅ Manage event status (Available, Cancelled, Full)
- ✅ View all events in a grid layout
- ✅ Real-time capacity tracking

### Reservation System (Visitors)
- ✅ Browse available events
- ✅ Make reservations with visitor information
- ✅ Real-time availability checking
- ✅ Form validation and error handling
- ✅ Reservation status management

### API Endpoints

#### Calendar Management
- `GET /api/calendars` - Get all events
- `GET /api/calendars/{id}` - Get specific event
- `GET /api/calendars/available` - Get available events only
- `GET /api/calendars/range?start=X&end=Y` - Get events by date range
- `GET /api/calendars/location/{location}` - Get events by location
- `POST /api/calendars` - Create new event
- `PUT /api/calendars/{id}` - Update event
- `DELETE /api/calendars/{id}` - Delete event

#### Reservation Management
- `GET /api/reservations` - Get all reservations
- `GET /api/reservations/{id}` - Get specific reservation
- `GET /api/reservations/calendar/{calendarId}` - Get reservations for an event
- `GET /api/reservations/visitor/{email}` - Get visitor's reservations
- `GET /api/reservations/status/{status}` - Get reservations by status
- `POST /api/reservations` - Create new reservation
- `PUT /api/reservations/{id}` - Update reservation
- `PUT /api/reservations/{id}/confirm` - Confirm reservation
- `PUT /api/reservations/{id}/cancel` - Cancel reservation
- `DELETE /api/reservations/{id}` - Delete reservation
- `GET /api/reservations/check?calendarId=X&numberOfPeople=Y` - Check availability

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Database**: H2 (in-memory for development)
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Java Version**: 17
- **Frontend**: HTML5, CSS3, JavaScript (Vanilla)
- **Testing**: JUnit 5, MockMvc

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- IntelliJ IDEA (recommended) or any Java IDE

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd mssd-backend
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Access the Interfaces

#### Admin Interface
- URL: `http://localhost:8080/admin-calendar.html`
- Purpose: Manage calendar events (Create, Edit, Delete)

#### Visitor Interface
- URL: `http://localhost:8080/visitor-reservation.html`
- Purpose: Make reservations for events

#### H2 Database Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Classes
```bash
mvn test -Dtest=CalendarControllerTest
mvn test -Dtest=ReservationControllerTest
```

### Test Coverage
The project includes comprehensive tests for:
- ✅ Controller layer (REST endpoints)
- ✅ Service layer (business logic)
- ✅ Repository layer (data access)
- ✅ Integration tests

## 📊 Database Schema

### Calendar Entity
- `id` - Primary key
- `title` - Event title
- `description` - Event description
- `startTime` - Event start time
- `endTime` - Event end time
- `location` - Event location
- `maxCapacity` - Maximum number of participants
- `currentCapacity` - Current number of participants
- `status` - Event status (AVAILABLE, FULL, CANCELLED, COMPLETED)

### Reservation Entity
- `id` - Primary key
- `calendar_id` - Foreign key to Calendar
- `visitorName` - Visitor's full name
- `visitorEmail` - Visitor's email address
- `visitorPhone` - Visitor's phone number
- `numberOfPeople` - Number of people in reservation
- `notes` - Additional notes
- `status` - Reservation status (PENDING, CONFIRMED, CANCELLED, COMPLETED)
- `reservationDate` - Date of reservation

## 🔧 Configuration

### Application Properties
Key configuration options in `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## 🎯 Usage Examples

### Creating an Event (Admin)
1. Open `http://localhost:8080/admin-calendar.html`
2. Click "Add New Event"
3. Fill in event details (title, description, date/time, location, capacity)
4. Click "Save Event"

### Making a Reservation (Visitor)
1. Open `http://localhost:8080/visitor-reservation.html`
2. Select an available event from the dropdown
3. Fill in visitor information (name, email, phone)
4. Select number of people
5. Add any additional notes
6. Click "Make Reservation"

## 🐛 Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   - Change the port in `application.properties`: `server.port=8081`

2. **Maven not found**
   - Install Maven: https://maven.apache.org/download.cgi
   - Add Maven to your system PATH

3. **Java version issues**
   - Ensure Java 17+ is installed: `java --version`
   - Set JAVA_HOME environment variable

4. **Database connection issues**
   - Check H2 console at `http://localhost:8080/h2-console`
   - Verify database credentials in `application.properties`

## 📝 API Documentation

### Request/Response Examples

#### Create Calendar Event
```bash
POST /api/calendars
Content-Type: application/json

{
  "title": "Tech Conference 2024",
  "description": "Annual technology conference",
  "startTime": "2024-12-15T09:00:00",
  "endTime": "2024-12-15T17:00:00",
  "location": "Convention Center",
  "maxCapacity": 100,
  "status": "AVAILABLE"
}
```

#### Create Reservation
```bash
POST /api/reservations
Content-Type: application/json

{
  "calendarId": 1,
  "visitorName": "John Doe",
  "visitorEmail": "john@example.com",
  "visitorPhone": "1234567890",
  "numberOfPeople": 2,
  "notes": "Vegetarian meal preference"
}
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -am 'Add feature'`
4. Push to the branch: `git push origin feature-name`
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team

---

**Happy Coding! 🚀** 