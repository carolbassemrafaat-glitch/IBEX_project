package com.example.myapplication1.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "Items",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "categoryID",
                childColumns = "categoryID",
                onDelete = CASCADE
        ),
        indices = {
                @Index("categoryID"),
                @Index(value = {"categoryID", "itemDesc"}, unique = true),
                @Index(value = {"barcode"}, unique = true)
        }
)

public class Item {
    @PrimaryKey(autoGenerate = true)
    public int itemId;
    @NonNull
    @ColumnInfo(name = "itemDesc")
    public String itemDesc;
    @NonNull
    public String barcode;

    public Item(int categoryID, String itemDesc, double price, String barcode) {
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @NonNull
    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(@NonNull String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    @ColumnInfo(name = "price")
    public float price;
    @ColumnInfo(name = "categoryID")
    public int categoryID;

    public Item() {
        itemDesc = "";
        barcode = "";
    }

    public Item(@NonNull String itemDesc, float price, int categoryId, @NonNull String barcode) {
        this.itemDesc = itemDesc;
        this.price = price;
        this.categoryID = categoryId;
        this.barcode = barcode;
    }
@NonNull
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}