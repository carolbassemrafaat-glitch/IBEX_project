package com.example.myapplication1.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
        (tableName = "inventory")
public class InventoryEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int fileId;
    public String categoryName;
    public String barcode;
    public String itemDesc;

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int qty;
    public double totalPrice;

    public InventoryEntry(int fileId, String categoryName, String barcode,
                           String itemDesc, int qty, double totalPrice) {
        this.fileId = fileId;
        this.categoryName = categoryName;
        this.barcode = barcode;
        this.itemDesc = itemDesc;
        this.qty = qty;
        this.totalPrice = totalPrice;
    }

    public InventoryEntry() {
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


}
