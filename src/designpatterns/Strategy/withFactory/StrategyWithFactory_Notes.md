# Strategy + Factory Pattern — Revision Notes

> Read `basic/StrategyPattern_Notes.md` first. This doc covers only what Factory adds on top.

---

## The Gap That Remains After Pure Strategy

In the `basic` version, the client (App.java) still does this:

```java
cart.setPaymentStrategy(new UPIPayment("prakhar@upi"));   // client knows UPIPayment exists
cart.setPaymentStrategy(new CreditCardPayment("411...", "Prakhar")); // client knows CreditCardPayment exists
```

The `ShoppingCart` is decoupled — it only sees `PaymentStrategy`. But the **client** is still tightly coupled to every concrete class. It has to `import` each one. If you rename `UPIPayment` to `UpiPaymentGateway`, every caller breaks.

This is the gap Factory fills.

---

## What Factory Adds

Factory is the **object creation expert**. You tell it *what* you want via an enum or string key, and it returns the right object — you never touch a constructor.

```
Before (basic):     App  ──── new UPIPayment(...)
                    App  ──── new CreditCardPayment(...)

After (withFactory): App  ──── PaymentStrategyFactory.getStrategy(PaymentType.UPI)
                              └──── map lookup ──── UPIPayment
```

The client now depends only on:
- `PaymentType` (enum) — just a label
- `PaymentStrategyFactory` — the lookup gate
- `PaymentStrategy` (interface) — the result type

It has **zero dependency on any concrete payment class**.

---

## The Map-Based Factory Explained

```java
private static final Map<PaymentType, PaymentStrategy> STRATEGIES = new EnumMap<>(PaymentType.class);

static {
    STRATEGIES.put(PaymentType.UPI,        new UPIPayment("prakhar@upi"));
    STRATEGIES.put(PaymentType.CREDIT_CARD, new CreditCardPayment("4111...", "Prakhar Pandey"));
    STRATEGIES.put(PaymentType.NET_BANKING, new NetBankingPayment("HDFC", "ACC123456"));
}

public static PaymentStrategy getStrategy(PaymentType type) {
    PaymentStrategy strategy = STRATEGIES.get(type);
    if (strategy == null) throw new IllegalArgumentException("No strategy for: " + type);
    return strategy;
}
```

**Why `EnumMap` over `HashMap`?**
- `EnumMap` is backed by an array indexed by enum ordinal — O(1) lookup, zero hash collisions, more memory efficient than HashMap for enum keys. Always prefer it when keys are an enum.

**Why `static` block?**
- The map is initialized once when the class loads. All strategies are ready before the first call to `getStrategy()`.

**Why `null` check + throw instead of returning null?**
- Returning `null` would let the bug silently travel to `paymentStrategy.pay()` and explode there as a NullPointerException, far from the root cause. Failing loudly at the factory is always better.

---

## The `register()` Method — True OCP

```java
public static void register(PaymentType type, PaymentStrategy strategy) {
    STRATEGIES.put(type, strategy);
}
```

This is how you add a new payment method in production **without touching a single existing file**:
1. Create `WalletPayment implements PaymentStrategy` (new file)
2. Add `WALLET` to the `PaymentType` enum
3. Call `PaymentStrategyFactory.register(PaymentType.WALLET, new WalletPayment(...))` at startup

No edits to `ShoppingCart`, no edits to other strategies, no edits to the factory's core logic. This is Open/Closed Principle at its purest.

---

## Two Ways to Set Strategy — Now in `ShoppingCart`

```java
// Option 1: pass enum — ShoppingCart uses factory internally
cart.setPaymentType(PaymentType.UPI);

// Option 2: pass strategy object directly — pure Strategy pattern, factory bypassed
cart.setPaymentStrategy(new UPIPayment("custom@upi"));
```

Having both is intentional:
- `setPaymentType` is the 99% path — clean, enum-driven, no concrete class knowledge needed.
- `setPaymentStrategy` is the escape hatch — for tests (inject a mock), for one-off custom strategies, for anything the factory doesn't cover yet.

---

## How the Two Patterns Split Responsibilities

| Responsibility | Owned by |
|---|---|
| "How do I pay via UPI?" | `UPIPayment` (Strategy) |
| "Which strategy object do I use?" | `PaymentStrategyFactory` (Factory) |
| "When and how much do I pay?" | `ShoppingCart` (Context) |
| "Which payment type does the user want?" | `App` / caller |

Each class has exactly one reason to change. This is textbook SRP.

---

## Visual: How a Checkout Call Flows

```
App
 │  cart.setPaymentType(PaymentType.UPI)
 ▼
ShoppingCart.setPaymentType()
 │  PaymentStrategyFactory.getStrategy(UPI)
 ▼
PaymentStrategyFactory
 │  STRATEGIES.get(UPI)  →  UPIPayment instance
 └──────────────────────────────────────────▶ returns UPIPayment
 ▲
ShoppingCart stores UPIPayment as PaymentStrategy

App
 │  cart.checkout()
 ▼
ShoppingCart.checkout()
 │  paymentStrategy.pay(79999.0)    ← only knows interface
 ▼
UPIPayment.pay()
 │  prints "Paid Rs.79999.0 via UPI to prakhar@upi"
```

---

## Tradeoffs of Adding Factory

### New Pros
- Client is fully decoupled from concrete strategy classes — zero imports of `UPIPayment` etc. in `App.java`.
- Adding a new strategy = new file + one-line registration, zero edits to caller code.
- Centralized creation logic — all `new XPayment(...)` calls live in one place.

### New Cons
- **Extra indirection** — more files, more layers. For a small system with 2-3 strategies, this is overkill.
- **Static state** — the map is a static singleton. In tests, if one test registers a mock strategy, it bleeds into other tests unless you reset it. (Fix: make the factory instance-based, inject it.)
- **Enum coupling** — every new payment type needs the `PaymentType` enum updated. The enum and the factory are now two places that must stay in sync.

---

## Interview Questions Specific to This Version

**Q: Why do you need both Strategy and Factory? Isn't Strategy enough?**
> Strategy decouples the *context* from concrete algorithms. Factory decouples the *caller/client* from concrete algorithm classes. They fix two different coupling problems. Strategy alone leaves the client still doing `new CreditCardPayment(...)`. Factory removes that last coupling.

**Q: What's the difference between Factory Method pattern and this Map-based factory?**
> Factory Method (GoF) uses inheritance — a subclass overrides a method to decide what to create. This Map-based factory is closer to a **Registry** or **Simple Factory** — it's a static lookup, no inheritance involved. In interviews, clarify which variant you're implementing. Simple/Registry factory is more practical and common in real codebases.

**Q: What happens if someone adds a new `PaymentType` enum value but forgets to register it in the factory?**
> The `getStrategy()` null check throws `IllegalArgumentException` at runtime. To catch this at compile time, you'd need to use a `switch` with a full `case` per enum value — the compiler warns on missing cases. Trade-off: switch is less flexible (can't register at runtime), but safer at compile time.

**Q: How would you make this factory testable / injectable?**
> Make it non-static — turn it into a class you instantiate and inject:
> ```java
> public class ShoppingCart {
>     private PaymentStrategyFactory factory;
>     public ShoppingCart(..., PaymentStrategyFactory factory) {
>         this.factory = factory;
>     }
> }
> ```
> Now in tests you inject a mock factory that returns mock strategies. Static factories are the enemy of unit testing.

**Q: Where does this pattern appear in real frameworks?**
> - Spring's `BeanFactory` — a map of bean names to instances, resolved at runtime.
> - Java's `ServiceLoader` — registry of interface implementations.
> - JDBC's `DriverManager` — registers drivers by string key, returns the right one.
> - OkHttp's interceptor chain — strategies chained via a list, selected at request time.

---

## One-Line Summary for Interviews

> **Strategy + Factory** together achieve full decoupling: Strategy removes the context's dependency on concrete algorithms, and Factory removes the client's dependency on concrete algorithm classes — the client only ever sees enums and interfaces.
