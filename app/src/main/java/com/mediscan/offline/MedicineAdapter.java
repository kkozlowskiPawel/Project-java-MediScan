package com.mediscan.offline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mediscan.offline.database.UserMedicine;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.Holder> {
    private List<UserMedicine> list;
    private final OnItemClick listener;

    public interface OnItemClick {
        void onClick(UserMedicine med);
    }

    public MedicineAdapter(List<UserMedicine> list, OnItemClick listener) {
        this.list = list;
        this.listener = listener;
    }

    public void updateList(List<UserMedicine> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        UserMedicine item = list.get(position);
        holder.name.setText(item.name);
        holder.quantity.setText("Ilość: " + item.currentQuantity);
        holder.form.setText(" • " + item.form);
        holder.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView name, quantity, form;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            quantity = itemView.findViewById(R.id.tvQuantity);
            form = itemView.findViewById(R.id.tvForm);
        }
    }
}