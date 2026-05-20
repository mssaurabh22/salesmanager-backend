# Sales Manager CRM Agent Brief

## Purpose
This repository contains the backend for a modular monolith CRM / Sales Tracking application built with Java 17 and Spring Boot 3.

This brief is intended for:
- UI/UX design agents
- Android app agents
- iOS app agents
- product flow / frontend architecture agents

## Current Backend Modules
- `authentication`: JWT login, refresh, logout
- `user`: user lookup and role model
- `lead-module`: lead lifecycle, search, reassignment, temperature/weightage
- `activity-module`: calls, meetings, WhatsApp, email, notes, GPS-capable activity logging
- `followup-module`: follow-up creation, completion, missed tracking, scheduler
- `timeline-module`: lead journey / audit timeline
- `dashboard-module`: employee and owner dashboards
- `report-module`: analytics and reporting APIs
- `common-lib`: shared support classes such as API response wrapper and common exceptions

## User Roles
- `ADMIN`
  Full visibility, reassignment, owner dashboard, reports
- `EMPLOYEE`
  Lead creation/management, activities, follow-ups, employee dashboard

## Authentication Model
- JWT-based
- Login endpoint uses email + password
- Refresh token flow exists
- OTP login is not implemented

## Standard API Behavior
- APIs are wrapped in a standard response shape by response advice:

```json
{
  "status": "success",
  "message": null,
  "data": {}
}
```

- Validation failures and business errors return structured error payloads

## Core Business Model

### Lead Lifecycle
SPANCO progression:
- `SUSPECT`
- `APPROACH`
- `NEGOTIATION`
- `CLOSURE`
- `ORDER`
- `LOST`

Current backend enforces progression and blocks invalid transitions/jumps.

### Lead Temperature
- `0–30` => `COLD`
- `31–70` => `WARM`
- `71–100` => `HOT`

### Activity Types
- `CALL`
- `MEETING`
- `WHATSAPP`
- `EMAIL`
- `NOTE`

### Follow-Up Status
- `PENDING`
- `COMPLETED`
- `MISSED`

## Major Supported Flows
- User logs in and receives JWT
- Employee creates lead
- Employee updates lead and stage
- Employee adds activities against a lead
- Activity creation can affect lead engagement/weightage
- Follow-ups can be created, completed, auto-marked missed by scheduler
- Timeline tracks lead journey events
- Admin can reassign leads
- Owner/admin can view aggregate dashboards and reports

## Key Backend Endpoints

### Auth
- `POST /api/auth/login`
- `POST /api/auth/refresh`
- `POST /api/auth/logout`

### Leads
- `POST /api/leads`
- `GET /api/leads/{id}`
- `PUT /api/leads/{id}`
- `PUT /api/leads/{id}/stage`
- `POST /api/leads/search`
- `PUT /api/leads/reassign`

### Activities
- `POST /api/activities`
- `GET /api/activities/lead/{leadId}`
- `GET /api/activities/user`

### Follow-Ups
- `POST /api/followups`
- `PUT /api/followups/{id}/complete`
- `GET /api/followups/today`
- `GET /api/followups/missed`

### Timeline
- `GET /api/timeline/lead/{leadId}`

### Dashboard
- `GET /api/dashboard/employee`
- `GET /api/dashboard/owner`

### Reports
- `GET /api/reports/employee-performance`
- `GET /api/reports/lead-conversion`
- `GET /api/reports/followup-compliance`

## PPT / Flow Alignment Summary
The shared Sales Tracking PPT is only partially covered by the backend.

### Covered
- login flow
- role-based dashboard routing support
- lead creation and management
- activity logging
- follow-up creation and missed/completed tracking
- owner dashboard
- reassignment
- lead timeline
- hot/warm/cold lead status

### Partially Covered
- auto-generated follow-ups after engagement
- owner timeline/history before reassignment
- employee daily work cockpit
- date-wise performance visibility
- calling / field visit flows

### Missing
- OTP login
- employee alerts for due follow-ups
- owner alerts for missed follow-ups
- upcoming follow-up endpoint
- employee-wise timeline view
- date-wise timeline view
- explicit call-flow / field-visit workflow APIs
- voice notes

## Guidance For UI / Mobile Agents

### Design Assumptions
- Backend is API-first and suitable for mobile clients
- Employee and owner should have clearly separated app surfaces
- Timeline is a major trust/audit feature and should be highly visible
- Follow-up discipline is central to the product value

### Recommended Primary Screens

#### Employee App
- Login
- Employee dashboard
- Add lead
- Lead detail
- Add activity
- Today follow-ups
- Missed follow-ups
- Timeline for a lead

#### Owner/Admin App
- Login
- Owner dashboard
- Lead search/filter
- Employee performance
- Lead reassignment
- Lead timeline
- Reports

### UX Notes
- Expose lead temperature and stage prominently
- Make follow-up due/missed states visually strong
- Timeline should be reverse chronological
- Reassignment should show current owner and history context
- Lead detail should combine summary + activities + follow-ups + timeline

## Important Constraints For Frontend Agents
- OTP flow should not be assumed available
- Push notifications are not yet backed by a notification service
- Some PPT flows may need UI placeholders pending backend support
- Reports are aggregate APIs, not raw data exports

## Swagger
- Swagger UI is expected at:
  `http://localhost:8081/swagger-ui/index.html`

## Suggested Next Backend Priorities
- upcoming follow-up API
- notification/event delivery APIs
- employee-wise and date-wise timeline APIs
- stronger owner operational views
- explicit field-visit / call workflow support

