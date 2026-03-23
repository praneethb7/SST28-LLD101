---

## The problem this solves

The mutable `IncidentTicket` was a ticking time bomb. You could create a ticket, pass it to an audit logger, and then mutate the priority before the log was written — the log would capture the *new* state, not the state at the time of the event. The refactor eliminates this entire class of bug by making the object's state fixed at birth.

There are three interlocking ideas here. Let me diagram the class structure, then walk through each one.---

Now the detailed walkthrough of each decision.

---

## 1. Making `IncidentTicket` truly immutable

```java
public final class IncidentTicket {
    private final String id;
    private final String priority;
    private final List<String> tags;
    ...
    private IncidentTicket(Builder builder) { ... }
```

Three things working together here. The class is `final` so nobody can subclass it and sneak in a setter. Every field is `private final` — there are no setters at all. And the constructor is `private`, which means the only way to get an instance is through the Builder. An object that cannot be constructed except through a validated, controlled path, and whose fields cannot be changed after construction — that's the definition of immutability.

The subtle part is the `List`. Strings are already immutable in Java, so those fields are safe. But a `List` is a reference type — if you store the caller's list directly, they still hold a reference and can mutate it. There are two defensive copies in play:

- In the `Builder`: `this.tags = new ArrayList<>(tags)` — the Builder owns its own copy, independent of whatever the caller passed in.
- In `IncidentTicket`'s constructor: `new ArrayList<>(builder.tags)` — the ticket owns its own copy, independent of the Builder.
- In `getTags()`: `Collections.unmodifiableList(tags)` — callers can iterate but can't add or remove. The `UnsupportedOperationException` in `TryIt` proves this works.

---

## 2. The Builder pattern

```java
return new IncidentTicket.Builder(id, reporterEmail, title)
    .priority("MEDIUM")
    .source("CLI")
    .customerVisible(false)
    .tags(tags)
    .build();
```

The key design decision is separating required from optional fields. `id`, `reporterEmail`, and `title` are in the `Builder` constructor itself — you literally cannot instantiate a Builder without them. Everything else is set via fluent methods. Each fluent method returns `this`, enabling the method chain.

This pattern solves the constructor telescoping problem. Without a Builder, you'd need either one massive 10-argument constructor (impossible to call without checking the signature every time) or a family of smaller constructors that overlap and are impossible to maintain. The Builder gives you named, optional parameters in a language that doesn't have them natively.

`toBuilder()` is the "update" mechanism — it creates a fresh Builder pre-populated with every field from the existing ticket, then lets you selectively change what you need before calling `build()`. The original ticket is untouched; you get back a brand new object.

---

## 3. Centralized validation in `build()`

```java
public IncidentTicket build() {
    Validation.requireTicketId(id);
    Validation.requireEmail(reporterEmail, "reporterEmail");
    Validation.requireNonBlank(title, "title");
    Validation.requireOneOf(priority, "priority", "LOW", "MEDIUM", "HIGH", "CRITICAL");
    ...
    return new IncidentTicket(this);
}
```

In the broken starter, validation was scattered — some in `TicketService`, some in constructors, some missing entirely. The fix is a single enforcement point: `build()`. The contract is: if `build()` returns, the object is valid. If anything is wrong, it throws before the object ever exists. An invalid `IncidentTicket` is simply not constructible.

`Validation` is a utility class with a private constructor — it's a namespace for pure functions, not something you instantiate. Each helper has a single responsibility: check one constraint, throw a descriptive `IllegalArgumentException` if it fails. Optional fields like `slaMinutes` use a null guard at the top — `if (value == null) return;` — so they're only validated when actually provided.

---

## 4. `TicketService` — returning new instances instead of mutating

```java
public IncidentTicket escalateToCritical(IncidentTicket t) {
    return t.toBuilder()
            .priority("CRITICAL")
            .addTag("ESCALATED")
            .build();
}
```

The return type changed from `void` to `IncidentTicket`. The old `void` methods were the entire problem — they mutated state in place with no record of what changed. Now each "update" operation produces a new, fully validated, immutable object. The caller decides whether to discard `ticketV1` or keep it around for comparison, audit, or rollback. Both can coexist in memory simultaneously.

---

## The key takeaway

An immutable class is a design commitment, not just `final` fields. You need to close every mutation path: private constructor, no setters, defensive copies on collection inputs *and* outputs. The Builder is what makes this usable — it separates the construction phase (where state is still being assembled) from the operational phase (where the object is sealed). And centralizing validation in `build()` means the invariants are checked exactly once, in exactly one place, every time.
