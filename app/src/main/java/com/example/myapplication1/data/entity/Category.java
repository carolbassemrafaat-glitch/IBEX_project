package com.example.myapplication1.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "categories",
        indices = {
                @Index(value = {"CategoryDesc"}, unique = true)
        }
)
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int categoryID;

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryDesc() {
        return CategoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        CategoryDesc = categoryDesc;
    }

    @ColumnInfo(name = "CategoryDesc")
    public String CategoryDesc;

    public Category() {}

    public Category(@NonNull String categoryDesc) {
        this.CategoryDesc = categoryDesc;
    }

}