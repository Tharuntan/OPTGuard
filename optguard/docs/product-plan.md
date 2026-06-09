# OPTGuard Product Plan

## Problem

F-1 students on OPT and STEM OPT track many dates across school, employer, EAD, SEVIS, and I-983 workflows. Missing a reporting milestone can create serious stress and risk. Students often manage these deadlines in spreadsheets, email threads, and calendar reminders that are easy to forget.

OPTGuard centralizes the dates, reminders, unemployment-day tracking, DSO email templates, and I-983 checklist in one dashboard.

## Target Users

- F-1 students on initial OPT
- F-1 students preparing for or already on STEM OPT
- Recent graduates who need a lightweight compliance organizer
- University advisors who want a simple demo workflow for students

## MVP Scope

- Email/password registration and JWT login
- Student profile with school, DSO, SEVIS, visa, and passport expiry fields
- OPT/STEM and EAD date tracking
- Automatic deadline generation for STEM validation, evaluations, EAD expiry, OPT end, STEM end, passport expiry, and checklist-style change reporting
- Unemployment days used and remaining
- Employer records with current employer flag
- Dashboard summary with upcoming and urgent reminders
- Seeded DSO email templates with placeholders
- Static I-983 checklist
- PostgreSQL configuration and Docker Compose support

## Future Features

- Calendar export and calendar sync
- User-defined custom reminders
- Document upload for EAD, I-20, and I-983 packets
- Multi-profile support for advisors
- Reminder emails and push notifications
- Official guidance link manager with review dates
- Admin-managed template library

## Monetization Plan

- Free tier for one student profile
- Low-cost premium tier for calendar sync, reminders, and document storage
- University license for bulk onboarding and advisor dashboards
- No payment integration is included in this MVP.

## Disclaimer

OPTGuard is not legal advice. Users must confirm requirements, deadlines, and reporting rules with their DSO or immigration attorney.
