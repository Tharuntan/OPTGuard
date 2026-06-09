# OPTGuard User Flows

## New User Onboarding

1. User opens the landing page.
2. User clicks Get Started.
3. User registers with name, email, and password.
4. Backend hashes the password with BCrypt and returns a JWT.
5. Frontend stores the JWT for MVP use and routes the user to the dashboard.

## Add OPT/STEM Dates

1. User opens OPT/STEM Record.
2. User enters OPT, STEM OPT, EAD, status, and unemployment days.
3. User saves the form.
4. Backend upserts the record and generates deadlines.
5. Frontend displays remaining unemployment days.

## Generate Deadlines

1. User enters or updates OPT/STEM dates.
2. Backend generates missing reminders.
3. User can also click Generate reminders on the Deadlines page.
4. Existing generated reminders are not duplicated.

## Add Employer

1. User opens Employers.
2. User enters employer name, E-Verify data, job title, dates, worksite, and supervisor details.
3. If marked current, other employers are cleared as current.
4. User can edit or delete employer records.

## Use Email Template

1. User opens Templates.
2. User selects a DSO email template.
3. User reviews placeholders such as `{{studentName}}`, `{{sevisId}}`, and `{{employerName}}`.
4. User copies the draft and personalizes it before sending.

## Complete Deadline

1. User opens Deadlines.
2. User reviews pending and completed items.
3. User marks a pending deadline complete.
4. Backend updates status while preserving the original generated reminder.

## Disclaimer

OPTGuard is not legal advice. Users must confirm requirements and deadlines with their DSO or immigration attorney.
