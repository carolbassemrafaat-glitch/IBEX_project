package com.example.myapplication1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.data.database.AppDatabase;
import com.example.myapplication1.data.entity.InventoryEntry;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private List<InventoryEntry> inventoryList;
    private AppDatabase db;
    private int currentFileId;
    private InventoryAdapter adapter;

    public InventoryAdapter(List<InventoryEntry> inventoryList) {
        this.inventoryList = inventoryList;
    }
    public void setInventoryList(List<InventoryEntry> list) {
        this.inventoryList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_items, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        InventoryEntry entry = inventoryList.get(position);
//        holder.txtCategory.setText(entry.categoryName);
        holder.txtBarcode.setText("Barcode: " + entry.barcode);
        //holder.txtDescription.setText(entry.itemDesc);
        holder.txtQty.setText("Qty: " + entry.qty);
        holder.txtTotalPrice.setText("Total: " + entry.totalPrice);
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }
    public static class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtBarcode, txtDescription, txtQty, txtTotalPrice;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBarcode = itemView.findViewById(R.id.textBarcode);
            txtQty = itemView.findViewById(R.id.textQty);
            txtTotalPrice = itemView.findViewById(R.id.textTotal);
        }
    }

}
