package com.example.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.myapplication1.data.database.AppDatabase;
import com.example.myapplication1.data.entity.FileEntity;

import java.util.List;

public class FilesActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView recyclerView;
    private FilesAdapter adapter;
    private List<FileEntity> fileList;
    private EditText editFileName;
    private Button btnAddFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_name);
        db = AppDatabase.getInstance(this);
        editFileName = findViewById(R.id.editFileName);
        btnAddFile = findViewById(R.id.btnAddFile);
        recyclerView = findViewById(R.id.recyclerFiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFiles();
        btnAddFile.setOnClickListener(v -> {
            String fileName = editFileName.getText().toString().trim();
            if (!fileName.isEmpty()) {
                FileEntity file = new FileEntity();
                file.fileName = fileName;
                db.fileDao().insert(file);
                editFileName.setText("");
                loadFiles();
            }
        });
    }
    private void loadFiles() {
        fileList = db.fileDao().getAll();
        adapter = new FilesAdapter(fileList, file -> {
            Intent intent = new Intent(FilesActivity.this, InventoryActivity.class);
            intent.putExtra("id", file.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}

