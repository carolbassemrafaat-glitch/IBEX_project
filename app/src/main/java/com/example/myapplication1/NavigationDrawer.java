package com.example.myapplication1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.data.dao.CategoryDao;
import com.example.myapplication1.data.dao.ItemDao;
import com.example.myapplication1.data.database.AppDatabase;
import com.example.myapplication1.data.entity.Category;
import com.example.myapplication1.data.entity.FileEntity;
import com.example.myapplication1.data.entity.InventoryEntry;
import com.example.myapplication1.data.entity.Item;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;


public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private Spinner categorySpinner;
    private RecyclerView itemsRecyclerView;
    private ItemAdaptor itemAdapter;
    private ArrayAdapter<String> spinnerAdapter;
    private AppDatabase db;
    private List<String> categoryNames;
    private CategoryDao categoryDao;
    private ItemDao itemDao;
    List<Item> items = new ArrayList<>();
    private String barcode;
    private static final int PICK_CSV_FILE = 1;
    private Spinner fileSpinner;
    private InventoryAdapter inventoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        initDrawer();
        initDatabase();
        initRecyclerView();
        initSpinner();
        initNavigationHeader();
    }

    private void initDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initDatabase() {
        db = AppDatabase.getInstance(this);
        categoryDao = db.categoryDao();
        itemDao = db.itemDao();
    }

    private void initRecyclerView() {
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        inventoryAdapter = new InventoryAdapter(new ArrayList<>());
        itemsRecyclerView.setAdapter(inventoryAdapter);
    }

    private void initSpinner() {
        fileSpinner = findViewById(R.id.fileSpinner);
        List<FileEntity> files = db.fileDao().getAll();
        List<String> fileNames = new ArrayList<>();
        fileNames.add("Select a file");
        for (FileEntity f : files) {
            fileNames.add(f.getFileName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                fileNames
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fileSpinner.setAdapter(spinnerAdapter);
        fileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (inventoryAdapter == null) return;
                if (position == 0) {
                    List<InventoryEntry> allInventory = db.inventoryDao().getAll();
                    inventoryAdapter.setInventoryList(allInventory);
                } else {
                    if (files.isEmpty()) return;
                    FileEntity selectedFile = files.get(position - 1); // shift because of "Select a file"
                    List<InventoryEntry> fileInventory = db.inventoryDao()
                            .getAllByFileId(selectedFile.getId());
                    inventoryAdapter.setInventoryList(fileInventory);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (inventoryAdapter == null) return;
                List<InventoryEntry> allInventory = db.inventoryDao().getAll();
                inventoryAdapter.setInventoryList(allInventory);
            }
        });
        fileSpinner.setOnTouchListener((v, event) -> {
            fileNames.clear();
            fileNames.add("Select a file");
            List<FileEntity> updatedFiles = db.fileDao().getAll();
            for (FileEntity f : updatedFiles) {
                fileNames.add(f.getFileName());
            }
            spinnerAdapter.notifyDataSetChanged();
            return false;
        });
    }

  /*  private void initSpinner() {
        categorySpinner = findViewById(R.id.categorySpinner);
        List<Category> categories = db.categoryDao().getAllCategories();
        categoryNames = new ArrayList<>();
        categoryNames.add("Select a category");
        for (Category c : categories) {
            categoryNames.add(c.CategoryDesc);
        }
        spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        categorySpinner.setOnItemSelectedListener(createCategorySelectionListener());
        categorySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                categoryNames.clear();
                categoryNames.add("Select a category");
                categoryNames.addAll(AppDatabase.getInstance(getApplicationContext()).categoryDao().getAll());
                return false;
            }
        });
    }*/
    private AdapterView.OnItemSelectedListener createCategorySelectionListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleCategorySelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }

    private void handleCategorySelection(int position) {
        String selectedCategory = categoryNames.get(position);
        if (!selectedCategory.equals("Select a category")) {
            Category cat = db.categoryDao().getByDesc(selectedCategory);
            List<Item> items = db.itemDao().getItemsByCategory(cat.categoryID);
            System.out.println("in line " + items.toString());

            if (items == null || items.isEmpty()) {
                Toast.makeText(NavigationDrawer.this, "No items in this category", Toast.LENGTH_SHORT).show();
                items = new ArrayList<>();
            }
            itemAdapter.updateItems(items);
            itemAdapter.notifyDataSetChanged();
        }
    }

    private void initNavigationHeader() {
        String username = getIntent().getStringExtra("USERNAME");
        NavigationView navigationView = findViewById(R.id.navigation_view);
        if (navigationView != null) {
            View headerView = navigationView.getHeaderView(0);
            if (headerView != null) {
                TextView headerUsername = headerView.findViewById(R.id.headerUsername);
                if (headerUsername != null && username != null && !username.isEmpty()) {
                    headerUsername.setText(username);
                }
            }
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_SignUp) {
            startActivity(new Intent(this, SignUp.class));
        } else if (id == R.id.nav_AddItems) {
            startActivity(new Intent(this, Add_Items.class));
        } else if (id == R.id.nav_delete) {
            DeleteAllData();
        } else if (id == R.id.nav_export) {
            exportData();
        } else if (id == R.id.nav_import) {
            openFilePicker();
        } else if (id == R.id.nav_inventory) {
            startActivity(new Intent(this, FilesActivity.class));
        }else if (id == R.id.nav_exportFiles) {
            exportFiles();
        } else if (id == R.id.nav_coutry_activity) {
            startActivity(new Intent(this, UniversitiesActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    void DeleteAllData() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All Data")
                .setMessage("Are you sure you want to delete all categories and items? This cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.categoryDao().deleteAllCategories();
                    db.itemDao().deleteAllItems();
                    Toast.makeText(this, "All categories and items deleted", Toast.LENGTH_SHORT).show();
                    refreshAll(1);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    void exportData() {
        try {
            File exportDir = getExternalFilesDir(null);
            List<Category> categories = db.categoryDao().getAllCategories();
            for (Category c : categories) {
                File file = new File(exportDir, c.CategoryDesc + ".csv");
                FileWriter writer = new FileWriter(file);
                CSVWriter csvWriter = new CSVWriter(writer);
                csvWriter.writeNext(new String[]{"Category", "Item", "Price"});
                List<Item> items = db.itemDao().getItemsByCategory(c.categoryID);
                for (Item i : items) {
                    csvWriter.writeNext(new String[]{c.getCategoryDesc(), i.itemDesc, String.valueOf(i.price)});
                }
                csvWriter.close();
                writer.close();
            }
            Toast.makeText(this, "Files saved in: " + exportDir.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Export failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*"); 
        startActivityForResult(intent, PICK_CSV_FILE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CSV_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            importData(uri);
        }
    }
    private void importData(Uri uri) {
        int inserted = 0;
        int skipped = 0;

        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length < 4) continue;

                String categoryName = columns[0].trim();
                String itemDesc = columns[1].trim();
                String priceText = columns[2].trim();
                String barcode = columns[3].trim();

                double price;
                try {
                    price = Double.parseDouble(priceText);
                } catch (NumberFormatException e) {
                    skipped++;
                    continue;
                }
                ensureCategoryExists(categoryName);
                Category cat = categoryDao.getByDesc(categoryName);
                if (cat == null) {
                    skipped++;
                    continue;
                }
                Item existingByBarcode = itemDao.getByBarcode(barcode);
                Item existingByDesc = itemDao.getByCategoryAndDesc(cat.categoryID, itemDesc,barcode);

                if (existingByBarcode != null || existingByDesc != null) {
                    skipped++;
                    continue;
                }
                Item item = new Item(itemDesc, (float) price, cat.categoryID, barcode);
                itemDao.insert(item);
                inserted++;
            }
            Toast.makeText(this, "Import finished. Added: " + inserted + ", Skipped: " + skipped,
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Import failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private boolean ensureCategoryExists(String categoryName) {
        Category category = categoryDao.getByDesc(categoryName);
        if (category == null) {
            categoryDao.insert(new Category(categoryName));
            category = categoryDao.getByDesc(categoryName);
            if (category == null) {
                Toast.makeText(this, "Error creating category", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Toast.makeText(this, "New category created: " + categoryName, Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
    boolean itemExists(int categoryId, String itemDesc) {
        Item existing = itemDao.getByCategoryAndDesc(categoryId, itemDesc, barcode);
        if (existing != null) {
            Toast.makeText(this, "Item already exists in this category", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    void insertItem(String itemDesc, float price, int categoryId) {
        Item item = new Item(itemDesc, price, categoryId, barcode);
        itemDao.insert(item);
        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
    }
    public void refreshAll(int state) {
        try {
            if (state == 1) {
                categoryNames.clear();
                categoryNames.add("Select a category");
                spinnerAdapter.notifyDataSetChanged();
                itemAdapter.clearAll();
                return;
            }
        } catch (Exception e) {
            Log.e("Refresh", "Failed to refresh", e);
            Toast.makeText(this, "Refresh failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void exportFiles() {
        try {
            File exportDir = getExternalFilesDir(null);
            List<FileEntity> files = db.fileDao().getAll();
            for (FileEntity f : files) {
                File file = new File(exportDir, f.getFileName() + ".csv");
                FileWriter writer = new FileWriter(file);
                CSVWriter csvWriter = new CSVWriter(writer);
                csvWriter.writeNext(new String[]{
                        "Item Barcode", "Item Description", "Item Category", "Scanned Qty", "Total Price"
                });
                List<InventoryEntry> inventoryList = db.inventoryDao().getAllByFileId(f.getId());
                for (InventoryEntry inv : inventoryList) {
                    csvWriter.writeNext(new String[]{
                            inv.getBarcode(),
                            inv.getItemDesc(),
                            inv.categoryName,
                            String.valueOf(inv.qty),
                            String.valueOf(inv.totalPrice)
                    });
                }
                csvWriter.close();
                writer.close();
            }
            Toast.makeText(this, "Files saved in: " + exportDir.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Export failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

