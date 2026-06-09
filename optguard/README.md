<img width="1792" height="995" alt="Screenshot 2026-06-08 at 20 05 59" src="https://github.com/user-attachments/assets/a9e7f7a6-4042-42e9-a821-06116f33b93e" />


# OPTGuard

OPTGuard is an MVP web application for F-1 students tracking OPT and STEM OPT dates, reminders, unemployment days, employer records, DSO email templates, and an I-983 checklist.

OPTGuard is not legal advice. Always confirm deadlines and requirements with your DSO or immigration attorney.

## Tech Stack

- Backend: Java 21, Spring Boot 3.x, Maven, Spring Security JWT, Spring Data JPA/Hibernate
- Frontend: Angular 22, npm, Bootstrap
- Database: PostgreSQL
- Docker: PostgreSQL, backend, and frontend services

## Environment Check Results

- Java: `openjdk 21.0.4` is available from `java -version`.
- Maven: `3.9.10`, but it currently runs with Java 24 through `JAVA_HOME`.
- Node: `v21.7.3`, which is not supported by Angular 22.
- npm: `10.8.3`.
- Angular CLI: global CLI is `17.3.12`, but the project uses local Angular CLI `22.0.0`.
- Existing repo contents before scaffolding: `.DS_Store` and `.qodo/` only.

Recommended local upgrades:

```bash
# Node version required by Angular 22
nvm install 24.15.0
nvm use 24.15.0

# Optional global CLI alignment
npm install -g @angular/cli@22.0.0

# If Maven should run exactly on Java 21
brew install openjdk@21
export JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"
```

## Local Setup

From the `optguard` folder:

```bash
docker compose up -d postgres
```

The PostgreSQL defaults are:

- Database: `optguard`
- Username: `optguard`
- Password: `optguard`
- Port: `5432`

## Backend

Run from `optguard/backend`:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/optguard
export DB_USERNAME=optguard
export DB_PASSWORD=optguard
export JWT_SECRET=change-this-local-secret-with-at-least-32-characters
mvn spring-boot:run
```

Temporary no-PostgreSQL demo mode:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2 -Dspring-boot.run.useTestClasspath=true
```

Run tests:

```bash
mvn test
```

## Frontend

Use Node `24.15.0` or another version matching `^22.22.3 || ^24.15.0 || >=26.0.0`.

Run from `optguard/frontend/optguard-ui`:

```bash
npm install
npm start
```

Open `http://localhost:4200`.

## Full Docker Run

From `optguard`:

```bash
docker compose up --build
```

Frontend: `http://localhost:4200`

Backend: `http://localhost:8080`

## Public Deployment

Use [docs/deployment-guide.md](docs/deployment-guide.md) to deploy:

- Backend and PostgreSQL on Render
- Frontend on Vercel
- Free beta mode with `SEED_DEMO_DATA=false`

Current public beta endpoints:

- Frontend: `https://opt-guard.vercel.app`
- Backend health: `https://optguard-api.onrender.com/api/health`

See [docs/launch-record.md](docs/launch-record.md) for the launch record and production readiness checklist.

## Demo User

When `SEED_DEMO_DATA=true` or omitted, the backend seeds:

- Email: `demo@optguard.test`
- Password: `Password123!`

The seed also creates a sample profile, OPT record, employer, generated deadlines, and DSO email templates.

## Environment Variables

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS`
- `CORS_ALLOWED_ORIGINS`
- `SEED_DEMO_DATA`

## Testing Steps

1. Start PostgreSQL with Docker Compose.
2. Run backend tests with `mvn test`.
3. Start backend with `mvn spring-boot:run`.
4. Install frontend dependencies with a compatible Node version.
5. Start frontend with `npm start`.
6. Login with the demo user.
7. Save the profile and OPT/STEM record.
8. Open Deadlines and confirm generated reminders.
9. Mark a deadline complete and reopen it.
10. Copy a DSO email template.

## Known Limitations

- Deadline rules are MVP calculations and must be verified regularly against official guidance and DSO instructions.
- JWT is stored in browser local storage for MVP simplicity.
- No email sending, calendar sync, document upload, or payment integration.
- No admin role or multi-profile advisor workflow.

## Next Steps

- Add calendar export and email reminders.
- Add document upload for EAD, I-20, and I-983.
- Add official guidance links with last-reviewed timestamps.
- Add integration tests for authenticated API flows.
- Add e2e tests for dashboard and deadline completion.

## Suggested Git Commits

1. `chore: scaffold optguard repository`
2. `feat: add spring boot optguard backend`
3. `feat: add angular optguard frontend`
4. `docs: add product api database and setup docs`
5. `test: add backend deadline and dashboard tests`
