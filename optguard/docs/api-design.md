# OPTGuard API Design

Base URL: `http://localhost:8080/api`

All endpoints except `POST /auth/register` and `POST /auth/login` require `Authorization: Bearer <jwt>`.

## Auth

### POST /auth/register

Request:

```json
{
  "email": "student@example.com",
  "password": "Password123!",
  "fullName": "Student Name"
}
```

Response:

```json
{
  "token": "jwt-token",
  "user": {
    "id": 1,
    "email": "student@example.com",
    "fullName": "Student Name",
    "createdAt": "2026-06-08T12:00:00Z"
  }
}
```

### POST /auth/login

Request:

```json
{
  "email": "student@example.com",
  "password": "Password123!"
}
```

Response: same as register.

### GET /auth/me

Response:

```json
{
  "id": 1,
  "email": "student@example.com",
  "fullName": "Student Name",
  "createdAt": "2026-06-08T12:00:00Z"
}
```

## Student Profile

### POST /profile

Creates a profile.

```json
{
  "schoolName": "Sample University",
  "dsoName": "Jordan DSO",
  "dsoEmail": "dso@example.edu",
  "sevisId": "N0000000000",
  "visaStatus": "F-1",
  "passportExpiryDate": "2027-12-31"
}
```

### GET /profile

Returns the current user's profile or `null`.

### PUT /profile

Upserts the profile. Request body matches `POST /profile`.

## OPT Record

### POST /opt-record

Creates an OPT record and generates deadlines.

```json
{
  "optStartDate": "2026-01-01",
  "optEndDate": "2026-12-31",
  "stemStartDate": "2027-01-01",
  "stemEndDate": "2028-12-31",
  "eadStartDate": "2026-01-01",
  "eadEndDate": "2028-12-31",
  "unemploymentDaysUsed": 42,
  "status": "INITIAL_OPT"
}
```

### GET /opt-record

Returns the current user's OPT record or `null`.

### PUT /opt-record

Upserts the OPT record and generates missing deadlines. Request body matches `POST /opt-record`.

## Employers

### POST /employers

```json
{
  "employerName": "Example Analytics Inc.",
  "ein": "12-3456789",
  "eVerifyNumber": "1234567",
  "jobTitle": "Data Analyst",
  "employmentStartDate": "2026-03-01",
  "employmentEndDate": null,
  "worksiteAddress": "100 Market Street, New York, NY",
  "supervisorName": "Avery Manager",
  "supervisorEmail": "avery.manager@example.com",
  "current": true
}
```

### GET /employers

Returns all employers for the current user.

### GET /employers/current

Returns the current employer or `null`.

### PUT /employers/{id}

Updates an employer. Request body matches `POST /employers`.

### DELETE /employers/{id}

Deletes an employer.

## Deadlines

### GET /deadlines

Returns all deadlines.

Example item:

```json
{
  "id": 10,
  "deadlineType": "STEM_VALIDATION",
  "title": "6-month STEM OPT validation",
  "description": "Submit or confirm the required STEM OPT validation report with your DSO.",
  "dueDate": "2027-07-01",
  "status": "PENDING",
  "priority": "SAFE",
  "generated": true
}
```

### POST /deadlines/generate

Generates missing deadlines and returns all deadlines.

### PUT /deadlines/{id}/complete

Marks a deadline complete.

### PUT /deadlines/{id}/pending

Reopens a completed deadline.

## Dashboard

### GET /dashboard/summary

Response:

```json
{
  "currentStatus": "INITIAL_OPT",
  "optEndDate": "2026-12-31",
  "stemEndDate": "2028-12-31",
  "unemploymentDaysUsed": 42,
  "unemploymentDaysRemaining": 108,
  "upcomingDeadlines": [],
  "urgentDeadlines": [],
  "warningMessage": "No unemployment day warning based on the information entered."
}
```

## Email Templates

### GET /email-templates

Returns all seeded templates.

### GET /email-templates/{templateType}

Returns one template by type, such as `EMPLOYER_CHANGE`.

## Disclaimer

OPTGuard is not legal advice. Users must confirm deadlines and requirements with their DSO or immigration attorney.
