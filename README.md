# Inventory Only Ticketing

Implementation of the Sqills **Inventory Only Ticketing** programming assignment.

The solution focuses on a clean, testable domain model while keeping the implementation within the intended scope of the assignment.

## Design Decisions

The solution is intentionally kept simple and focuses on correctness, readability and separation of responsibilities.

Some notable design decisions:

- Business rules are implemented as close to the domain model as possible, keeping the service layer focused on orchestration.
- Interfaces are only introduced where they provide a clear abstraction (repositories).
- Repositories use in-memory storage, matching the assignment requirements.
- Seat overlap detection is encapsulated in `SeatReservation`.
- Route validation is encapsulated in `Route`.
- Tickets represent a single journey segment for a passenger.
- Duplicate seat reservations are detected by comparing reservation overlap on the same train service.
- Seat reservations are fully validated before persisting a booking, ensuring an all-or-nothing reservation workflow.
- A REST-like API abstraction is provided without using HTTP, as requested in the assignment.
- Domain exceptions are translated into response status codes at the API boundary, keeping transport concerns outside the domain model.
- Train services have an explicit identity based on service number and operating date. Reservation conflicts compare this identity rather than the full service object structure.
- The required routes (Paris–London, Paris–Amsterdam and Amsterdam–Berlin) are implemented as reusable test fixtures used throughout the automated tests.

## Assumptions

- Data is stored in memory.
- HTTP is intentionally omitted as requested by the assignment.
- A ticket represents a single journey segment for one passenger.
- A passenger may have multiple tickets when changing seats during a journey.
- Adjacent reservations (for example Paris → Amsterdam and Amsterdam → Berlin) are not considered overlapping.
- Scenario 3 has been interpreted as two passengers travelling from London to Amsterdam using separate tickets for each journey segment.
- Train services and bookings are stored in memory using repository abstractions.

## Test Coverage

The following scenarios are covered by automated tests.

### Route Validation

- Valid journeys can be identified.
- Invalid journey direction is rejected.
- Unknown origin stations are rejected.
- Unknown destination stations are rejected.

### Seat Reservation

- Overlapping reservations are detected.
- Adjacent reservations are allowed.
- Nested reservations are detected correctly.
- Independent reservations do not overlap.

### Reservation Workflow

- Reserve two first-class seats from Paris to Amsterdam.
- Reject duplicate reservations.
- Reserve two passengers with seat changes from London to Amsterdam.
- Reject duplicate reservations involving seat changes.
- Reject reservations for unknown train services.
- Reject reservations for unknown seats.
- Reject invalid journeys.
- Reject duplicate seat reservations within the same request.