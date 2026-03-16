**SOLID/Ex1 — SRP: Student Onboarding** breaks up a monolithic `OnboardingService` that was doing everything at once. The fix was straightforward: pull parsing into `RawInputParser`, validation into `StudentValidator` with a clean `ValidationResult` object, persistence behind a `StudentRepository` interface backed by `FakeDBRepository`, and all console output into a dedicated `Printer` class. The orchestrator just wires them together.

**SOLID/Ex2 — SRP: Cafeteria Billing** had a `CafeteriaSystem` mixing pricing math, tax, discounts, formatting, and saving. Each concern got its own home: `PricingCalculator`, `TaxPolicy`/`DefaultTaxPolicy`, `DiscountPolicy`/`DefaultDiscountPolicy`, `InvoiceBuilder` for formatting, and `InvoiceRepository` for persistence. The system becomes a pure coordinator.

**SOLID/Ex3 — OCP: Placement Eligibility** replaced a long if/else chain with a list of `EligibilityRule` objects (`CgrRule`, `AttendanceRule`, `CreditsRule`, `DisciplineRule`). The engine iterates the list and short-circuits on first failure. Adding a new rule means adding a new class and wiring it in — no editing the engine.

**SOLID/Ex4 — OCP: Hostel Fee Calculator** eliminated a room-type switch by introducing a `PricingComponent` interface. `RoomPricing` uses a map lookup instead of branching, and `AddOnPricing` sums add-on costs similarly. The calculator just iterates components and accumulates the total.

**SOLID/Ex5 — LSP: File Exporters** addressed the problem of `PdfExporter` tightening preconditions by throwing on large content. The base `Exporter` class handles null-checking, and subclasses use the `validate` hook only where the contract genuinely allows it. The demo catches the PDF exception gracefully, preserving observable output while making the hierarchy explicit about its constraints.

**SOLID/Ex6 — LSP: Notification Senders** fixed `WhatsAppSender` throwing on non-E.164 phone numbers and `SmsSender` silently ignoring fields. The base `NotificationSender` enforces null-checking and audit logging via a template method; each sender's `doSend` handles only its channel-specific logic, with the WhatsApp exception caught at the call site in the demo.

**SOLID/Ex7 — ISP: Smart Classroom Devices** split a fat `SmartClassroomDevice` marker into focused capability interfaces: `PowerControl`, `BrightnessControl`, `TemperatureControl`, `InputConnectable`, and `AttendanceScanning`. Each device implements only what it actually supports. A generic `DeviceRegistry` resolves devices by capability interface, so the controller never touches concrete classes.

**SOLID/Ex8 — ISP: Club Admin Tools** followed the same ISP pattern — split `ClubAdminTools` into `FinanceOps`, `MinutesOps`, and `EventOps`. `TreasurerTool`, `SecretaryTool`, and `EventLeadTool` each implement only their relevant interface, and `ClubConsole` depends on those three small interfaces rather than one bloated one.

**SOLID/Ex9 — DIP: Evaluation Pipeline** decoupled the pipeline from concrete graders and checkers by introducing `PlagiarismService`, `CodeGradingService`, and `ReportWriter` interfaces. A default no-arg constructor wires in the real implementations, while a second constructor accepts any combination for testing or swapping strategies without touching pipeline logic.

**SOLID/Ex10 — DIP: Transport Booking** applied the same injection pattern to `TransportBookingService`, extracting `DistanceService`, `DriverAllocationService`, and `PaymentService` interfaces. The default constructor wires concrete implementations, keeping `Demo10` unchanged while making every dependency swappable.

**adapter-payments** introduced a `PaymentGateway` interface as the target, then `FastPayAdapter` and `SafeCashAdapter` as thin wrappers that translate each SDK's unique method signature into `charge(customerId, amountCents)`. `OrderService` does a simple map lookup by provider key — no branching, no SDK awareness.

**flyweight-markers** extracted `MarkerStyle` as an immutable intrinsic state object and built a `MarkerStyleFactory` that caches instances by a compound string key. `MapDataSource` obtains styles through the factory rather than constructing them directly, so thousands of markers share a small fixed pool of style objects.

**immutable-tickets** made `IncidentTicket` fully immutable with private final fields, no setters, a defensive copy of the tags list, and a nested `Builder` that centralizes all validation in `build()`. A `toBuilder()` method enables non-destructive "updates" by copying existing state into a new builder — `TicketService` returns new instances instead of mutating existing ones.

**proxy-reports** introduced a `Report` interface and split responsibility between `RealReport` (does the expensive disk load) and `ReportProxy` (holds metadata, checks access via `AccessControl`, lazy-loads `RealReport` on first authorized access, and caches it for subsequent calls). `ReportViewer` and all client code depend only on the `Report` interface.

**singleton-metrics** hardened `MetricsRegistry` with a private constructor that throws if called after initialization, double-checked locking with a `volatile` instance field for thread safety, and a `readResolve()` method to return the existing instance on deserialization. `MetricsLoader` was updated to call `getInstance()` rather than `new MetricsRegistry()`.
