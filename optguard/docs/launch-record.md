# OPTGuard Launch Record

OPTGuard was built as a public beta web application for F-1 students who need a clearer way to track OPT and STEM OPT deadlines.

## Public Beta URLs

- Frontend: `https://opt-guard.vercel.app`
- Backend health: `https://optguard-api.onrender.com/api/health`
- Repository: `https://github.com/Tharuntan/OPTGuard`

## Build Story

- Built from scratch with AI-assisted development in a focused launch session across June 8-9, 2026.
- Shipped as a full-stack application, not just a static landing page.
- Deployed with a live Angular frontend, Spring Boot API, JWT authentication, and PostgreSQL database.

## What Is Live

- Public registration and login
- Student profile capture
- OPT and STEM OPT date tracking
- Deadline generation
- Employer records
- DSO email templates
- Dashboard summary
- Backend health endpoint
- Production beta deployment on Vercel and Render

## Production Readiness Checklist

- [x] Frontend deployed publicly on Vercel
- [x] Backend deployed publicly on Render
- [x] PostgreSQL database provisioned on Render
- [x] Demo seed disabled in production
- [x] CORS restricted to the Vercel frontend domain
- [x] Demo credentials removed from the public login screen
- [x] Runtime API URL configured through Vercel environment variables
- [ ] Complete live registration smoke test from Vercel
- [ ] Complete login smoke test from Vercel
- [ ] Add Privacy Policy page before inviting strangers
- [ ] Add Terms page before inviting strangers
- [ ] Add visible legal disclaimer inside authenticated dashboard
- [ ] Add basic analytics for launch traffic
- [ ] Add a support/contact email
- [ ] Buy and connect a custom domain
- [ ] Review Render database backup limits before paid launch

## Beta Launch Rules

- Keep the product free while validating real student workflows.
- Do not promise legal advice or deadline guarantees.
- Ask users to verify all immigration actions with their DSO or immigration attorney.
- Collect feedback before adding paid plans.

## Suggested Launch Post

I built OPTGuard from scratch in a focused AI-assisted development session.

It is a full-stack OPT/STEM OPT tracking app for F-1 students, with authentication, deadline generation, employer records, DSO email templates, Angular frontend, Spring Boot backend, PostgreSQL database, and public deployment on Vercel and Render.

Public beta: `https://opt-guard.vercel.app`

This is not legal advice. It is a productivity tool to help students stay organized and verify deadlines with their DSO.
