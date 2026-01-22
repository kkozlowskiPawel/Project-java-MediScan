# UI/UX Design & Navigation Flow

## Overview
The application interface is built using standard Android **Material Design** components to ensure familiarity and ease of use. The navigation follows a single-activity architecture with a bottom navigation bar switching between three main fragments.

## 📱 Screen Hierarchy

### 1. Main Dashboard (Navigation Shell)
* **Component:** `MainActivity`
* **Layout:** `RelativeLayout` containing a `FrameLayout` (for fragments) and `BottomNavigationView`.
* **Navigation Graph:**
    * Tab 1: **Scanner** (Default)
    * Tab 2: **My Cabinet** (List)
    * Tab 3: **Calendar** (Schedule)

### 2. Scanner Screen (`ScannerFragment`)
* **Goal:** Minimalist interface focusing on the camera preview.
* **Elements:**
    * Full-screen `PreviewView` (CameraX).
    * Semi-transparent overlay with a "cutout" guide frame to direct user focus.
    * Text hint instructing the user.

### 3. Inventory List (`ListFragment`)
* **Goal:** Clear overview of owned medications.
* **Elements:**
    * Search bar (EditText) at the top for real-time filtering.
    * `RecyclerView` displaying cards (`CardView`) for each medication.
    * **Card Details:** Medicine name, form (pill/syrup), and current quantity highlighted in blue.

### 4. Medicine Details (`MedicineDetailsActivity`)
* **Goal:** Detailed management of a specific item.
* **Elements:**
    * **Stock Control:** A `SeekBar` and `+/-` buttons to easily adjust the quantity (Inventory management).
    * **Leaflet Button:** Direct link to external PDF resources.

### 5. Calendar (`CalendarFragment`)
* **Goal:** Dosage planning.
* **Elements:**
    * Interactive `CalendarView`.
    * "Add Dose" button triggering a dialog.
    * List of planned doses for the selected day.