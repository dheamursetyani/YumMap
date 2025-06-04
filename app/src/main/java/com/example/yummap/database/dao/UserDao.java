package com.example.yummap.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.yummap.database.UserModel;

import java.util.List;

@Dao
public interface UserDao {

    // Insert user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserModel user);

    // Get user by username and password for login
    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password")
    LiveData<List<UserModel>> getUserByName(String username, String password);

    // Get all users (if needed)
    @Query("SELECT * FROM user_table")
    LiveData<List<UserModel>> getAllUsers();
}