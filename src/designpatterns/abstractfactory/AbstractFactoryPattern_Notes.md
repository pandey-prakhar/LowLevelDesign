# Abstract Factory Pattern — Revision Notes

## One-Line Definition

> Provide an interface for creating a **family of related objects** — so all objects are guaranteed to be consistent with each other, and client code never knows which concrete family it's using.

---

## The Problem — Where Factory Alone Breaks Down

You're building a cross-platform UI app (Windows + Mac). You have two components:
- `Button`
- `Checkbox`

And each platform needs its own version:
```
WindowsButton     MacButton
WindowsCheckbox   MacCheckbox
```

**First instinct:** use two separate factories.

```java
Button   btn = ButtonFactory.create("WINDOWS");
Checkbox cb  = CheckboxFactory.create("MAC");    // BROKEN — mixed platforms
```

Nothing enforces consistency. `ButtonFactory` and `CheckboxFactory` don't know about each other. You silently get a Windows button and a Mac checkbox — the UI looks broken.

**Second problem:** `Application` needs to pass the platform string to two separate factories. It now knows the platform in two places. Add Linux tomorrow — edit two factories.

**What you actually need:** One object that says "I am the Windows factory. Everything I create is Windows."

---

## The Fix — Abstract Factory

Define ONE interface with a creation method **for each product in the family**:

```java
public interface UIFactory {
    Button createButton();
    Checkbox createCheckbox();
}
```

Then concrete factories implement the entire interface:

```java
public class WindowsUIFactory implements UIFactory {
    public Button   createButton()   { return new WindowsButton(); }
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
}

public class MacUIFactory implements UIFactory {
    public Button   createButton()   { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}
```

A factory that implements `UIFactory` is **forced** by the compiler to provide all products. You can't make a `WindowsUIFactory` that accidentally returns a `MacCheckbox` — it would have to explicitly do that, which is obvious and intentional.

---

## Code Walkthrough

### The Product Interfaces — what clients depend on

```java
public interface Button   { void render(); void onClick(); }
public interface Checkbox { void render(); void onCheck(); }
```

`Application` only imports these interfaces. It never imports `WindowsButton` or `MacButton`.

### The Concrete Products — one class per platform per product

```java
public class WindowsButton implements Button { ... }
public class MacButton     implements Button { ... }

public class WindowsCheckbox implements Checkbox { ... }
public class MacCheckbox   implements Checkbox { ... }
```

Four classes total. Each is self-contained.

### The Abstract Factory Interface

```java
public interface UIFactory {
    Button createButton();
    Checkbox createCheckbox();
}
```

This is the core of the pattern. One method per product. The interface acts as a **contract**: any factory that implements this must produce a complete, consistent family.

### The Concrete Factories

```java
public class WindowsUIFactory implements UIFactory {
    public Button   createButton()   { return new WindowsButton();   }
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
}
```

`WindowsUIFactory` is the only class that imports both `WindowsButton` and `WindowsCheckbox`. Client code never needs to.

### Application.java — the client, knows nothing about platforms

```java
public class Application {
    private UIFactory factory;

    public Application(UIFactory factory) {
        this.factory = factory;          // receives whichever factory was chosen
    }

    public void buildUI() {
        Button   btn = factory.createButton();
        Checkbox cb  = factory.createCheckbox();
        // guaranteed consistent — both came from the same factory
    }
}
```

`Application` does NOT import `WindowsUIFactory`, `MacUIFactory`, `WindowsButton`, `MacButton`, `WindowsCheckbox`, or `MacCheckbox`. Zero concrete imports. This is the whole win.

### App.java — the only place that knows the platform

```java
UIFactory factory = new WindowsUIFactory();
// UIFactory factory = new MacUIFactory();   // swap this one line for Mac

Application app = new Application(factory);
app.buildUI();
```

ONE line change. Everything else stays identical.

---

## The "Consistency Guarantee" — Why This Matters

With two separate factories, this was possible (and broken):
```java
Button   btn = ButtonFactory.create("WINDOWS");
Checkbox cb  = CheckboxFactory.create("MAC");    // mixed — bad
```

With Abstract Factory, this is impossible without intentional effort:
```java
UIFactory factory = new WindowsUIFactory();
Button   btn = factory.createButton();    // WindowsButton
Checkbox cb  = factory.createCheckbox(); // WindowsCheckbox — guaranteed
```

The factory is ONE object. Both products come from the same object. Consistency is structural, not something you have to remember.

---

## Factory vs Abstract Factory — Side by Side

| | Factory (Simple) | Abstract Factory |
|---|---|---|
| Creates | One type of product | A family of related products |
| Interface looks like | `create(type)` — one method | `createButton()`, `createCheckbox()` — one per product |
| Consistency guarantee | None | Yes — one factory = one family |
| Adding a new product TYPE | N/A | Must add method to interface + all factories (big change) |
| Adding a new FAMILY (e.g., Linux) | Add a case to factory | Add a new concrete factory class (one file) |
| Client coupling | Decoupled from concrete class | Decoupled from entire family of concrete classes |

**The key asymmetry:**
- Adding a new **family** (Linux UI) = easy — one new concrete factory class, zero existing code changes.
- Adding a new **product type** (TextField) = painful — must add to the interface + every existing factory. This is the main tradeoff.

---

## When to Use Abstract Factory

| Situation | Use Abstract Factory? |
|-----------|----------------------|
| Products must be consistent with each other | Yes |
| Switching between "product families" at runtime | Yes |
| Adding new families frequently | Yes (cheap) |
| Adding new product types frequently | No (expensive — changes interface) |
| Only one product type involved | No — plain Factory is enough |

**Real-world signals:**
- "DB objects (connection, query builder, transaction) must all be MySQL or all Postgres"
- "UI components must all match the selected theme"
- "Cloud resources must all be from AWS or all from GCP"
- The word **suite**, **stack**, **theme**, or **provider** in your domain description

---

## SOLID Principles This Pattern Enforces

**Open/Closed Principle**
- Adding a new platform (Linux) = add one new `LinuxUIFactory` class, zero changes to existing factories or `Application`.

**Single Responsibility Principle**
- `WindowsUIFactory` is responsible for creating Windows products only.
- `Application` is responsible for building the UI only — not for knowing which platform.

**Dependency Inversion Principle**
- `Application` depends on `UIFactory` (abstraction), not on `WindowsUIFactory` (concrete).
- `Application` depends on `Button`/`Checkbox` interfaces, not on `WindowsButton`/`MacButton`.

---

## Tradeoffs

### Pros
- Consistency guaranteed — client can never accidentally mix families.
- Adding new families is cheap — one new file.
- Client code is completely decoupled from all concrete classes.

### Cons
- **Hard to add new product types** — adding `TextField` means changing `UIFactory` interface + every existing concrete factory. This is the main pain point.
- More classes than plain Factory — each product × each platform = a class. Two platforms, three products = 6 concrete product classes + 2 factories.
- Overkill if products don't actually need to be consistent with each other.

---

## How Abstract Factory Relates to Other Patterns

**Built from Factory Methods**
Each `createButton()` and `createCheckbox()` inside `UIFactory` is itself a Factory Method. Abstract Factory is a group of Factory Methods organized under one interface to enforce family consistency.

**Often paired with Singleton**
Concrete factories (like `WindowsUIFactory`) usually have no state — you only need one instance. They're often implemented as singletons or have a static instance.

**Abstract Factory vs Builder**
- Builder: constructs ONE complex object step by step.
- Abstract Factory: creates multiple related objects of different types.

---

## Common Interview Questions

**Q: What is Abstract Factory in one sentence?**
> An interface for creating a family of related objects, where a concrete factory guarantees all objects it creates belong to the same consistent family.

**Q: What's the difference between Factory Method and Abstract Factory?**
> Factory Method deals with one product — one creation method, the subclass decides the concrete type. Abstract Factory deals with a family of products — multiple creation methods, one per product type, all returning objects that must be consistent with each other.

**Q: What's the main disadvantage of Abstract Factory?**
> Adding a new product type to the family is expensive — you must modify the abstract factory interface AND all existing concrete factories. Abstract Factory is open for new families, but closed for new product types within a family.

**Q: Why is `Application.java` completely decoupled in this design?**
> Because it only imports interfaces (`UIFactory`, `Button`, `Checkbox`). The concrete classes (`WindowsButton`, `MacUIFactory`) are never imported by `Application` — only by `App.java` (the wiring code) and the factory classes themselves.

**Q: What real-world examples use Abstract Factory?**
> Java's `DocumentBuilderFactory` / `SAXParserFactory` (XML parsing families), `javax.xml.transform.TransformerFactory`, UI toolkit families (AWT/Swing), and database driver families (JDBC — connection, statement, result set all belong to one DB vendor).

---

## One-Line Summary for Interviews

> **Abstract Factory Pattern** groups multiple Factory Methods into one interface so you can create a whole family of related objects and guarantee they're all consistent — swap the factory, swap the entire family, client code untouched.
