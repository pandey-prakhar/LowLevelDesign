# Factory Pattern — Revision Notes

## One-Line Definition

> Provide a single place to create objects — so client code never uses `new ConcreteClass()` directly and stays decoupled from concrete types.

---

## The Problem It Solves

You have a notification service with three types: Email, SMS, Push. Without a factory, every caller makes the creation decision themselves:

```java
// OrderService.java — WRONG, this code shouldn't know about EmailNotification
if (user.prefersEmail()) {
    new EmailNotification(user.getEmail()).send("Order confirmed");
} else if (user.prefersSMS()) {
    new SMSNotification(user.getPhone()).send("Order confirmed");
}
```

**Problems:**
1. `OrderService` is tightly coupled to `EmailNotification`, `SMSNotification` — it imports and depends on both. It should only care about *sending* a notification, not about *which* class does it.
2. This if-else will appear in `OrderService`, `PaymentService`, `ShippingService`... tomorrow you add `PushNotification` and you hunt down all of them.
3. **Open/Closed Principle violation** — adding a new notification type means editing every existing service.

---

## The Fix — Hand Creation to a Factory

```java
// Client (OrderService) only knows the interface and the factory
Notification n = NotificationFactory.create(NotificationType.EMAIL, "prakhar@gmail.com");
n.send("Order confirmed");
```

`OrderService` doesn't import `EmailNotification`. It doesn't know it exists. The factory is the only class that knows all the concrete types.

---

## Code Walkthrough

### 1. The Interface — the only thing clients depend on

```java
public interface Notification {
    void send(String message);
}
```

All three concrete classes implement this. Client code always talks to `Notification`, never to `EmailNotification` or `SMSNotification`.

### 2. Concrete Classes — each does one thing

```java
public class EmailNotification implements Notification {
    private String toEmail;
    public void send(String message) {
        System.out.println("Sending EMAIL to " + toEmail + " : " + message);
    }
}
```

Each class is self-contained — its own fields, its own send logic. Nothing bleeds into another.

### 3. The Enum — type-safe way to represent choices

```java
public enum NotificationType { EMAIL, SMS, PUSH }
```

Using an enum instead of a raw String means you can't pass a typo like `"EMAL"`. The compiler catches it.

### 4. The Factory — the single place that knows all concrete types

```java
public class NotificationFactory {
    public static Notification create(NotificationType type, String target) {
        switch (type) {
            case EMAIL: return new EmailNotification(target);
            case SMS:   return new SMSNotification(target);
            case PUSH:  return new PushNotification(target);
            default: throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
```

Key points:
- **Return type is `Notification` (interface)** — caller gets back the interface, not the concrete class. This is the whole point.
- The if-else / switch still exists, but it's in **exactly ONE place**. Adding `WhatsAppNotification` tomorrow = add one `case` here, nothing else changes.
- `static` method — no need to instantiate the factory. It's just a utility.

### 5. App.java — what clean client code looks like

```java
Notification email = NotificationFactory.create(NotificationType.EMAIL, "prakhar@gmail.com");
Notification sms   = NotificationFactory.create(NotificationType.SMS,   "+91-9876543210");
Notification push  = NotificationFactory.create(NotificationType.PUSH,  "device-token-abc123");

email.send("Your order has been confirmed!");
sms.send("Your OTP is 4321");
push.send("Your delivery is 2 stops away");
```

`App.java` imports `Notification`, `NotificationFactory`, `NotificationType` — and nothing else. Zero coupling to concrete classes.

---

## Before vs After — Side by Side

| Without Factory | With Factory |
|---|---|
| Client imports `EmailNotification`, `SMSNotification`, etc. | Client only imports the `Notification` interface |
| if-else scattered across 5 services | if-else in one place: the factory |
| Adding `WhatsApp` → edit 5 files | Adding `WhatsApp` → edit 1 file (factory) + add 1 new class |
| Can't test services without concrete classes | Can mock `Notification` interface in tests |

---

## Simple Factory vs Factory Method (Two Variations)

### Simple Factory (what this code is — very common in practice)
- One class, one static `create()` method.
- The switch/if-else lives inside the factory.
- Not an official GoF pattern, but solves the real problem cleanly.
- **Limitation:** the factory class itself is still tightly coupled to all concrete types. If you add `WhatsApp`, you still edit `NotificationFactory.java`.

### Factory Method Pattern (official GoF — "next level")
- No single factory class with a switch.
- Instead, you define an **abstract `createNotification()` method** in a base class.
- Each subclass overrides it and returns its own concrete type.
- Example: `EmailService` extends `BaseNotificationService` and overrides `createNotification()` → returns `EmailNotification`.
- No switch anywhere. Adding a new type = add a new subclass. Zero edits to existing code.
- **Tradeoff:** more classes, more hierarchy. Use when the type selection is stable and you want zero edits to existing code for new types.

> **For interviews:** Simple Factory is what you'll usually implement. Factory Method is what you name-drop when asked "how would you make it even more open/closed?"

---

## When to Use Factory

| Situation | Use Factory? |
|-----------|-------------|
| Client shouldn't know which concrete class it gets | Yes |
| Object type determined at runtime (config, user input) | Yes |
| Same if-else appearing in multiple places | Yes |
| Want to add new types without editing client code | Yes |
| Only 2 types that will never change | Probably overkill |
| You always create the same one concrete class | Skip it |

---

## SOLID Principles This Pattern Enforces

**Open/Closed Principle (OCP)**
- Adding `WhatsAppNotification` = add one class + one line in the factory. No existing code is modified.

**Single Responsibility Principle (SRP)**
- `OrderService` is responsible for order logic only.
- `NotificationFactory` is responsible for creation logic only.
- Each notification class handles only its own delivery logic.

**Dependency Inversion Principle (DIP)**
- Clients depend on `Notification` (abstraction), not on `EmailNotification` (concrete). The factory is the only place that touches concrete classes.

---

## Tradeoffs

### Pros
- Clients are fully decoupled from concrete classes.
- Adding new types = one place to change (the factory).
- Easy to mock `Notification` interface in unit tests.
- Clean, readable call sites — no `new` or if-else in business logic.

### Cons
- Adds a level of indirection — harder to trace in a debugger where the object actually comes from.
- Simple Factory still has a switch inside it — it's coupled to all types, just centralized.
- Overkill for 2 fixed types that will never grow.

---

## How Factory Relates to Other Patterns

**Factory + Strategy (you already know this)**
- In your `Strategy/withFactory` code, `PaymentStrategyFactory` is a Simple Factory that returns a Strategy. They're often combined: Factory decides *which* Strategy to inject.

**Factory vs Builder**
- Builder: builds *one complex object* step by step, handles optional fields and validation.
- Factory: decides *which type of object* to create. Simple objects, but the *which one* is the decision being hidden.

**Factory Method vs Abstract Factory**
- Factory Method: one product type, one creation method.
- Abstract Factory: a *family* of related products that must be consistent (covered in the Abstract Factory notes).

---

## Common Interview Questions

**Q: What is Factory Pattern in one sentence?**
> It centralizes object creation so that client code depends only on an interface, never on concrete classes — making it easy to add new types without changing existing code.

**Q: What's the difference between Simple Factory and Factory Method?**
> Simple Factory is one class with a static method containing a switch — creation logic is centralized but the factory is still coupled to all types. Factory Method is an abstract method in a base class — each subclass decides what to create, so there's no switch and adding a new type means adding a new subclass with zero edits elsewhere.

**Q: Why use an enum instead of a String for the type?**
> Compile-time safety. `NotificationType.EMAL` is a compile error. `"EMAL"` is a bug that surfaces at runtime.

**Q: How do you unit test code that uses a factory?**
> The client depends on the `Notification` interface. In tests, you can pass a mock `Notification` directly instead of going through the factory. The factory itself is tested separately by asserting it returns the right concrete type.

**Q: Isn't the factory class still tightly coupled to all concrete types?**
> Yes — in Simple Factory, the switch is just centralized. That's the trade-off. Factory Method eliminates this by using polymorphism instead of a switch. But Simple Factory is good enough for most real use cases — changing one file is vastly better than changing ten.

**Q: What real-world frameworks use Factory?**
> `Calendar.getInstance()` (Java), `NumberFormat.getInstance()`, Spring's `BeanFactory`, JDBC's `DriverManager.getConnection()`, `LoggerFactory.getLogger()` (SLF4J).

---

## One-Line Summary for Interviews

> **Factory Pattern** centralizes object creation behind an interface so clients are fully decoupled from concrete classes — adding a new type means editing one file, not ten.
