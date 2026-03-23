## The problem this solves

The brief was to take a broken, naïve global-state implementation of `MetricsRegistry` and harden it into a **production-grade Singleton** that survives three real-world attacks: concurrency races, reflection abuse, and serialization bypasses.

Let me diagram the overall structure first, then go through each design decision.---

Now let me walk through each design decision as I'd explain it to an instructor.

---

## 1. Lazy initialization with Double-Checked Locking (DCL)

```java
private static volatile MetricsRegistry INSTANCE;

public static MetricsRegistry getInstance() {
    if (INSTANCE == null) {                    // First check — no lock, fast path
        synchronized (MetricsRegistry.class) {
            if (INSTANCE == null) {            // Second check — inside lock
                INSTANCE = new MetricsRegistry();
            }
        }
    }
    return INSTANCE;
}
```

**Why DCL and not just `synchronized`?** A fully synchronized `getInstance()` creates a bottleneck — every call acquires a lock even after the instance is already created. DCL gives you the best of both worlds: the first `null` check is a cheap unsynchronized read (fast path for 99.9% of calls), and the lock is only acquired on the very first construction path.

**Why `volatile`?** This is the subtle part. Object construction in Java involves three steps: allocate memory, initialize fields, assign reference. Without `volatile`, the JVM's memory model allows instruction reordering — another thread could see a non-null `INSTANCE` before its fields are initialized. `volatile` adds a memory barrier that prevents this reordering. Without it, DCL is broken.

---

## 2. Reflection protection

```java
private MetricsRegistry() {
    if (INSTANCE != null) {
        throw new IllegalStateException("Singleton already initialized");
    }
}
```

Reflection can call `setAccessible(true)` on a private constructor and bypass access control entirely. The guard here is: if the constructor is called and an instance already exists, throw immediately. The attacker in `ReflectionAttack.java` calls `ctor.newInstance()` — this triggers the constructor, which sees `INSTANCE != null`, and throws `IllegalStateException` wrapped in an `InvocationTargetException`. The attack fails.

**Limitation to be honest about:** This only works *after* the first instance is created. If the attacker calls the constructor before `getInstance()` has ever been called, the check passes. A stricter approach would be an `enum` singleton, which the JVM itself protects at the bytecode level. But for this exercise, the DCL + constructor guard combination is the expected pattern.

---

## 3. Serialization safety

```java
private Object readResolve() throws ObjectStreamException {
    return getInstance();
}
```

When Java deserializes an object, it bypasses the constructor entirely and creates a brand new instance from the byte stream. Without intervention, `SerializationCheck` would produce a second `MetricsRegistry` object. `readResolve()` is a special hook the serialization framework calls after reconstruction — returning `getInstance()` here forces the deserialized result to be replaced by the canonical singleton. The new object gets GC'd, and the same reference is returned.

---

## 4. Thread-safe business methods

```java
public synchronized void increment(String key) {
    counters.put(key, getCount(key) + 1);
}
```

The counters map itself is a plain `HashMap`, which is not thread-safe. All mutating and reading methods are `synchronized` on `this`, ensuring atomic reads and writes. A `ConcurrentHashMap` with `merge()` or `compute()` would be a more granular and slightly more performant alternative, but `synchronized` methods are correct and easier to reason about.

---

## 5. `MetricsLoader` — using the singleton, not `new`

The fixed `MetricsLoader` calls `MetricsRegistry.getInstance()` instead of `new MetricsRegistry()`. This means loading from `metrics.properties` populates the **same** instance that the rest of the application references. In `App.java`, the identity hash codes of `loaded` and `global` will match — that's the validation that the design works end to end.

---

## How the three checks validate correctness

`ConcurrencyCheck` spawns 80 threads all racing to call `getInstance()` simultaneously. Because of DCL + `volatile`, only one instance is ever created — all 80 threads get the same identity hash code.

`ReflectionAttack` tries `ctor.newInstance()` after the singleton exists — the constructor guard throws and the attack fails with an `InvocationTargetException`.

`SerializationCheck` serializes the instance to bytes, deserializes it, and checks if `a == b`. Because of `readResolve()`, they are the exact same object.

---

**The key takeaway for an LLD interview:** a Singleton is not just "a class with a static field." The three threats, concurrency, reflection, and serialization, each require a distinct, explicit fix. Missing any one of them means you have a *nominally* singleton class that can be broken in practice.
