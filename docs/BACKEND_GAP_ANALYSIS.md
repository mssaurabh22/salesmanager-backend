# Backend Gap Analysis Against Shared Sales Tracking PPT

## Source Reviewed
- `C:\Users\CT-21-015\Downloads\Sales_Tracking_App_Flow.pptx`

## Overall Assessment
The backend covers the core CRM foundation well, but the PPT describes a more guided operational product flow than the current APIs fully provide.

## Covered Well
- JWT login flow
- employee dashboard endpoint
- owner dashboard endpoint
- lead lifecycle management
- lead reassignment
- lead timeline
- activity logging
- follow-up create/complete/missed
- hot/warm/cold classification

## Covered Partially
- auto follow-up generation
- owner historical review before reassignment
- date-wise performance
- employee daily work orchestration
- lead engagement updates after activities

## Missing
- OTP login
- upcoming follow-up endpoint
- employee alert notifications
- owner alert notifications
- employee-wise timeline API
- date-wise timeline API
- explicit field-visit workflow
- explicit calling workflow
- voice notes support

## Important Backend Implications For UI Teams
- Some user journeys in the PPT will need to be represented as UI flows over generic APIs rather than dedicated backend flows
- Notification screens may need mock or placeholder handling until backend notification delivery is added
- Existing-customer interaction can currently be built using lead detail + activity logging

## Suggested API Additions If Product Wants Full PPT Fidelity
- `GET /api/followups/upcoming`
- `GET /api/timeline/employee/{employeeId}`
- `GET /api/timeline/date-range?...`
- notification preference and alert delivery APIs
- OTP auth endpoints
- dedicated customer interaction workflow APIs for call/visit actions

