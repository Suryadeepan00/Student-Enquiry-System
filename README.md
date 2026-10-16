# Student Enquiry and Demo Booking System

A Spring Boot web application for Students that lets students submit course enquiries and book chemistry demo sessions. Counsellors can view enquiries and bookings, filter them by date and status, and update booking statuses.

The application uses an embedded H2 database by default, so it can be run locally without installing a database server. An optional MySQL schema and profile are also included.

## Features

### Student webpage

- Submit a student enquiry with name, mobile number, optional email, class, course, and enquiry source.
- Select a class from `11` or `12`.
- Select a course from the supported course list.
- Book a demo for a selected future date and available slot.
- Prevent duplicate bookings for the same student, date, and slot.
- Show clear validation, confirmation, and error messages.
- Display the student’s existing enquiries and demo bookings.

### Counsellor dashboard

- Show summary counts:
    - Total enquiries.
    - Total demo bookings.
    - Currently booked demos.
    - Demos attended.
    - Demos cancelled.
- View all enquiries.
- Filter enquiries by enquiry date.
- View all demo bookings.
- Filter bookings by demo date and booking status.
- Update a booking status to `BOOKED`, `ATTENDED`, or `CANCELLED`.
- Refresh all dashboard data.

## Technology stack

- Java
- Spring Boot
- Spring Data JPA
- Spring Validation
- H2 in-memory database
- HTML, CSS, and JavaScript
- JUnit 5 and MockMvc

## Project structure

```text
demo-booking/
├── database/
│   └── schema-mysql.sql
├── src/
│   ├── main/
│   │   ├── java/com/kmie/demo_booking/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── DemoBookingApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html
│   │       │   └── counsellor.html
│   │       ├── application.properties
│   │       └── application-mysql.properties
│   └── test/
│       └── java/com/kmie/demo_booking/
│           └── KmieWorkflowTest.java
├── .gitignore
└── pom.xml
```

## Requirements

- Java 21 or a compatible Java version.
- Maven Wrapper is included, so Maven does not need to be installed separately.
- No external database installation is required for the default H2 configuration.

## Run the application

From the project root:

```powershell
cd "E:\kmie-demo-booking"
.\mvnw.cmd spring-boot:run
```

When the application starts, open:

```text
http://localhost:8080/
```

- Student enquiry and demo booking page: `http://localhost:8080/`
- Counsellor dashboard: `http://localhost:8080/counsellor.html`
- H2 database console: `http://localhost:8080/h2-console`

The H2 console uses the JDBC URL, username, and password configured in `application.properties`.

## API endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/enquiries` | Create a student enquiry |
| `GET` | `/api/enquiries` | List enquiries; optional `date` query parameter |
| `POST` | `/api/bookings` | Book a demo |
| `GET` | `/api/bookings` | List bookings; optional `date` and `status` parameters |
| `PATCH` | `/api/bookings/{bookingId}/status` | Update a booking status |
| `GET` | `/api/dashboard` | Return dashboard summary counts |

## Example enquiry request

```http
POST /api/enquiries
Content-Type: application/json

{
  "name": "Aarav Sharma",
  "mobile": "9876543210",
  "email": "aarav@example.com",
  "studentClass": "12",
  "course": "NEET Chemistry",
  "enquirySource": "Website",
  "enquiryDateTime": "2026-10-14T10:00:00"
}
```

## Example booking request

```http
POST /api/bookings
Content-Type: application/json

{
  "studentId": 1,
  "demoDate": "2026-10-15",
  "demoSlot": "SLOT_3_PM"
}
```

## Example status update

```http
PATCH /api/bookings/1/status
Content-Type: application/json

{
  "status": "ATTENDED"
}
```

## Business rules

- A student is identified by a 10-digit mobile number.
- If a mobile number already exists, the existing student record is reused and a new enquiry is created.
- Supported classes are `11` and `12`.
- A demo date must be today or a future date.
- Supported slots are:
    - `SLOT_10_AM`: 10 AM
    - `SLOT_11_30_AM`: 11:30 AM
    - `SLOT_3_PM`: 3 PM
    - `SLOT_4_30_PM`: 4:30 PM
- A student cannot have more than one booking for the same date and slot.
- Booking status can be `BOOKED`, `ATTENDED`, or `CANCELLED`.
- Summary counts cover all records and are independent of dashboard filters.

## Automated tests

The integration tests use a separate in-memory H2 database and test the controller, service, validation, repository, and business-rule behaviour together.

Run all tests:

```powershell
.\mvnw.cmd test
```

Run only the workflow test class:

```powershell
.\mvnw.cmd "-Dtest=KmieWorkflowTest" test
```

The tests cover:

- Valid enquiry creation.
- Duplicate mobile number handling.
- Invalid mobile and class validation.
- Valid demo booking.
- Duplicate booking prevention.
- Missing student handling.
- Past-date prevention.
- Invalid slot handling.
- Booking status updates.
- Booking date and status filters.
- Dashboard summary counts.

## Manual test checklist

| Test | Steps | Expected result |
|---|---|---|
| Submit valid enquiry | Open `/`, complete the enquiry form, submit | Success message and enquiry appears in Recent Enquiries |
| Submit duplicate enquiry | Submit another enquiry with the same mobile number | Existing student is reused; enquiry is added |
| Submit invalid mobile | Enter fewer than 10 digits | Inline mobile validation appears; no record is saved |
| Book valid demo | Select student, future date, and slot | Booking confirmation appears |
| Book duplicate demo | Repeat the same student, date, and slot | Duplicate booking message appears |
| Filter enquiries | Enter an enquiry date on the dashboard | Only matching enquiries appear |
| Filter bookings | Enter date and status on the dashboard | Only matching bookings appear |
| Update status | Change a booking status and save | Status and summary counts update |
| Refresh dashboard | Click Refresh All | Tables and summary counts reload |

## Optional MySQL configuration

The application defaults to H2. To use MySQL:

1. Run `database/schema-mysql.sql` in MySQL.
2. Set database credentials as environment variables:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "YOUR_ACTUAL_MYSQL_PASSWORD"
```

3. Start the application with the `mysql` profile:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=mysql"
```

The MySQL configuration is optional and was not required for the core assignment demonstration.

## Known limitations

- The application does not include counsellor login or role-based authorization.
- The counsellor dashboard is intended for local assignment demonstration, not public deployment with real student data.
- The MySQL profile is provided as an optional configuration and should be tested separately before being relied on.
- Automated tests cover API and business-rule workflows; browser UI interactions are verified manually.