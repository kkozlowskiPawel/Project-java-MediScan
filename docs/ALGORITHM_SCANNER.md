# Barcode Recognition Algorithm & Camera Pipeline

## Overview
The scanning module is designed to operate continuously in real-time, capturing video frames from the device's back camera, analyzing them for EAN-13 barcodes, and querying the local database for matches.



## 📷 CameraX Pipeline

The application utilizes the **Jetpack CameraX** library to manage the camera lifecycle and ensure device compatibility.

### 1. Preview Use Case
* **Implementation:** `PreviewView`
* **Function:** Displays a direct feed from the camera sensor to the UI, allowing the user to aim at the medication package.

### 2. ImageAnalysis Use Case
* **Strategy:** `STRATEGY_KEEP_ONLY_LATEST`
* **Reasoning:** Since barcode scanning requires high responsiveness, we discard older frames if the analyzer is busy. This prevents memory overflow and latency (lag).
* **Executor:** Analysis runs on a dedicated background thread (`Executors.newSingleThreadExecutor()`) to keep the UI thread smooth (60fps).

## 🧠 ML Kit Detection Logic

Google's ML Kit (Vision API) is used for on-device machine learning inference.

1.  **Input:** `ImageProxy` (YUV_420_888 format) from the camera is converted to `InputImage`.
2.  **Processing:** The `BarcodeScanner` client scans the image for patterns matching standard 1D formats (EAN-13, EAN-8).
3.  **Result:** Upon successful detection, the raw string value (e.g., "590...") is extracted.

## 🔄 Concurrency & Debouncing

To prevent multiple rapid scans of the same item (which would duplicate database entries):
* A boolean flag `isProcessing` locks the analysis loop immediately after a code is detected.
* The flag is released either after a successful database transaction or after a 2-second timeout (if the code is invalid), allowing the user to scan the next item.