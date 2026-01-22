# Project Setup & Dependencies

## Overview
This document outlines the initial configuration of the MediScan application, specifically focusing on the required libraries and system permissions necessary for offline operation.

## 📦 Dependencies (Gradle)

### 1. Database: Android Room
* **Purpose:** Local data persistence (Offline-First architecture).
* **Version:** 2.6.1
* **Reason:** Provides an abstraction layer over SQLite, allowing for robust object-mapping and compile-time verification of SQL queries.

### 2. Camera: CameraX
* **Purpose:** Image capture and preview.
* **Version:** 1.3.1
* **Reason:** Lifecycle-aware camera component that ensures consistent behavior across different Android devices (handles fragmentation).

### 3. Machine Learning: Google ML Kit
* **Purpose:** Barcode Scanning.
* **Version:** 17.2.0
* **Reason:** Optimized on-device model for detecting EAN-13 codes without requiring an internet connection.

## 🔐 System Permissions

* `android.permission.CAMERA`: Required to capture video stream for barcode analysis.
* `android.permission.INTERNET`: Used **solely** for opening external links to medication leaflets (PDF/HTML). Ideally, the app operates offline.