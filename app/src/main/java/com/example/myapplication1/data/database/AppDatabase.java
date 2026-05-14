package com.example.myapplication1.data.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication1.data.dao.CategoryDao;
import com.example.myapplication1.data.dao.FileDao;
import com.example.myapplication1.data.dao.InventoryDao;
import com.example.myapplication1.data.dao.ItemDao;
import com.example.myapplication1.data.dao.UserDao;
import com.example.myapplication1.data.entity.FileEntity;
import com.example.myapplication1.data.entity.InventoryEntry;
import com.example.myapplication1.data.entity.User;
import com.example.myapplication1.data.entity.Category;
import com.example.myapplication1.data.entity.Item;

@Database(
        entities = { FileEntity.class ,User.class, Category.class, Item.class , InventoryEntry.class},
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ItemDao itemDao();
    public  abstract FileDao fileDao();
    public abstract InventoryDao inventoryDao();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "my_database"
                            )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
