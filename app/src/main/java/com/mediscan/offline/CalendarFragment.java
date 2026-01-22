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
    private RecyclerView rvSchedule;
    private ScheduleAdapter adapter;
    private String selectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        rvSchedule = view.findViewById(R.id.rvSchedule);
        Button btnAdd = view.findViewById(R.id.btnAddSchedule);

        rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ScheduleAdapter(new ArrayList<>());
        rvSchedule.setAdapter(adapter);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(new Date());

        loadScheduleForDate();

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            selectedDate = sdf.format(c.getTime());
            loadScheduleForDate();
        });

        btnAdd.setOnClickListener(v -> showAddDialog());

        return view;
    }

    private void loadScheduleForDate() {
        new Thread(() -> {
            List<ScheduleItem> items = AppDatabase.getInstance(requireContext()).dao().getScheduleForDate(selectedDate);
            requireActivity().runOnUiThread(() -> {
                if (adapter != null) {
                    adapter.updateList(items);
                }
            });
        }).start();
    }

    private void showAddDialog() {
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
        builder.setTitle("Zaplanuj dawkę: " + selectedDate);

        View view = getLayoutInflater().inflate(R.layout.dialog_add_schedule, null);
        Spinner spinner = view.findViewById(R.id.spinnerMeds);
        EditText etAmount = view.findViewById(R.id.etAmount);
        TimePicker timePicker = view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        List<String> names = new ArrayList<>();
        for (UserMedicine m : medicines) names.add(m.name + " (Dostępne: " + m.currentQuantity + ")");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(view);
        builder.setPositiveButton("Zapisz", (dialog, which) -> {
            int selectedIndex = spinner.getSelectedItemPosition();
            if (medicines.isEmpty()) return;

            UserMedicine selectedMed = medicines.get(selectedIndex);
            String amountStr = etAmount.getText().toString();
            if(amountStr.isEmpty()) return;
            int amountToTake = Integer.parseInt(amountStr);

            if (selectedMed.currentQuantity >= amountToTake) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String time = String.format("%02d:%02d", hour, minute);
                saveSchedule(selectedMed, amountToTake, time);
            } else {
                Toast.makeText(getContext(), "Za mało tabletek! Masz tylko " + selectedMed.currentQuantity, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Anuluj", null);
        builder.show();
    }

    private void saveSchedule(UserMedicine med, int amount, String time) {
        new Thread(() -> {
            med.currentQuantity -= amount;
            AppDatabase.getInstance(requireContext()).dao().updateUserMedicine(med);

            ScheduleItem item = new ScheduleItem(selectedDate, time, med.name, amount);
            AppDatabase.getInstance(requireContext()).dao().insertSchedule(item);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Zaplanowano!", Toast.LENGTH_SHORT).show();
                loadScheduleForDate();
            });
        }).start();
    }
}