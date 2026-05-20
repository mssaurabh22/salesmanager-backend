# Mobile/UI Handoff Notes

## Product Summary
This app is a sales tracking CRM focused on:
- lead lifecycle management
- activity logging
- follow-up discipline
- owner visibility
- auditability through timeline

## Backend-Driven Entities

### Lead
Core fields exposed to clients include:
- customer name
- business name
- contact number
- email
- stage
- weightage
- temperature
- expected value

### Activity
Client-facing activity shape includes:
- lead id
- type
- notes
- GPS latitude/longitude
- created timestamp

### Follow-Up
Client-facing follow-up shape includes:
- lead id
- follow-up time
- status

### Timeline Event
Timeline items represent:
- lead created
- lead updated
- stage changed
- lead reassigned
- activity created
- follow-up created
- follow-up completed
- follow-up missed

## Recommended App Information Architecture

### Employee
1. Login
2. Dashboard
3. Leads list
4. Lead detail
5. Add lead
6. Add activity
7. Today follow-ups
8. Missed follow-ups
9. Timeline

### Owner/Admin
1. Login
2. Owner dashboard
3. Lead search/filter
4. Lead detail
5. Employee performance
6. Reassign leads
7. Reports
8. Timeline

## Backend Status For Major PPT Flows

### Add New Customer / Existing Customer
- New customer: supported
- Existing customer meeting: supported indirectly via activity creation on an existing lead
- dedicated "meeting existing customer" flow API: not present

### Daily Follow-Up Flow
- today follow-ups: supported
- complete follow-up: supported
- missed follow-ups: supported
- upcoming follow-ups: not yet available as dedicated API

### Owner Monitoring Flow
- owner dashboard: supported
- reports: supported
- lead timeline: supported
- employee/date-wise timeline: not yet available

### Reassignment Flow
- bulk/single reassignment: supported
- history checking: possible via timeline, but not a dedicated reassignment preview API

### Notification Flow
- backend scheduler marks missed follow-ups
- actual user-facing alerts/push notifications: not implemented

## Design Opportunities
- Build a "Daily Cockpit" around employee dashboard + today follow-ups
- Treat lead detail as the core workspace screen
- Make timeline a first-class tab on lead detail
- Surface hot/warm/cold visually with strong color semantics
- Use owner dashboard as a summary + drill-down experience

## UI Warnings
- Do not assume OTP
- Do not assume push notification support yet
- Do not assume offline sync support yet
- Use backend responses as wrapped payloads:

```json
{
  "status": "success",
  "message": null,
  "data": {}
}
```

## Good Next Collaboration Tasks
- design wireframes for employee and owner apps
- create screen inventory and navigation map
- define empty/loading/error states for wrapped API responses
- propose UI placeholders for not-yet-implemented flows

