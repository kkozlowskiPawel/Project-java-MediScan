# Business Logic & Algorithms

## Overview
This document details the core logic handling user inventory management and the safety checks implemented within the dosage scheduling system.

## 💊 Inventory Management System

The application serves as a **CRUD** (Create, Read, Update, Delete) interface for the local SQLite database.

### Filtering Algorithm (`ListFragment`)
* **Mechanism:** Linear Search.
* **Trigger:** `TextWatcher` on the search bar input.
* **Logic:** Iterates through the cached `fullList` of medicines. If the medicine name contains the query substring (case-insensitive), it is added to a temporary filtered list which then updates the RecyclerView adapter.

## 📅 Scheduling & Stock Validation

One of the key safety features of MediScan is the prevention of scheduling doses that exceed available supply.

### The "Check-then-Commit" Transaction
When a user attempts to schedule a dose in the calendar:
1.  **Retrieve:** The app pulls the current stock level (`currentQuantity`) for the selected medicine.
2.  **Validate:** `if (currentQuantity >= amountToTake)`
    * **Pass:** Proceed to step 3.
    * **Fail:** Block the action and display a Toast warning ("Not enough stock").
3.  **Atomic Update (Simulated):**
    * Deduct the dosage from `UserMedicine` table.
    * Insert a new record into `ScheduleItem` table.
    * *Note:* Both operations are performed sequentially on a background thread to ensure data consistency.

## 🧵 Thread Management
Database operations (Room) are blocking I/O operations. To prevent the UI from freezing (ANR), all database calls are wrapped in:
`new Thread(() -> { ... }).start();`
UI updates post-transaction are dispatched back using `requireActivity().runOnUiThread()`.