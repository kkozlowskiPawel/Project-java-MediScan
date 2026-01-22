# MediScan (Offline Edition) 💊

[![pl](https://img.shields.io/badge/lang-pl-red.svg)](README.pl.md)

**MediScan** is a fully offline, native Android application designed for home medication management. It utilizes barcode scanning technology and a local database to help users identify medications, track inventory, and schedule dosages.

> Built with an **Offline-First** architecture, ensuring data privacy and functionality without an internet connection.

---

## 📱 Key Features

* **Barcode Scanner (EAN):** Instant medication identification using the camera (CameraX + ML Kit).
* **Local Database:** All data (drug catalog, inventory, schedule) is stored locally on the device using SQLite (Room).
* **Virtual Medicine Cabinet:** Manage your home inventory (add/remove pills).
* **Dosage Scheduler:** Plan medication intake for specific dates and times, with validation against available stock.
* **Leaflet Access:** Quick access to digital medication leaflets (external links).

---

## 🏗 Architecture & Tech Stack

The application follows a monolithic architecture based on native Android components.

### Tech Stack
* **Language:** Java 17
* **Database:** Room Database (SQLite abstraction)
* **Camera:** Android CameraX
* **Image Analysis:** Google ML Kit (Barcode Scanning)
* **UI:** Material Design Components, XML Layouts
* **Navigation:** BottomNavigationView + Fragments

### Database Entities
1.  **CatalogMedicine:** Dictionary of medications (EAN code, name, form, leaflet link).
2.  **UserMedicine:** Medications added to "My Cabinet" (with current quantity).
3.  **ScheduleItem:** Dosage schedule linked to date and time.

---

## ⚙️ Getting Started

To run this project locally, you need Android Studio or IntelliJ IDEA with the Android SDK installed.

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/kkozlowskiPawel/Project-java-MediScan.git]
    ```
2.  **Open the project:**
    Select the `android-app` directory in your IDE.
3.  **Sync Gradle:**
    Wait for the IDE to download necessary dependencies (Room, CameraX, ML Kit).
4.  **Run:**
    Connect a physical Android device (recommended for camera testing) or use an emulator.
    *Minimum SDK: Android 8.0 (API 26)*

---

## 📄 License

This project is open-source and available for educational purposes.