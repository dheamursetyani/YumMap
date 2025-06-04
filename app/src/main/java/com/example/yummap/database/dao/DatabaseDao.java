package com.example.yummap.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.yummap.database.DatabaseModel;

import java.util.List;

@Dao
public interface DatabaseDao {

    // Get all orders, sorted by order_date in descending order
    @Query("SELECT * FROM order_table ORDER BY order_date DESC")
    LiveData<List<DatabaseModel>> getAllOrder();

    // Get order by ID
    @Query("SELECT * FROM order_table WHERE uid = :uid")
    DatabaseModel getOrderById(int uid);

    // Insert order
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(DatabaseModel model);

    // Update order
    @Update
    void updateData(DatabaseModel model);

    // Delete order by ID
    @Query("DELETE FROM order_table WHERE uid = :uid")
    void deleteSingleData(int uid);
}