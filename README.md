# MediScan System 💊

**MediScan** is a comprehensive solution for identifying and managing medications using barcode scanning technology. The system consists of a robust backend REST API and a native Android mobile application.

The project is currently in the **MVP (Minimum Viable Product)** phase, focusing on core scanning functionality and database retrieval.

---

## 🏗 Architecture

The system is built using a classic Client-Server architecture:

1.  **Mobile App (Client):** Android (Java) using CameraX and ML Kit for barcode scanning.
2.  **Backend (Server):** Java Spring Boot REST API.
3.  **Database:** MySQL for storing medication details (EAN codes, dosage, leaflets).

![System Schema](https://img.shields.io/badge/Architecture-Client%20Server-blue)

---

## 🚀 Key Features

### Current Functionality (v1.0)
-   **Barcode Scanning:** Uses Google ML Kit to detect EAN-13 codes on medicine packaging.
-   **Instant Identification:** Queries the remote Spring Boot API to retrieve drug details.
-   **Drug Info Display:** Shows name, form, strength, and administration route.
-   **Digital Leaflet:** Allows users to open the official medication leaflet (PDF/HTML) directly from the app.

### Roadmap (Upcoming Features)
-   [ ] **Offline Mode:** Local caching using Room Database (Android) to store scanned history.
-   [ ] **Medication List:** A dedicated dashboard showing user's scan history.
-   [ ] **Pill Calendar:** Local scheduling and reminders for taking medications.
-   [ ] **Cloud Deployment:** Migration from localhost to a VPS (Ubuntu/Azure).

---

## 🛠 Tech Stack

### Backend (API)
* **Language:** Java 17+
* **Framework:** Spring Boot 3 (Web, Data JPA)
* **Database:** MySQL
* **Build Tool:** Maven

### Mobile (Android)
* **Language:** Java
* **Minimum SDK:** Android 8.0 (API 26)
* **Network:** Retrofit 2 + Gson
* **Camera:** CameraX
* **ML:** Google ML Kit (Barcode Scanning)
* **UI:** XML Layouts (Material Design)

---

## ⚙️ Getting Started

To run this project locally, you need to set up both the backend and the mobile app.

### 1. Backend Setup
Prerequisites: JDK 17, MySQL Server.

1.  Clone the repository.
2.  Create a MySQL database named `pharmacy_db`.
3.  Update `src/main/resources/application.properties` with your database credentials:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/pharmacy_db
    spring.datasource.username=YOUR_USER
    spring.datasource.password=YOUR_PASSWORD
    ```
4.  Run the application using Maven or your IDE.
    ```bash
    mvn spring-boot:run
    ```

### 2. Mobile App Setup
Prerequisites: Android Studio or IntelliJ IDEA with Android SDK.

1.  Open the `android-app` directory in your IDE.
2.  Navigate to `RetrofitClient.java` and update the `BASE_URL` with your server's IP address:
    ```java
    // For local emulator use: [http://10.0.2.2:8080/](http://10.0.2.2:8080/)
    // For physical device use your PC's IP: [http://192.168.](http://192.168.)X.X:8080/
    private static final String BASE_URL = "http://YOUR_IP:8080/";
    ```
3.  Build and run the app on your device.

---

## 📄 License

This project is open-source and available for educational purposes.
