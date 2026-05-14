package com.example.myapplication1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication1.data.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories WHERE CategoryDesc = :desc LIMIT 1")
    Category getByDesc(String desc);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Category category);

    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();

    @Query("DELETE FROM categories")
    void deleteAllCategories();

    @Query("SELECT CategoryDesc FROM categories")
    List<String> getAll();

    @Query("SELECT * FROM categories WHERE categoryID=:categoryID")
    Category getById(int categoryID);
}