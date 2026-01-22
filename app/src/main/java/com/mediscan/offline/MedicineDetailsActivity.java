package com.mediscan.offline;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mediscan.offline.database.AppDatabase;
import com.mediscan.offline.database.UserMedicine;

public class MedicineDetailsActivity extends AppCompatActivity {

    private UserMedicine medicine;
    private TextView tvStock;
    private int currentStockValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);

        medicine = (UserMedicine) getIntent().getSerializableExtra("MED_ID");
        if (medicine == null) finish();

        TextView name = findViewById(R.id.tvDetailName);
        TextView strength = findViewById(R.id.tvDetailStrength);
        tvStock = findViewById(R.id.tvCurrentStock);
        Button btnLeaflet = findViewById(R.id.btnLeaflet);
        SeekBar seekBar = findViewById(R.id.seekBarStock);
        Button btnPlus = findViewById(R.id.btnPlus);
        Button btnMinus = findViewById(R.id.btnMinus);

        name.setText(medicine.name);
        strength.setText("Moc: " + medicine.strength + " | " + medicine.form);
        currentStockValue = medicine.currentQuantity;
        updateStockUI();

        seekBar.setProgress(currentStockValue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentStockValue = progress;
                updateStockUI();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { saveStock(); }
        });

        btnPlus.setOnClickListener(v -> {
            if (currentStockValue < 100) {
                currentStockValue++;
                seekBar.setProgress(currentStockValue);
                saveStock();
            }
        });

        btnMinus.setOnClickListener(v -> {
            if (currentStockValue > 0) {
                currentStockValue--;
                seekBar.setProgress(currentStockValue);
                saveStock();
            }
        });

        btnLeaflet.setOnClickListener(v -> {
            if (medicine.leafletUrl != null && !medicine.leafletUrl.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(medicine.leafletUrl));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, "Brak linku do ulotki", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStockUI() {
        tvStock.setText(String.valueOf(currentStockValue));
    }

    private void saveStock() {
        medicine.currentQuantity = currentStockValue;
        new Thread(() -> {
            AppDatabase.getInstance(this).dao().updateUserMedicine(medicine);
        }).start();
    }
}