package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication1.data.database.AppDatabase;
import com.example.myapplication1.data.entity.Category;
import com.example.myapplication1.data.entity.InventoryEntry;
import com.example.myapplication1.data.entity.Item;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
//import com.honeywell.scanintent.ScanIntent;

public class InventoryActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText editQty;
    private CheckBox checkChangeQty;
    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private List<InventoryEntry> inventoryList = new ArrayList<>();
    private int currentFileId;

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_activitty);

        db = AppDatabase.getInstance(this);

        editQty = findViewById(R.id.editQty);
        checkChangeQty = findViewById(R.id.checkChangeQty);
        recyclerView = findViewById(R.id.recyclerInventory);
        adapter = new InventoryAdapter(inventoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        editQty.setText("1");
        editQty.setEnabled(false);

        checkChangeQty.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editQty.setEnabled(isChecked);
            if (!isChecked) {
                editQty.setText("1");
            }
        });
        currentFileId = getIntent().getIntExtra("id", -1);
        refreshList();
        inventoryList.addAll(db.inventoryDao().getAllByFileId(currentFileId));
        adapter.notifyDataSetChanged();
        editQty.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                int qty = editQty.getText().toString().trim().isEmpty() ? 1 :
                        Integer.parseInt(editQty.getText().toString().trim());
                applyQtyToLastScanned(qty);
                return true;
            }
            return false;
        });
        IntentFilter filter = new IntentFilter("com.honeywell.decode.action");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(barcodeReceiver, filter, String.valueOf(Context.RECEIVER_NOT_EXPORTED), null);
        } else {
            registerReceiver(barcodeReceiver, filter);
        }
    }
    private void addItem(String barcode, int qty) {
        if (barcode.isEmpty()) {
            Toast.makeText(this, "Barcode empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (qty <= 0) {
            Toast.makeText(this, "Qty must be > 0", Toast.LENGTH_SHORT).show();
            return;
        }
        Item item = db.itemDao().getByBarcode(barcode);
        if (item == null) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
            return;
        }
        Category cat = db.categoryDao().getById(item.categoryID);
        for (InventoryEntry entry : inventoryList) {
            if (entry.barcode.equals(barcode)) {
                entry.qty += qty;
                entry.totalPrice = entry.qty * item.price;
                db.inventoryDao().update(entry);
                adapter.notifyDataSetChanged();
                return;
            }
        }
        InventoryEntry newEntry = new InventoryEntry(
                currentFileId,
                cat.getCategoryDesc(),
                item.getBarcode(),
                item.getItemDesc(),
                qty,
                qty * item.getPrice()
        );
        db.inventoryDao().insert(newEntry);
        inventoryList.add(newEntry);
        adapter.notifyItemInserted(inventoryList.size() - 1);
        refreshList();
    }

    private void applyQtyToLastScanned(int qty) {
        if (!inventoryList.isEmpty()) {
            InventoryEntry lastEntry = inventoryList.get(inventoryList.size() - 1);
            lastEntry.qty = qty;
            lastEntry.totalPrice = lastEntry.qty * db.itemDao().getByBarcode(lastEntry.barcode).price;
            db.inventoryDao().update(lastEntry);
            adapter.notifyDataSetChanged();
        }
    }
    private final BroadcastReceiver barcodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.honeywell.decode.action".equals(intent.getAction())) {
                String scannedData = intent.getStringExtra("com.honeywell.decode.data");
                if (scannedData != null && !scannedData.isEmpty()) {
                    int qty = editQty.getText().toString().trim().isEmpty() ? 1 :
                            Integer.parseInt(editQty.getText().toString().trim());
                    addItem(scannedData, qty);
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(barcodeReceiver);
    }
    private void refreshList() {
        List<InventoryEntry> updatedList = db.inventoryDao().getAllByFileId(currentFileId);
        adapter.setInventoryList(updatedList);
    }
}
