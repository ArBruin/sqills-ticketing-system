# Inventory Only Ticketing

Implementation of the Sqills programming assignment.

The solution focuses on a clean, testable domain model while keeping the implementation within the intended scope of the
assignment.

## Design Decisions

The solution focuses on a clean and testable domain model.

Some notable design decisions:

- Business rules are implemented as close to the domain model as possible, keeping the service layer focused on
  orchestration.
- Interfaces are only introduced where they add value (repositories).
- Repositories use in-memory storage, matching the assignment requirements.
- Seat overlap detection is encapsulated in `SeatReservation`.
- Route validation is encapsulated in `Route`.
- Tickets represent a single journey segment for a passenger.
- Duplicate seat reservations are detected by comparing reservation overlap on the same service.

## Assumptions

- Data is stored in memory.
- HTTP is intentionally omitted as requested by the assignment.
- A ticket represents a single journey segment for one passenger.
- A passenger may have multiple tickets when changing seats during a journey.
- Adjacent reservations (e.g. Paris → Amsterdam and Amsterdam → Berlin) are not considered overlapping.
- Scenario 3 has been interpreted as two passengers travelling from London to Amsterdam using separate tickets for each
  journey segment.
- Train services and bookings are stored in memory using repository abstractions.

## Test Coverage

The following scenarios are covered by automated tests:

### Route validation

- Valid journeys can be identified.
- Invalid journey direction is rejected.
- Unknown origin stations are rejected.
- Unknown destination stations are rejected.

### Seat reservation

- Overlapping reservations are detected.
- Adjacent reservations are allowed.
- Nested reservations are detected correctly.
- Independent reservations do not overlap.

### Reservation workflow

- Reserve two first-class seats from Paris to Amsterdam.
- Reject duplicate reservations.
- Reserve two passengers with seat changes from London to Amsterdam.
- Reject duplicate reservations involving seat changes.
- Reject reservations for unknown train services.
- Reject reservations for unknown seats.