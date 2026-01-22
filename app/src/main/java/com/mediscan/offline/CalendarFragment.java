package com.mediscan.offline;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mediscan.offline.database.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private String selectedDate;
    private RecyclerView rvSchedule;
    // Tutaj należałoby stworzyć prosty adapter dla listy zadań, dla skrótu pominę go w kodzie, ale logika jest niżej

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        Button btnAdd = view.findViewById(R.id.btnAddSchedule);
        rvSchedule = view.findViewById(R.id.rvSchedule);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(new Date());

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Miesiące są indeksowane od 0!
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            selectedDate = sdf.format(c.getTime());
            loadScheduleForDate();
        });

        btnAdd.setOnClickListener(v -> showAddDialog());

        return view;
    }

    private void loadScheduleForDate() {
        // Tu pobieramy ScheduleItems z bazy dla selectedDate i wyświetlamy w RecyclerView
        // Ze względu na limit znaków, skupię się na logice dodawania (wymaganej przez Ciebie)
    }

    private void showAddDialog() {
        // Pobierz listę dostępnych leków
        new Thread(() -> {
            List<UserMedicine> medicines = AppDatabase.getInstance(requireContext()).dao().getAllUserMedicines();
            requireActivity().runOnUiThread(() -> {
                if (medicines.isEmpty()) {
                    Toast.makeText(getContext(), "Najpierw zeskanuj jakieś leki!", Toast.LENGTH_SHORT).show();
                    return;
                }
                buildDialog(medicines);
            });
        }).start();
    }

    private void buildDialog(List<UserMedicine> medicines) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Zaplanuj dawkę na: " + selectedDate);

        View view = getLayoutInflater().inflate(R.layout.dialog_add_schedule, null); // Musisz stworzyć prosty layout
        Spinner spinner = view.findViewById(R.id.spinnerMeds);
        EditText etAmount = view.findViewById(R.id.etAmount);
        TimePicker timePicker = view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        // Adapter do Spinnera (nazwy leków)
        List<String> names = new ArrayList<>();
        for (UserMedicine m : medicines) names.add(m.name + " (Dostępne: " + m.currentQuantity + ")");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(view);
        builder.setPositiveButton("Zapisz", (dialog, which) -> {
            int selectedIndex = spinner.getSelectedItemPosition();
            UserMedicine selectedMed = medicines.get(selectedIndex);

            String amountStr = etAmount.getText().toString();
            if(amountStr.isEmpty()) return;
            int amountToTake = Integer.parseInt(amountStr);

            // Logika sprawdzania dostępności
            if (selectedMed.currentQuantity >= amountToTake) {
                // Zapisz do kalendarza
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String time = String.format("%02d:%02d", hour, minute);

                saveSchedule(selectedMed, amountToTake, time);
            } else {
                Toast.makeText(getContext(), "Za mało tabletek w apteczce! Masz tylko " + selectedMed.currentQuantity, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Anuluj", null);
        builder.show();
    }

    private void saveSchedule(UserMedicine med, int amount, String time) {
        new Thread(() -> {
            // 1. Zmniejsz stan magazynowy
            med.currentQuantity -= amount;
            AppDatabase.getInstance(requireContext()).dao().updateUserMedicine(med);

            // 2. Dodaj wpis do kalendarza
            ScheduleItem item = new ScheduleItem(selectedDate, time, med.name, amount);
            AppDatabase.getInstance(requireContext()).dao().insertSchedule(item);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Zaplanowano!", Toast.LENGTH_SHORT).show();
                loadScheduleForDate();
            });
        }).start();
    }
}