# Strategy Pattern — Revision Notes

## One-Line Definition

> Define a **family of algorithms**, encapsulate each one, and make them **interchangeable at runtime** without changing the object that uses them.

---

## The Problem It Solves

Imagine you're building an e-commerce checkout page. Users can pay via UPI, Credit Card, Net Banking, or Wallet. The naive approach is:

```java
public void checkout(String paymentType) {
    if (paymentType.equals("UPI")) {
        // UPI logic
    } else if (paymentType.equals("CREDIT_CARD")) {
        // Credit card logic
    } else if (paymentType.equals("NET_BANKING")) {
        // Net banking logic
    }
    // Adding PayPal tomorrow? Edit this method again.
}
```

**Problems with this:**
1. **Open/Closed Principle (OCP) violation** — every new payment method means editing existing, working code. You risk breaking something that already works.
2. **Single Responsibility Principle (SRP) violation** — one method now knows about every payment system in the world.
3. **Testing nightmare** — you can't test UPI logic in isolation; you have to bring the whole `checkout` method.
4. **Runtime inflexibility** — you cannot swap the algorithm after the object is created.

Strategy Pattern fixes all of this cleanly.

---

## How the Solution Works

You extract each "varying behavior" (payment logic) into its own class, all behind a **common interface**. The context class (`ShoppingCart`) holds a reference to the interface — not to any concrete implementation.

```
ShoppingCart ──── has-a ──── PaymentStrategy (interface)
                                   ▲
                    ┌──────────────┼──────────────┐
             CreditCardPayment  UPIPayment  NetBankingPayment
```

The cart doesn't know or care which payment class it holds. It just calls `paymentStrategy.pay(amount)`. The right behavior runs automatically — this is **polymorphism doing its job**.

---

## Code Walkthrough

### The Interface — the Contract
```java
public interface PaymentStrategy {
    void pay(double amount);
}
```
This is the only thing `ShoppingCart` depends on. All concrete strategies must honor this contract.

### A Concrete Strategy
```java
public class UPIPayment implements PaymentStrategy {
    private String upiId;
    public void pay(double amount) {
        System.out.println("Paid Rs." + amount + " via UPI to " + upiId);
    }
}
```
Each strategy is a **self-contained class** — it has its own fields (upiId, cardNumber, etc.) and its own logic. Nothing bleeds into another.

### The Context Class
```java
public class ShoppingCart {
    private PaymentStrategy paymentStrategy;  // holds reference to interface

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;       // swappable at runtime
    }

    public void checkout() {
        paymentStrategy.pay(totalAmount);      // delegates — doesn't execute itself
    }
}
```
Key insight: `ShoppingCart` **delegates** the payment work. It doesn't do it itself. This is the Strategy pattern's core mechanic.

### Runtime Swapping
```java
cart.setPaymentStrategy(new UPIPayment("prakhar@upi"));
cart.checkout();  // UPI logic runs

cart.setPaymentStrategy(new CreditCardPayment(...));
cart.checkout();  // Credit card logic runs — same cart, different behavior
```
The same object behaves differently depending on which strategy is injected. No `if-else` anywhere.

---

## Real-World Examples

| Domain | Context | Strategies |
|--------|---------|------------|
| E-commerce | `ShoppingCart` | UPI, Credit Card, Wallet, COD |
| Google Maps | `Navigator` | Fastest route, Shortest route, Avoid tolls, Walking |
| Sorting libraries | `Sorter` | BubbleSort, MergeSort, QuickSort |
| Compression tools | `FileCompressor` | ZIP, RAR, 7z, GZIP |
| Game characters | `Fighter` | AggressiveAttack, DefensiveAttack, RangedAttack |
| Logging frameworks | `Logger` | ConsoleLogger, FileLogger, CloudLogger |
| Discount systems | `PriceCalculator` | SeasonalDiscount, MemberDiscount, CouponDiscount |

**The pattern appears wherever you have:** "do X, but the *way* you do X can vary."

---

## SOLID Principles This Pattern Enforces

**Open/Closed Principle (OCP)** — most important one here.
- `ShoppingCart` is **closed for modification** — you never touch it when adding a new payment method.
- The system is **open for extension** — just add a new class implementing `PaymentStrategy`.

**Single Responsibility Principle (SRP)**
- `ShoppingCart` handles cart logic only.
- `CreditCardPayment` handles only credit card logic.
- No class has more than one reason to change.

**Dependency Inversion Principle (DIP)**
- `ShoppingCart` depends on the **abstraction** (`PaymentStrategy` interface), not on concrete classes.
- This is why you can swap strategies without recompiling `ShoppingCart`.

---

## Strategy vs If-Else — Side-by-Side

| | If-Else Approach | Strategy Pattern |
|---|---|---|
| Add new payment method | Edit existing method | Add new class, zero edits elsewhere |
| Test one payment type | Hard, whole method involved | Instantiate one class, test it alone |
| Runtime swap | Possible but messy | Built-in via `setPaymentStrategy()` |
| Code size | Grows linearly in one file | Distributed across files, each small |
| Risk of regression | High | Low — existing classes untouched |

---

## Tradeoffs

### Pros
- Clean separation of concerns — each algorithm lives in its own class.
- Adding new strategies = adding new files, not editing old ones (OCP).
- Strategies are independently testable.
- Runtime flexibility — behavior can change without recreating the object.

### Cons
- **Class explosion** — if you have 10 payment methods, that's 10 classes. For very simple cases, `if-else` might genuinely be cleaner.
- **Client must know strategies exist** — the caller (App.java) has to decide which strategy to inject. This can be pushed to a Factory, but that adds another layer.
- **Stateless vs stateful** — strategies work best when they are stateless or carry only their own config (like a UPI ID). If strategies need to share state with the context, it gets messy.
- **Overkill for stable behavior** — if the algorithm never changes (e.g., tax calculation always works one way), you don't need Strategy. Pattern has a cost.

---

## How Strategy Relates to Other Patterns

**Strategy vs Template Method**
- Both define a "skeleton" with variable parts.
- Template Method: variation through **inheritance** (subclass overrides steps).
- Strategy: variation through **composition** (inject a different object).
- Prefer Strategy (composition > inheritance) — more flexible, testable, and you can change at runtime.

**Strategy vs State**
- Both look identical in structure (context holds a reference to an interface, delegates to it).
- Strategy: the context usually doesn't switch strategies on its own — the *client* controls which strategy is used.
- State: the context *itself* switches states based on internal conditions (e.g., a traffic light cycling through Red → Green → Yellow automatically).
- The intent is different — Strategy is about *interchangeable algorithms*, State is about *lifecycle transitions*.

**Strategy vs Factory**
- Factory creates objects; Strategy defines behavior.
- They're often used together: a Factory decides *which* Strategy to inject.

---

## Common Interview Questions

**Q: What is the Strategy Pattern in one sentence?**
> It lets you define a family of algorithms, put each in its own class, and make them swappable at runtime via a common interface.

**Q: What design principles does Strategy enforce?**
> OCP (open for extension, closed for modification), SRP (each strategy does one thing), and DIP (depend on abstraction, not concrete classes).

**Q: When should you NOT use Strategy?**
> When the behavior never changes, or when you only have 2 simple variations — a plain `if-else` is more readable and has no overhead. Don't over-engineer.

**Q: What's the difference between Strategy and Template Method?**
> Strategy uses composition (inject the behavior as an object). Template Method uses inheritance (subclass overrides the steps). Strategy is generally preferred because it's more flexible — you can change behavior at runtime and combine strategies freely.

**Q: How would you make the Strategy selection cleaner in production?**
> Use a Factory or a Map to resolve the strategy:
> ```java
> Map<String, PaymentStrategy> strategies = new HashMap<>();
> strategies.put("UPI", new UPIPayment("prakhar@upi"));
> strategies.put("CREDIT_CARD", new CreditCardPayment(...));
> 
> strategies.get(userSelectedMethod).pay(amount);
> ```
> This eliminates even the `if-else` in the client code entirely.

**Q: What's the difference between Strategy and State patterns?**
> Structure is almost identical. The difference is *intent and who controls the switch*. In Strategy, the client injects the algorithm. In State, the object itself transitions between states internally based on its own logic.

---

## One-Line Summary for Interviews

> **Strategy Pattern** lets you swap algorithms (behaviors) at runtime by coding to an interface, eliminating if-else chains and making your system open for extension without touching existing code.
