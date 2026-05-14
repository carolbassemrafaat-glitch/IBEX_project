package com.example.myapplication1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import com.example.myapplication1.data.entity.InventoryEntry;

import java.util.List;

@Dao
public interface InventoryDao {
        @Insert
        void insert(InventoryEntry inventory);

        @Update
        void update(InventoryEntry inventory);

        @Delete
        void delete(InventoryEntry inventory);

        @Query("DELETE FROM inventory")
        void clearAll();

        @Query("SELECT * FROM inventory")
        List<InventoryEntry> getAll();

        @Query("SELECT * FROM inventory WHERE barcode = :barcode LIMIT 1")
        InventoryEntry getByBarcode(String barcode);
        @Query("SELECT * FROM inventory WHERE fileId = :fileId")
        List<InventoryEntry> getAllByFileId(int fileId);
        @Query("SELECT * FROM inventory WHERE barcode = :barcode AND fileId = :fileId LIMIT 1")
        InventoryEntry getByBarcodeAndFile(String barcode, int fileId);

        @Query("DELETE FROM inventory WHERE fileId = :fileId")
        void deleteByFileId(int fileId);

}
