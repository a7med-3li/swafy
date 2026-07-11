# Swafy — TO-DO

Last updated: July 2026

---

## Current Progress Summary

| Layer | Status |
|---|---|
| Backend (Spring Boot) | ~70% — Core features done, payment/notifications stubs |
| Admin Dashboard (React) | ~90% — All 8 pages functional |
| Passenger App (React) | ~10% — Only signup + welcome screen |
| Driver App | 0% — Not started (planned as same codebase as passenger app) |
| Landing Page | Done — Static waitlist pages (EN + AR) |

---

## CRITICAL — Must Fix Before MVP Launch

These are blockers. The app will not function for real users without them.

### 1. Payment Gateway Integration (Fawry + Vodafone Cash)

- `PaymentService` is an empty stub — no actual payment processing
- `PaymentController` has no endpoints
- `Payment` entity exists in DB but is unused
- Subscriptions are purchased without any payment validation
- **Impact:** Cannot collect 200 EGP from students. No revenue. No driver payouts.
- **Action:** Integrate Fawry API + Vodafone Cash API. Implement payment verification on subscription purchase.

### 2. Fare Calculation Service

- `FareCalculationService` is a stub
- `RideEstimationService` hardcodes fare as `50.00` EGP
- `PricingService` is empty
- `PriceBreakdownDto` exists but is unused
- **Impact:** All fare estimates are wrong (50 EGP instead of the actual 5–8 EGP range)
- **Action:** Implement corridor-based flat fare calculation (6–8 EGP for Class A, subscription-based for Class C).

### 3. GeoUtils / Distance Calculation

- `GeoUtils.distanceMeters()` is a placeholder (no Haversine implementation)
- **Impact:** Cannot calculate actual distances between VBS points or verify pickup proximity
- **Action:** Implement Haversine formula. Used by fare calculation and proximity checks.

### 4. Subscription Expiry Scheduler

- `SubscriptionService.expireSubscriptions()` exists but is **not wired to any cron job**
- Subscriptions will never auto-expire
- **Impact:** Expired subscriptions remain active, passengers ride for free
- **Action:** Add `@Scheduled` annotation or create a scheduler bean (similar to `NoShowScheduler`).

### 5. Ride Type Service

- `RideTypeService` is empty
- **Impact:** Cannot determine available ride types based on online drivers
- **Action:** Implement to show available corridors/vehicles based on active drivers.

---

## HIGH PRIORITY — Needed for Full MVP

### 6. Passenger App — Login & Auth Flow

- Only signup + welcome screen exist
- No login screen
- No JWT token storage
- No auth state management ( ProtectedRoute equivalent)
- No React Router — just `useState` screen switcher
- **Action:** Implement login, token storage (SecureStore for mobile), auth context, protected routes.

### 7. Passenger App — Subscription Purchase Flow

- No UI for viewing plans or purchasing subscriptions
- **Action:** Create plans list screen + purchase flow with Fawry/Vodafone Cash integration.

### 8. Passenger App — Seat Booking Flow

- No UI for browsing departures or booking seats
- **Action:** Create corridor search → available departures → booking confirmation → PIN display flow.

### 9. Passenger App — Ride History

- No ride history screen
- **Action:** Fetch and display passenger ride history from `/api/v3/ride/history`.

### 10. Passenger App — Profile Management

- No profile screen
- **Action:** Display and edit user info from `/api/v1/users/me`.

### 11. Driver App (Same Codebase)

- Not started at all
- **Action:** Implement driver mode toggle in passenger app codebase. Build: corridor activation, manifest view, PIN entry, earnings dashboard.

### 12. Push Notifications

- `PushNotificationService` and `SmsNotificationService` are empty stubs
- **Action:** Implement push notification service for ride updates, departure reminders, subscription expiry alerts.

---

## MEDIUM PRIORITY — Important but Not Blocking

### 13. Rating & Review System

- `RatingService` and `RatingController` are empty
- `RideRating` entity exists in DB
- **MVP note:** Collected informally during MVP period. Implement after launch.

### 14. Google Maps Routing Fallback

- `GoogleMapsRoutingService` is a stub
- `HERERoutingServiceImpl` is `@Primary` and functional
- **Action:** Implement as fallback when HERE API is unavailable.

### 15. RideTypeService

- Empty stub
- **Action:** Show available ride types based on online/active drivers.

### 16. SMS Notifications

- `SmsNotificationService` is empty
- Twilio is configured and working for OTP
- **Action:** Extend Twilio integration for ride confirmations, departure reminders.

### 17. App Client — React Router

- Currently using `useState` for screen switching
- **Action:** Migrate to React Router with proper routes for login, home, booking, history, profile.

---

## LOW PRIORITY — Phase 2+

- Class B private hailing integration
- Class D inter-district shuttles
- Class E women-only corridor
- In-app chat between driver and passenger
- Corporate subscription tiers
- Multiple corridor support
- Dynamic pricing engine
- Surge pricing logic
- Arabic localization for app client
- Dark mode polish across all clients
- Series A pitch deck preparation

---

## Done — Completed Features

### Backend

- ✅ User registration (passenger + driver) with phone + password
- ✅ JWT authentication (login, logout, refresh tokens, rate limiting)
- ✅ OTP verification via Twilio
- ✅ User management (CRUD, roles, soft delete)
- ✅ Driver profile, corridor activation, shift toggle
- ✅ Driver wallet (payout 4 EGP/ride, deposits 500–1000 EGP, transactions audit trail)
- ✅ Passenger profile with ride balance tracking
- ✅ Corridor + VBS management (CRUD)
- ✅ Subscription system (3 plans, purchase, deduct, cancel)
- ✅ Ride booking, confirmation (PIN), completion, cancellation
- ✅ No-show scheduler (marks expired bookings every 15 min)
- ✅ HERE API routing integration (geocoding + route calculation)
- ✅ Address autocomplete (local DB + HERE API fallback)
- ✅ Admin API (stats, rides, drivers, deposits, subscriptions, no-shows)
- ✅ Spring Security config with role-based access control
- ✅ CORS configuration
- ✅ Swagger/OpenAPI documentation
- ✅ Flyway database migrations (2 versions)
- ✅ Rate limiting on auth endpoints (10 req/min)
- ✅ Global exception handling

### Admin Dashboard

- ✅ Login page with JWT authentication
- ✅ Dashboard stats overview (users, rides, revenue, no-shows)
- ✅ Ride monitoring with status filters
- ✅ Driver management (approve/reject, filter by status)
- ✅ Deposit management (approve/forfeit with reason)
- ✅ Subscription sales view with stats breakdown
- ✅ No-show tracking + passenger ranking
- ✅ Corridor CRUD with Leaflet map picker for VBS locations

### Passenger App (Partial)

- ✅ Registration form with full validation
- ✅ Post-signup welcome screen with confetti animation

### Landing Page

- ✅ English landing page (v1 + v2 variants)
- ✅ Arabic RTL landing page
- ✅ Waitlist form → Google Sheets integration
- ✅ React + Capacitor announcement app (with Android build)
