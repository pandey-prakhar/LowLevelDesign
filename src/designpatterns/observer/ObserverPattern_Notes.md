# Observer Pattern — Interview Notes

---

## What is it?

A **behavioural** design pattern where one object (the **Publisher / Subject**) maintains a list of dependents (**Observers**) and automatically notifies them when its state changes.

> One-liner for interviews: *"Publisher notifies all subscribers whenever something interesting happens — without knowing who they are."*

---

## The Problem it Solves

Imagine Flipkart's order system. When an order status changes, you need to:
- Generate an invoice
- Update inventory
- Send an SMS/email
- Credit loyalty points

**Without Observer:** The `OrderService` would directly call `InvoiceService`, `InventoryService`, etc. — tightly coupled, hard to extend.

**With Observer:** `OrderPublisher` just says *"status changed"* and fires. Each service decides what to do on its own. Adding a new service (e.g. `FraudDetectionService`) = zero changes to existing code.

---

## Key Participants

| Role | This Example | Responsibility |
|---|---|---|
| **Observer (interface)** | `OrderObserver` | Defines `onOrderUpdate(Order)` |
| **Publisher (Subject)** | `OrderPublisher` | Maintains list, calls notify |
| **Concrete Observer** | `InvoiceService`, etc. | Reacts to the event |
| **Data/Event** | `Order` | Carries the state being shared |

---

## Code Skeleton (what to write in an interview)

```java
// 1. Observer interface
interface Observer {
    void update(EventData data);
}

// 2. Publisher
class Publisher {
    private List<Observer> observers = new ArrayList<>();

    public void subscribe(Observer o)   { observers.add(o); }
    public void unsubscribe(Observer o) { observers.remove(o); }

    private void notifyAll(EventData data) {
        for (Observer o : observers) o.update(data);
    }

    public void changeState(EventData data) {
        // ... mutate state ...
        notifyAll(data);
    }
}

// 3. Concrete observer
class ConcreteService implements Observer {
    @Override
    public void update(EventData data) {
        // react only to relevant events
    }
}
```

---

## Real-World Examples (drop these in interviews)

| Context | Publisher | Observers |
|---|---|---|
| Flipkart order | OrderPublisher | Invoice, Inventory, Notification, Loyalty |
| YouTube | Channel | Subscribers |
| Stock market | StockTicker | Trading bots, display panels |
| UI framework | Button (click event) | onClick listeners |
| Java stdlib | `java.util.Observable` (deprecated) | `java.util.Observer` |
| Spring | `ApplicationEventPublisher` | `@EventListener` methods |

---

## Push vs Pull Model

| | Push | Pull |
|---|---|---|
| **What** | Publisher sends full data to observer | Publisher sends minimal signal; observer fetches what it needs |
| **This example** | Yes — we pass the whole `Order` object | — |
| **Pros** | Simple, observers get everything | Observer controls what it reads; publisher stays lean |
| **Cons** | Observers get data they may not need | Extra coupling — observer needs reference to publisher |

**Interview tip:** Mention both, say Push is simpler and usually preferred unless observers have very different data needs.

---

## When to Use

- One change in one object requires changing N others, and you don't know N in advance.
- You want loose coupling — publisher shouldn't know concrete observer types.
- Event-driven systems: order events, UI events, messaging, pub-sub queues.

---

## When NOT to Use

- If the number of observers is always fixed and small — direct calls are simpler.
- If observers have complex dependencies on *each other's* order of execution — Observer doesn't guarantee order well.
- Don't use it just because something "might" need listeners in the future — YAGNI.

---

## Limitations / Gotchas

### 1. Memory Leaks
If observers are never unsubscribed, the publisher holds a reference → GC can't collect them.
```java
// Always pair subscribe with unsubscribe when observer is done
publisher.unsubscribe(loyaltyService);
```

### 2. Unexpected Notification Chains
Observer A's `update()` triggers a state change on the publisher → notifies Observer B → cascading calls. Very hard to debug.

### 3. No Guaranteed Order
Observers are notified in list insertion order. If `LoyaltyService` must run *after* `InvoiceService`, you have to manage that ordering manually — Observer doesn't help.

### 4. Performance at Scale
Notifying 10,000 observers synchronously will block. For high-scale systems use an **async event bus** (e.g., Kafka, RabbitMQ, Spring's `@Async` events) instead.

### 5. Observers Don't Know About Each Other
This is usually a feature, but sometimes an observer needs to know "did all other observers succeed?" — Observer pattern can't answer that without extra wiring.

---

## Observer vs Related Patterns

| Pattern | How it differs |
|---|---|
| **Mediator** | Mediator sits in the middle and coordinates — observers talk *through* mediator. Observer = direct broadcast. |
| **Event Bus / Pub-Sub** | Decoupled further — publisher and subscriber don't even hold references to each other; a bus sits in between. Observer is simpler. |
| **Chain of Responsibility** | Request passes down a chain *sequentially* until handled. Observer = broadcast to ALL. |
| **Strategy** | Swaps algorithm at runtime. Observer = notify listeners of state change. |

---

## OCP Compliance

Adding a new service (e.g., `FraudDetectionService`) requires:
- Create new class implementing `OrderObserver` ✅
- Register it: `publisher.subscribe(new FraudDetectionService())` ✅
- Zero changes to `OrderPublisher` or any other service ✅

This is the **Open/Closed Principle** in action.

---

## Quick Revision Checklist

- [ ] Publisher holds a `List<Observer>` — not concrete types
- [ ] `subscribe()` / `unsubscribe()` on the publisher
- [ ] `notifyAll()` is private — called internally after state change
- [ ] Observer interface has exactly one method (update/onEvent)
- [ ] Concrete observers filter events they care about (by status, type, etc.)
- [ ] Mention memory leak risk and async alternative in interviews
