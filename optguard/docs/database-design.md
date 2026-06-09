# OPTGuard Database Design

The MVP uses PostgreSQL with Spring Data JPA/Hibernate. Hibernate creates or updates tables during local development with `spring.jpa.hibernate.ddl-auto=update`.

## Tables

### users

- id: primary key
- email: unique login email
- password_hash: BCrypt password hash
- full_name
- created_at
- updated_at

### student_profiles

- id: primary key
- user_id: unique foreign key to users
- school_name
- dso_name
- dso_email
- sevis_id
- visa_status
- passport_expiry_date
- created_at
- updated_at

### opt_records

- id: primary key
- user_id: unique foreign key to users
- opt_start_date
- opt_end_date
- stem_start_date
- stem_end_date
- ead_start_date
- ead_end_date
- unemployment_days_used
- status
- created_at
- updated_at

### employers

- id: primary key
- user_id: foreign key to users
- employer_name
- ein
- e_verify_number
- job_title
- employment_start_date
- employment_end_date
- worksite_address
- supervisor_name
- supervisor_email
- is_current
- created_at
- updated_at

### deadlines

- id: primary key
- user_id: foreign key to users
- deadline_type
- title
- description
- due_date: nullable for checklist-style reminders
- status: PENDING or COMPLETED
- priority: SAFE, WARNING, or URGENT
- generated
- created_at
- updated_at

### email_templates

- id: primary key
- template_type: unique identifier
- title
- subject
- body
- created_at
- updated_at

## Relationships

- One user has one student profile.
- One user has one OPT record.
- One user has many employers.
- One user has many deadlines.
- Email templates are global seed data.

## Notes

Deadline and unemployment calculations are code-level business rules in the backend. They should be reviewed regularly against official government guidance and each school DSO's process.
