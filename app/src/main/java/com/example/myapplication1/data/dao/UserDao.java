package com.example.myapplication1.data.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.myapplication1.data.entity.User;

@Dao
public interface UserDao {
    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int isUsernameTaken(String username);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);
}