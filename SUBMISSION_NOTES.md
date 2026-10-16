# KMIE Demo Booking — Submission and Integration Notes

## Submission overview

This project implements a student enquiry and chemistry demo booking feature for KMIE.

It provides:

- A student-facing webpage for enquiries and demo bookings.
- A counsellor dashboard for viewing, filtering, and updating records.
- REST APIs for enquiries, bookings, status updates, and dashboard summaries.
- Input validation and duplicate-booking prevention.
- An embedded H2 database for local demonstration.
- Automated integration tests.
- An optional MySQL schema and profile.

## Source files to include

Include the complete project folder, excluding build output and local database files.

Required project contents:

```text
pom.xml
README.md
SUBMISSION_NOTES.md
.gitignore
mvnw
mvnw.cmd
.mvn/
database/
src/
```

The `.gitignore` excludes:

- `target/` build output.
- Local H2 database files.
- Local credential files.
- IDE-specific files.

## How to run

From the project root:

```powershell
cd "E:\kmie-demo-booking"
.\mvnw.cmd spring-boot:run
```

Open:

```text
http://localhost:8080/
```

Counsellor dashboard:

```text
http://localhost:8080/counsellor.html
```

## How to run tests

Run all tests:

```powershell
.\mvnw.cmd test
```

Run the main workflow tests:

```powershell
.\mvnw.cmd "-Dtest=KmieWorkflowTest" test
```

The tests use a separate H2 database and do not affect saved demonstration data.

## Demonstration script

Use this sequence during the demonstration:

1. Open the student page at `http://localhost:8080/`.
2. Submit a new enquiry using:
    - Name.
    - A 10-digit mobile number.
    - Class `11` or `12`.
    - A supported course.
    - Enquiry source.
3. Show the confirmation message and the enquiry in **Recent Enquiries**.
4. Submit a second enquiry using the same mobile number.
5. Explain that the existing student record is reused and a new enquiry is added.
6. Book a demo using the same student, a future date, and one of the available slots.
7. Show the booking confirmation and the booking in **My Demo Bookings**.
8. Try booking the same student, date, and slot again.
9. Show the duplicate-booking message.
10. Open `http://localhost:8080/counsellor.html`.
11. Show the summary counts.
12. Filter enquiries by the enquiry date.
13. Filter bookings by date and status.
14. Change a booking status from `BOOKED` to `ATTENDED`.
15. Show that the status and summary counts update.
16. Run the automated tests and show the successful Maven result.

## Demonstration data

Use fictional data only.

Example student:

- Name: `Aarav Sharma`
- Mobile: `9876543210`
- Class: `12`
- Course: `NEET Chemistry`
- Source: `Website`

Use a future date for the demo booking.

## Integration with the KMIE website

The feature can be integrated into the existing KMIE website in one of two ways.

### Option 1: Link from the existing website

Add a navigation link or button on the KMIE website:

```html
<a href="/demo-booking">Book a Chemistry Demo</a>
```

The Spring Boot application can be deployed under the `/demo-booking` context or behind a reverse proxy.

This is the simplest integration approach because it does not require changing the existing KMIE website code.

### Option 2: Embed the student form

The existing KMIE website can embed the student enquiry and booking form using an iframe:

```html
<iframe
    src="[https://YOUR_KMIE_DEMO_BOOKING_DOMAIN/](https://YOUR_KMIE_DEMO_BOOKING_DOMAIN/)"
    title="KMIE Chemistry Demo Booking"
    style="width: 100%; min-height: 900px; border: 0;"
></iframe>
```

This keeps the booking feature visually inside the KMIE website.

### Option 3: Use the REST APIs

If KMIE has its own frontend, it can call these endpoints:

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/enquiries` | Create an enquiry |
| `GET` | `/api/enquiries` | Retrieve enquiries |
| `POST` | `/api/bookings` | Book a demo |
| `GET` | `/api/bookings` | Retrieve bookings |
| `PATCH` | `/api/bookings/{id}/status` | Update booking status |
| `GET` | `/api/dashboard` | Retrieve summary counts |

For a production integration, KMIE should add authentication and authorization before exposing counsellor endpoints.

## Production considerations

Before using real student data, the following should be added:

- Counsellor authentication.
- Role-based access control.
- HTTPS.
- Rate limiting and spam protection.
- Persistent database backups.
- Audit logging for booking status changes.
- Privacy notice and consent handling for student data.
- Monitoring and alerting.

The current implementation is suitable for a local assignment demonstration, not for public deployment with real personal data.

## Completion checklist

- [x] Student enquiry form.
- [x] Student demo booking form.
- [x] Existing student lookup by mobile number.
- [x] Duplicate booking prevention.
- [x] Student enquiry and booking history.
- [x] Counsellor dashboard.
- [x] Enquiry date filter.
- [x] Booking date and status filters.
- [x] Booking status updates.
- [x] Dashboard summary counts.
- [x] REST APIs.
- [x] Validation and error handling.
- [x] Automated integration tests.
- [x] H2 database configuration.
- [x] Optional MySQL schema and profile.
- [x] README documentation.
- [x] Submission and integration notes.