# Database Schema & Persistence Layer

## Overview
The application utilizes **Android Room** (a SQLite abstraction library) to ensure offline-first capability. The database relies on three core entities representing the catalog, user inventory, and dosage schedule.

## 🗄️ Entity Relationship Description (ERD)

Since SQLite is a relational database, the data is structured into normalized tables.

### 1. CatalogMedicine (`catalog_medicines`)
Acts as a read-only dictionary for identifying scanned barcodes.
* **PK:** `id` (Auto-increment)
* **eanCode:** Unique identifier (barcode) used for lookup.
* **Attributes:** Name, Strength, Form, AdminRoute, LeafletUrl.

### 2. UserMedicine (`user_medicines`)
Represents the user's personal "Virtual Cabinet".
* **PK:** `id` (Auto-increment)
* **Attributes:** Name, Form, Strength, LeafletUrl.
* **State:** `currentQuantity` (Integer tracking the number of pills/units remaining).

### 3. ScheduleItem (`schedule_items`)
Represents a planned event in the calendar.
* **PK:** `id` (Auto-increment)
* **Foreign Key Concept:** Logically linked to `UserMedicine` by name (loose coupling for flexibility).
* **Attributes:** Date (YYYY-MM-DD), Time (HH:MM), MedicineName, Dosage.

## 🛠️ Design Decisions

### Why Room?
* **Compile-time verification:** Raw SQL queries are checked during the build process, preventing runtime crashes due to syntax errors.
* **Main Thread Safety:** Room by default prevents database access on the main UI thread to avoid ANR (Application Not Responding) errors.
* **Singleton Pattern:** The `AppDatabase` class uses a Singleton pattern to prevent multiple expensive connections to the database file.

### Pre-population
The database utilizes a `RoomDatabase.Callback` to inject initial catalog data (sample EAN codes) upon the very first creation of the database file `mediscan_offline.db`.