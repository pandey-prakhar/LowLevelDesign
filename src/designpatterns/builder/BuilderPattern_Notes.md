# Builder Pattern — Revision Notes

## The Problem It Solves

When a class has **many fields** (some required, some optional), you quickly run into these pain points using plain constructors:

1. **Telescoping constructors** — you write `new Student(id, name, age)`, `new Student(id, name, age, gender)`, etc. Ugly and hard to maintain.
2. **JavaBeans (setters) approach** — you call many setters after construction. Object is in an **incomplete/inconsistent state** between calls, and you can't make it immutable.
3. **Too many `null` arguments** — `new Student(1, "Prakhar", 27, null, null, 0, 0)` — which arg is which?

**Builder Pattern fixes all three** — you get readable construction, immutability, and a single place for validation.

---

## Core Idea

> "Separate the construction of a complex object from its representation."

You don't call the `Student` constructor directly. Instead:
1. Get a `Builder` via `Student.getBuilder()`
2. Set only the fields you care about (method chaining)
3. Call `.build()` — which validates and returns the final immutable `Student`

---

## Key Components in This Code

### 1. Private Constructor on the Outer Class
```java
private Student(Builder builder) { ... }
```
- **Why private?** Forces all callers to go through the Builder. No one can do `new Student(...)` directly and bypass validation.

### 2. Static Inner Builder Class
```java
static class Builder { ... }
```
- `static` means it does NOT need a `Student` instance to exist.
- It mirrors all the fields of `Student` so it can accumulate them.

### 3. Setter Methods Return `this` (Method Chaining / Fluent API)
```java
public Builder setName(String name) {
    this.name = name;
    return this;   // <-- key line
}
```
- Returning `this` (the Builder itself) lets you chain: `.setName("X").setAge(27).build()`
- Makes call sites self-documenting — you can see exactly which fields are set.

### 4. Validation in `build()`
```java
public Student build() {
    if (name == null || name.isEmpty()) throw new IllegalArgumentException(...);
    if (age <= 5) throw new IllegalArgumentException(...);
    return new Student(this);
}
```
- All validation lives in ONE place.
- `Student` is only created if it passes — you can never have an invalid `Student` object in your system.

### 5. Factory Method `getBuilder()`
```java
public static Builder getBuilder() {
    return new Builder();
}
```
- A convenience entry point. Some implementations skip this and just do `new Student.Builder()` directly — both are valid.

---

## Calling Code (How it Looks in Practice)

```java
Student st = Student.getBuilder()
        .setName("Prakhar")
        .setGender("M")
        .setAge(27)
        .build();
```
- Reads almost like English — no positional argument guessing.
- Optional fields (like `major`, `psp`) are simply not set — no `null` clutter.

---

## Immutability Note

`Student`'s fields are `private` with **no public setters**. Once built, the object cannot be changed. This is a huge win in multi-threaded environments — no synchronization needed.

---

## When to Use Builder

| Situation | Use Builder? |
|-----------|-------------|
| 4+ constructor params | Yes |
| Many optional fields | Yes |
| Need validation at creation time | Yes |
| 2-3 required fields, nothing optional | Probably overkill |
| Simple POJO / data transfer object | Skip it |

---

## Common Interview Questions

**Q: What's the difference between Builder and Factory pattern?**
> Factory decides *which type* of object to create (hides the concrete class). Builder builds a *single complex object* step by step. They solve different problems.

**Q: Why is the Builder a static nested class and not a separate top-level class?**
> It's logically coupled to `Student` — it exists only to build `Student`. Static nesting keeps them together and lets `Builder` call `Student`'s private constructor, which an outer class could not do.

**Q: What's the difference between `static` nested class and inner class in Java?**
> An inner (non-static) class holds a hidden reference to the enclosing instance. A `static` nested class does not — it's essentially a top-level class scoped inside another. For Builder you always want `static`.

**Q: Why does `setPsp` in your code NOT return `Builder` (it returns `void`)?**
> That's a bug / oversight in the code — it breaks the fluent chain. All setters in a Builder should return `this`.

**Q: Can Builder enforce mandatory fields?**
> Yes — two ways: (1) validate in `build()` and throw, or (2) put required fields in the `Builder`'s constructor so they are forced at creation time: `new Student.Builder(requiredName)`.

**Q: Is Builder thread-safe?**
> The `Builder` itself is typically used locally (not shared), so it doesn't need to be thread-safe. The resulting `Student` object, if immutable (no setters), is inherently thread-safe.

**Q: What real-world Java classes use Builder pattern?**
> `StringBuilder`, `Lombok @Builder`, `AlertDialog.Builder` (Android), `OkHttpClient.Builder`, `HttpRequest.Builder` (Java 11+).

---

## Bug in This Code (Worth Noting)

```java
// In Builder:
public void setPsp(int psp) {   // <-- returns void, breaks chaining!
    this.psp = psp;
}
```
Should be:
```java
public Builder setPsp(int psp) {
    this.psp = psp;
    return this;
}
```

---

## One-Line Summary for Interviews

> **Builder Pattern** creates complex objects step-by-step through a fluent API, keeping the object immutable and valid from the moment it's constructed.
