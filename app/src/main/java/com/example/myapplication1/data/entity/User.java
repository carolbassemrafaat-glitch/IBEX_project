package com.example.myapplication1.data.entity;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "users",
        indices = {@Index(value = "username", unique = true)}
)
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    public String username;
    @NonNull
    public String password;
    public User(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }
    public User() {}
    public String getPassword() {
        return password;
    }
}