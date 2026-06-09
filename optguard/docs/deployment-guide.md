# OPTGuard Deployment Guide

This guide uses Render for the Spring Boot API and PostgreSQL, and Vercel for the Angular frontend.

Current public beta:

- Frontend: `https://opt-guard.vercel.app`
- Backend health: `https://optguard-api.onrender.com/api/health`

## What You Need

- GitHub repository containing the `optguard` folder
- Render account
- Vercel account
- Optional custom domain

## 1. Push to GitHub

The current machine does not have GitHub CLI installed. Use VS Code Source Control, GitHub Desktop, or install GitHub CLI.

Recommended repo name:

```text
optguard
```

## 2. Deploy Backend on Render

1. Open Render.
2. Choose New Blueprint.
3. Connect the GitHub repo.
4. Select the `optguard/render.yaml` blueprint.
5. Render creates:
   - `optguard-api`
   - `optguard-db`
6. Wait for deploy to finish.
7. Copy the backend URL. It will look like:

```text
https://optguard-api.onrender.com
```

The health check should work at:

```text
https://YOUR-BACKEND-URL/api/health
```

## 3. Deploy Frontend on Vercel

1. Open Vercel.
2. Import the same GitHub repo.
3. Set project root to:

```text
optguard/frontend/optguard-ui
```

4. Add environment variable:

```text
OPTGUARD_API_URL=https://YOUR-BACKEND-URL/api
```

5. Deploy.
6. Copy the frontend URL. It will look like:

```text
https://YOUR-PROJECT.vercel.app
```

## 4. Update Render CORS

After Vercel gives you the frontend URL, update the Render backend environment variable:

```text
CORS_ALLOWED_ORIGINS=https://opt-guard.vercel.app
```

Redeploy the backend.

## 5. Production Settings

Use these backend environment values in Render:

```text
SEED_DEMO_DATA=false
JWT_SECRET=<Render generated secret or openssl-generated secret>
```

Do not use demo data for public production beta.

## 6. Custom Domain

Recommended domain directions:

- Use a short, memorable `.com`, `.app`, or `.io` if affordable.
- Point the main domain to Vercel.
- Keep the API on Render's generated domain first.

Suggested names to check:

- `optguard.app`
- `myoptguard.com`
- `optdeadline.com`
- `stemoptguard.com`
- `opttrackr.com`
- `f1deadline.com`

Domain availability changes constantly, so verify on Cloudflare Registrar, Namecheap, GoDaddy, or Squarespace Domains before buying.

## 7. Free Beta Launch

For free beta:

- Keep Stripe disabled.
- Add Privacy Policy and Terms before inviting strangers.
- Keep the legal disclaimer visible.
- Ask beta users for feedback before adding paid plans.

## 8. Post-Launch Checklist

- Register a test account.
- Log out and log back in with the same account.
- Add profile data.
- Add OPT/STEM dates.
- Generate deadlines.
- Add employer.
- Mark a deadline complete.
- Copy an email template.
- Test the app in a private/incognito browser window.
- Confirm no demo account is publicly advertised.
- Confirm Render `CORS_ALLOWED_ORIGINS` exactly matches the Vercel frontend domain.
- Confirm Vercel `OPTGUARD_API_URL` is set to the Render API base URL.

## Legal Reminder

OPTGuard is not legal advice. Users must confirm requirements and deadlines with their DSO or immigration attorney.
