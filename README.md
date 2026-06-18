# Design Patterns — Java

Learning and implementing Gang of Four (GoF) design patterns in Java. Each pattern has working code and revision notes.

---

## Patterns Covered

| Pattern | Type | Notes |
|---------|------|-------|
| Singleton | Creational | `singleton/` |
| Builder | Creational | `builder/BuilderPattern_Notes.md` |
| Factory | Creational | `factory/FactoryPattern_Notes.md` |
| Abstract Factory | Creational | `abstractfactory/AbstractFactoryPattern_Notes.md` |
| Strategy | Behavioral | `Strategy/basic/StrategyPattern_Notes.md` |
| Observer | Behavioral | `observer/ObserverPattern_Notes.md` |

---

## Structure

```
src/designpatterns/
├── singleton/          — Singleton pattern (multiple versions)
├── builder/            — Builder pattern
├── factory/            — Factory pattern (Simple Factory)
├── abstractfactory/    — Abstract Factory pattern
├── Strategy/
│   ├── basic/          — Strategy pattern
│   ├── withFactory/    — Strategy + Factory combined
│   └── selfImplementation/
└── observer/           — Observer pattern
```

---

## Setup

- Language: Java
- IDE: IntelliJ IDEA
- No external dependencies
