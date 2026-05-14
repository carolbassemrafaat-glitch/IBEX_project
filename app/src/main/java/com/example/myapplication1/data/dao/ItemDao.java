package com.example.myapplication1.data.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.myapplication1.data.entity.Item;
import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM Items WHERE categoryId = :categoryId AND itemDesc = :desc AND barcode = :barcode LIMIT 1")
    Item getByCategoryAndDesc(int categoryId, String desc, String barcode);
    @Query("SELECT * FROM Items WHERE categoryID = :categoryId")
    List<Item> getItemsByCategory(int categoryId);

    @Query("DELETE FROM Items")
    void deleteAllItems();

    @Query("SELECT * FROM Items")
    List<Item> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Query("SELECT * FROM Items WHERE barcode = :barcode LIMIT 1")
    Item getByBarcode(String barcode);

}
