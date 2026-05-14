package com.example.myapplication1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication1.data.database.AppDatabase;
import com.example.myapplication1.data.dao.CategoryDao;
import com.example.myapplication1.data.dao.ItemDao;
import com.example.myapplication1.data.entity.Category;
import com.example.myapplication1.data.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class Add_Items extends AppCompatActivity {

    private EditText categoryInput, itemDescInput, priceInput ,barcodeInput;
    private Button addButton;
    private AppDatabase db;
    private CategoryDao categoryDao;
    private ItemDao itemDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        initDatabase();
        initViews();
        initListeners();
    }

    private void initDatabase() {
        db = AppDatabase.getInstance(this);
        categoryDao = db.categoryDao();
        itemDao = db.itemDao();
    }

    private void initViews() {
        categoryInput = findViewById(R.id.CategoryName);
        itemDescInput = findViewById(R.id.itemDescription);
        priceInput = findViewById(R.id.price);
        barcodeInput= findViewById(R.id.editBarcode);
        addButton = findViewById(R.id.addItem);
    }

    private void initListeners() {
        addButton.setOnClickListener(v -> addItemFlow());
    }

    private void addItemFlow() {
        String categoryName = categoryInput.getText().toString().trim();
        String itemDesc = itemDescInput.getText().toString().trim();
        String priceText = priceInput.getText().toString().trim();
        String barcode = barcodeInput.getText().toString().trim();

        if (!validateInput(categoryName, itemDesc, priceText ,barcode)) return;

        float price = parsePrice(priceText);
        if (price == -1) return;

        Category category = ensureCategoryExists(categoryName);
        if (category == null) return;

        // ✅ Check uniqueness (barcode + category/itemDesc)
        if (barcodeExists(barcode)) return;
        if (itemExists(category.categoryID, itemDesc ,barcode)) return;

        insertItem(itemDesc, price, category.categoryID, barcode);
        clearInputs();
    }

    private boolean validateInput(String categoryName, String itemDesc, String priceText, String barcode) {
        if (categoryName.isEmpty() || itemDesc.isEmpty() || priceText.isEmpty() || barcode.isEmpty() ) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private float parsePrice(String priceText) {
        try {
            return Float.parseFloat(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Price must be a number", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    private Category ensureCategoryExists(String categoryName) {
        Category category = categoryDao.getByDesc(categoryName);
        if (category == null) {
            categoryDao.insert(new Category(categoryName));
            category = categoryDao.getByDesc(categoryName);
            if (category == null) {
                Toast.makeText(this, "Error creating category", Toast.LENGTH_SHORT).show();
                return null;
            } else {
                Toast.makeText(this, "New category created: " + categoryName, Toast.LENGTH_SHORT).show();
            }
        }
        return category;
    }
    private boolean barcodeExists(String barcode) {
        Item existing = itemDao.getByBarcode(barcode);
        if (existing != null) {
            Toast.makeText(this, "Barcode already exists", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    boolean itemExists(int categoryId, String itemDesc, String barcode) {
        Item existing = itemDao.getByCategoryAndDesc(categoryId, itemDesc, barcode);
        if (existing != null) {
            Toast.makeText(this, "Item already exists in this category with this barcode", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


    void insertItem(String itemDesc, float price, int categoryId,String barcode) {
        Item item = new Item(itemDesc, price, categoryId, barcode);
        itemDao.insert(item);
        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
    }

    private void clearInputs() {
        itemDescInput.setText("");
        priceInput.setText("");
        barcodeInput.setText("");
    }
}
