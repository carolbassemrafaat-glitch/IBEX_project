package com.example.myapplication1.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FileEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String fileName;
    public String filePath;

    public FileEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
