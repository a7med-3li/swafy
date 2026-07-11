# MVP Specification — Swafy

Based on the Software Product Description (SPD) document.

---

## MVP Thesis

If 150 students pay 200 EGP upfront for 40 rides on the Train Station → University corridor, and 5 drivers complete those rides consistently for 60 days, Swafy works. That's the only thing the MVP needs to prove.

---

## 1. Product Definition

Swafy is a localized hybrid transit platform for Beni Suef Governorate. It digitizes corridor-based shared rides with guaranteed seats, virtual bus stops (VBS), and prepaid subscriptions. The MVP focuses exclusively on **Class C subscriptions on a single corridor**.

### 1.1 Corridor-Matching Engine

Fixed-route matching — not open-radius dispatch. The city's highest-demand transit corridors are mapped as virtual lines. Drivers select a line; the app matches passengers along that path with real-time seat availability.

### 1.2 Virtual Bus Stop (VBS) System

All pickups occur at pre-mapped VBS — major landmarks, intersections, and institutional gates spaced 400–800m apart. Passengers walk to the nearest VBS. Beni Suef is compact (14 km²); max walk to any VBS is under 7 minutes.

### 1.3 Driver Accountability System

1. **Prepaid rides** — drivers can only receive payout by processing trips in-app. No cash to take.
2. **Driver deposit** — 500–1,000 EGP held as collateral on onboarding.
3. **PIN boarding confirmation** — verifiable record of every trip.

---

## 2. Service Classes

### MVP Scope — Class C Only

| Metric | Student Basic | Student Plus | Corporate Commuter |
|---|---|---|---|
| Price | 200 EGP/month | 300 EGP/month | 350 EGP/month |
| Rides | 40 | 70 | 60 |
| Per-Ride Cost | 5.00 EGP | 4.28 EGP | 5.83 EGP |

- **No-Show Policy:** Unconfirmed rides forfeited from monthly balance
- **Driver Payout:** 4 EGP/ride triggered by passenger boarding confirmation
- **Platform Margin:** 0.75–1 EGP per ride

### Class A — Shared Corridor (Future)

| Metric | Value |
|---|---|
| Vehicle Type | 7-seat minivan / microbus |
| Operation Mode | Fixed virtual corridor with VBS pickups |
| Pricing | Flat fare per ride (6–8 EGP) |
| Booking Window | On-demand or up to 2 hours ahead |
| Driver Payout | 4 EGP per passenger per ride |
| Platform Margin | ~1 EGP per passenger ride |

### Class B — Private Hailing (Phase 2)

| Metric | Value |
|---|---|
| Vehicle Type | Standard private sedan |
| Operation Mode | Open-grid dispatch, door-to-door |
| Pricing | Pre-booked fixed fare (no surge) |
| Driver Payout | 75–80% of fare |
| Platform Margin | 20–25% commission |

### Class D — Inter-District Shuttles (Phase 2)
Scheduled shuttles connecting Beni Suef city center with district capitals (El Wasta, Biba, El Fashn, Ihnasiya). Fixed departure times, bookable 24 hours ahead.

### Class E — Women-Only Corridor (Phase 2)
Same as Class A but operated exclusively by female drivers for female passengers. Separate matching pool.

---

## 3. MVP Scope

### 3.1 What Is Included

- **Passenger app:** Account creation, corridor subscription purchase, seat booking, trip tracking, PIN-based boarding
- **Driver app:** Corridor activation, passenger manifest view, boarding confirmation, payout tracking
- **Admin dashboard:** Trip monitoring, driver management, subscription sales, no-show tracking, manual dispute resolution
- **Payment integration:** Fawry and Vodafone Cash for subscriptions and driver top-ups
- **One corridor only:** Train Station → Mahatta Square → University Main Gate (3 VBS stops)
- **One subscription tier:** Student Basic at 200 EGP/month for 40 rides
- **Operating hours:** 7:00 AM – 6:00 PM only

### 3.2 What Is Deliberately Excluded

- Class B private hailing
- Class D inter-district shuttles
- Class E women-only service
- Dynamic or surge pricing (flat fare only)
- In-app chat between driver and passenger
- Rating and review system (collected informally during MVP)
- Corporate subscription tiers
- Multiple corridors

### 3.3 Technical Minimum

| Component | Technology |
|---|---|
| Passenger App | React Native (iOS + Android) |
| Driver App | Same codebase, driver mode toggle |
| Backend | Java 21 + Spring Boot 3.1 + PostgreSQL |
| Maps & Routing | HERE API (with Google Maps fallback) |
| Payments | Fawry API + Vodafone Cash API |
| SMS Notifications | Twilio |
| Admin Panel | React + Vite + shadcn/ui |
| Estimated Build Time | 8–12 weeks with a team of 2 developers |

---

## 4. The Single Most Important MVP Feature

**Boarding confirmation via PIN.** When a passenger boards, they show a 4-digit PIN to the driver who enters it into the driver app. This single interaction:
1. Triggers driver payout
2. Deducts the ride from the passenger's monthly balance
3. Creates a verifiable trip record
4. Makes off-platform cash transactions structurally pointless for the driver

**Do not launch without this.**

---

## 5. Build Phases & Plan

### Phase 0 — Pre-Launch Validation (Weeks 1–4)

| # | Task | Status |
|---|---|---|
| 1 | Map corridor physically — identify 3 VBS locations, GPS-pin each | Not Started |
| 2 | Talk to 30+ students at university gate — validate demand | Not Started |
| 3 | Recruit 5 drivers from Train Station microbus staging area | Not Started |
| 4 | Collect 500 EGP deposits from drivers | Not Started |
| 5 | Run manual pilot for 2 weeks via WhatsApp + spreadsheet | Not Started |

### Phase 1 — Backend Core (Weeks 5–8)

| # | Task | Status |
|---|---|---|
| 1 | User registration (passenger + driver) with phone + password | ✅ Done |
| 2 | JWT authentication (login, logout, refresh, rate limiting) | ✅ Done |
| 3 | OTP verification via Twilio | ✅ Done |
| 4 | User management (CRUD, roles, soft delete) | ✅ Done |
| 5 | Driver profile, corridor activation, shift toggle | ✅ Done |
| 6 | Driver wallet (payout 4 EGP/ride, deposits, transactions) | ✅ Done |
| 7 | Passenger profile with ride balance tracking | ✅ Done |
| 8 | Corridor + VBS management (CRUD) | ✅ Done |
| 9 | Subscription system (plans, purchase, deduct, cancel) | ✅ Done |
| 10 | Ride booking, confirmation (PIN), completion, cancellation | ✅ Done |
| 11 | No-show scheduler (marks expired bookings every 15 min) | ✅ Done |
| 12 | HERE API routing integration | ✅ Done |
| 13 | Address autocomplete (local DB + API fallback) | ✅ Done |
| 14 | Admin API (stats, rides, drivers, deposits, subscriptions, no-shows) | ✅ Done |
| 15 | **Payment gateway integration (Fawry + Vodafone Cash)** | ❌ Not Started |
| 16 | **Subscription expiry scheduler** (logic exists, not wired) | ❌ Not Started |
| 17 | **Fare calculation service** (currently hardcoded 50.00) | ❌ Not Started |
| 18 | **Pricing service** (empty stub) | ❌ Not Started |
| 19 | **GeoUtils / distance calculation** (placeholder) | ❌ Not Started |
| 20 | **Ride type service** (empty stub) | ❌ Not Started |

### Phase 2 — Admin Dashboard (Weeks 8–10)

| # | Task | Status |
|---|---|---|
| 1 | Login page with JWT auth | ✅ Done |
| 2 | Dashboard stats overview | ✅ Done |
| 3 | Ride monitoring with filters | ✅ Done |
| 4 | Driver management (approve/reject) | ✅ Done |
| 5 | Deposit management (approve/forfeit) | ✅ Done |
| 6 | Subscription sales view + stats | ✅ Done |
| 7 | No-show tracking + passenger ranking | ✅ Done |
| 8 | Corridor CRUD with map picker | ✅ Done |

### Phase 3 — Passenger App (Weeks 10–13)

| # | Task | Status |
|---|---|---|
| 1 | Registration form with validation | ✅ Done |
| 2 | Post-signup welcome screen | ✅ Done |
| 3 | **Login screen** | ❌ Not Started |
| 4 | **Token storage + auth state management** | ❌ Not Started |
| 5 | **React Router setup** | ❌ Not Started |
| 6 | **Subscription purchase flow** | ❌ Not Started |
| 7 | **Seat booking flow** (next available departure) | ❌ Not Started |
| 8 | **PIN display at boarding** | ❌ Not Started |
| 9 | **Ride history** | ❌ Not Started |
| 10 | **Profile management** | ❌ Not Started |
| 11 | **Push notifications** | ❌ Not Started |

### Phase 4 — Driver App (Weeks 11–14)

| # | Task | Status |
|---|---|---|
| 1 | Driver registration (same app, mode toggle) | ❌ Not Started |
| 2 | Corridor activation | ❌ Not Started |
| 3 | Passenger manifest view | ❌ Not Started |
| 4 | PIN entry for boarding confirmation | ❌ Not Started |
| 5 | Earnings dashboard | ❌ Not Started |
| 6 | Shift management | ❌ Not Started |

### Phase 5 — Beta & Launch (Weeks 14–16)

| # | Task | Status |
|---|---|---|
| 1 | Closed beta with 10 recruited students | Not Started |
| 2 | Fix PIN entry flow bugs | Not Started |
| 3 | Test Fawry payment end-to-end | Not Started |
| 4 | Complete 50 successful app-mediated trips | Not Started |
| 5 | Campus launch (physical presence at university gate) | Not Started |
| 6 | 3-day free trial promotion (8 rides cap) | Not Started |
| 7 | Plant 3–5 student ambassadors with referral codes | Not Started |

### Phase 6 — Post-Launch Stability (Weeks 16–26)

| # | Task | Status |
|---|---|---|
| 1 | Monitor no-show rate daily (target < 10%) | Not Started |
| 2 | Add second corridor at Month 3 | Not Started |
| 3 | Launch Class B private hailing at Month 4–5 | Not Started |
| 4 | Prepare Series A narrative at Month 6 | Not Started |

---

## 6. Revenue Model

### Revenue Streams (MVP)

1. Per-ride platform margin on Class C corridor rides (0.75–1 EGP/ride)
2. Subscription revenue float (upfront cash before rides consumed)

### Profitability Timeline (Conservative)

| Milestone | Target |
|---|---|
| Month 1 | 150 subscribers — Net: -10,000 EGP (intentional seeding loss) |
| Month 3 | 600 subscribers — Approaching breakeven |
| Month 6 | 1,500 subscribers — First profitable month (+40,000–60,000 EGP/month gross) |
| Month 12 | 4,000+ subscribers + Class B active — Sustainable operations |
| Month 18 | Multi-corridor, Class D launch — Regional expansion begins |

---

## 7. Risk: The One Thing That Kills This Platform

**Drivers defecting to WhatsApp.** Drivers collecting student contact numbers through the app and taking cash directly.

**Defense:** Make the subscription valuable beyond the ride — exam reminders, university schedule integration, priority booking during finals week, digital student transit card. The more Swafy feels like a student membership rather than a ticket, the harder it is for a driver to replicate in WhatsApp.
