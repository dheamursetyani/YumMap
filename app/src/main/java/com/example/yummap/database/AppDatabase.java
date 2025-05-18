package com.example.yummap.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.yummap.database.dao.DatabaseDao;
import com.example.yummap.database.dao.UserDao;

@Database(entities = {DatabaseModel.class, UserModel.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DatabaseDao databaseDao();
    public abstract UserDao userDao();
}