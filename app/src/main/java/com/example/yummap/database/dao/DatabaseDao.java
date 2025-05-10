package com.example.yummap.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.yummap.database.DatabaseModel;

import java.util.List;

@Dao
public interface DatabaseDao {

    //untuk get semua data
    @Query("SELECT * FROM tbl_catering where uid != 1")
    LiveData<List<DatabaseModel>> getAllOrder();

    //untuk login
    @Query("SELECT * FROM tbl_catering where username= :username AND password= :password")
    LiveData<List<DatabaseModel>> getUserByName(String username, String password);

    //untuk insert data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(DatabaseModel... modelDatabases);

    //untuk update data
    @Query("UPDATE tbl_catering SET nama_menu= :nama_menu, jml_items= :jml_items, harga= :harga WHERE uid = :uid")
    void updateData(String nama_menu, int jml_items, int harga, int uid);

    //untuk delete data by Id
    @Query("DELETE FROM tbl_catering WHERE uid= :uid")
    void deleteSingleData(int uid);
}
