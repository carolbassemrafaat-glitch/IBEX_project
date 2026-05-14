package com.example.myapplication1.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication1.data.entity.FileEntity;

import java.util.List;

@Dao
public interface FileDao {
    @Insert
    void insert(FileEntity file);

    @Query("SELECT * FROM FileEntity")
    public List<FileEntity> getAll();

    @Delete
    void delete(FileEntity file);

}
