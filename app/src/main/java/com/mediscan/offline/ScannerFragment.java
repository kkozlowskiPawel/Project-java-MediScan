package com.mediscan.offline;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.mediscan.offline.database.*;
import java.util.concurrent.Executors;

@androidx.camera.core.ExperimentalGetImage
public class ScannerFragment extends Fragment {

    private PreviewView previewView;
    private boolean isProcessing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        previewView = view.findViewById(R.id.previewView);
        startCamera();
        return view;
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> future = ProcessCameraProvider.getInstance(requireContext());
        future.addListener(() -> {
            try {
                ProcessCameraProvider provider = future.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis analysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                analysis.setAnalyzer(Executors.newSingleThreadExecutor(), image -> scanBarcode(image));

                CameraSelector selector = CameraSelector.DEFAULT_BACK_CAMERA;
                provider.unbindAll();
                provider.bindToLifecycle(this, selector, preview, analysis);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void scanBarcode(ImageProxy image) {
        if (isProcessing || image.getImage() == null) {
            image.close();
            return;
        }

        InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());
        BarcodeScanner scanner = BarcodeScanning.getClient();

        scanner.process(inputImage)
                .addOnSuccessListener(barcodes -> {
                    if (!barcodes.isEmpty()) {
                        String code = barcodes.get(0).getRawValue();
                        checkDatabase(code);
                    }
                })
                .addOnCompleteListener(task -> image.close());
    }

    private void checkDatabase(String code) {
        isProcessing = true; // Blokada
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            CatalogMedicine catalogItem = db.dao().findInCatalog(code);

            requireActivity().runOnUiThread(() -> {
                if (catalogItem != null) {
                    // Znaleziono w katalogu -> Dodaj do UserMedicine
                    addToMyCabinet(catalogItem);
                } else {
                    Toast.makeText(getContext(), "Nieznany kod: " + code, Toast.LENGTH_SHORT).show();
                    // Odblokuj po chwili
                    new android.os.Handler().postDelayed(() -> isProcessing = false, 2000);
                }
            });
        }).start();
    }

    private void addToMyCabinet(CatalogMedicine item) {
        new Thread(() -> {
            UserMedicine userMed = new UserMedicine(item.name, item.form, item.strength, item.leafletUrl, 20);
            AppDatabase.getInstance(requireContext()).dao().insertUserMedicine(userMed);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Dodano: " + item.name, Toast.LENGTH_LONG).show();
                ((MainActivity) requireActivity()).navigateToList();
            });
        }).start();
    }
}