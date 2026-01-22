package com.mediscan.offline;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mediscan.offline.database.AppDatabase;
import com.mediscan.offline.database.UserMedicine;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private List<UserMedicine> fullList = new ArrayList<>();
    private EditText etSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.rvMedicines);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        etSearch = view.findViewById(R.id.etSearch);

        loadData();

        etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void loadData() {
        new Thread(() -> {
            fullList = AppDatabase.getInstance(requireContext()).dao().getAllUserMedicines();
            requireActivity().runOnUiThread(() -> {
                adapter = new MedicineAdapter(fullList, medicine -> {
                    Intent intent = new Intent(getContext(), MedicineDetailsActivity.class);
                    intent.putExtra("MED_ID", medicine);
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }

    private void filter(String text) {
        List<UserMedicine> filtered = new ArrayList<>();
        for (UserMedicine item : fullList) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filtered.add(item);
            }
        }
        if (adapter != null) adapter.updateList(filtered);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(); // Odśwież po powrocie ze szczegółów
    }
}