# MediScan (Wersja Offline) 💊

[![en](https://img.shields.io/badge/lang-en-blue.svg)](README.md)

**MediScan** to inteligentna, działająca w pełni offline aplikacja mobilna na system Android, służąca do zarządzania domową apteczką. Wykorzystuje technologię skanowania kodów kreskowych oraz lokalną bazę danych, aby pomagać użytkownikom w identyfikacji leków, kontrolowaniu stanów magazynowych oraz planowaniu dawkowania.

> Projekt stworzony w architekturze **Offline-First**, zapewniający prywatność danych i działanie bez dostępu do internetu.

---

## 📱 Kluczowe Funkcjonalności

* **Skaner Kodów (EAN):** Błyskawiczna identyfikacja leków przy użyciu kamery (CameraX + ML Kit).
* **Lokalna Baza Danych:** Wszystkie dane (katalog leków, stany magazynowe) przechowywane są na urządzeniu w bazie SQLite (Room).
* **Wirtualna Apteczka:** Zarządzanie ilością posiadanych leków (dodawanie/odejmowanie sztuk).
* **Kalendarz Dawkowania:** Planowanie przyjmowania leków w konkretnych dniach i godzinach z walidacją dostępności leku w apteczce.
* **Dostęp do Ulotek:** Możliwość szybkiego otwarcia cyfrowej ulotki leku (linki zewnętrzne).

---

## 🏗 Architektura i Technologie

Aplikacja została zbudowana jako monolit w oparciu o natywne rozwiązania Androida.

### Tech Stack
* **Język:** Java 17
* **Baza Danych:** Room Database (nakładka na SQLite)
* **Kamera:** Android CameraX
* **Analiza Obrazu:** Google ML Kit (Barcode Scanning)
* **UI:** Material Design Components, XML Layouts
* **Nawigacja:** BottomNavigationView + Fragments

### Struktura Bazy Danych (Entities)
1.  **CatalogMedicine:** Słownik leków (kod EAN, nazwa, postać, link do ulotki).
2.  **UserMedicine:** Leki dodane przez użytkownika do "Mojej Apteczki" (z aktualnym stanem ilościowym).
3.  **ScheduleItem:** Harmonogram przyjmowania dawek powiązany z datą i godziną.

---

## ⚙️ Uruchomienie Projektu

Aby uruchomić projekt lokalnie, potrzebujesz środowiska Android Studio lub IntelliJ IDEA z zainstalowanym Android SDK.

1.  **Sklonuj repozytorium:**
    ```bash
    git clone [https://github.com/kkozlowskiPawel/Project-java-MediScan.git]
    ```
2.  **Otwórz projekt:**
    Wybierz folder `android-app` w swoim IDE.
3.  **Synchronizacja Gradle:**
    Poczekaj, aż IDE pobierze wymagane biblioteki (Room, CameraX, ML Kit).
4.  **Uruchomienie:**
    Podłącz fizyczne urządzenie z Androidem (wymagane do poprawnego działania kamery) lub użyj emulatora z emulacją kamery.
    *Minimum SDK: Android 8.0 (API 26)*

---

## 📄 Licencja

Projekt udostępniony w celach edukacyjnych.
