package com.example.yummap.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.yummap.database.dao.DatabaseDao;
import com.example.yummap.database.dao.UserDao;

@Database(entities = {DatabaseModel.class, UserModel.class}, version = 3, exportSchema = false) // Increment version from 2 to 3
public abstract class AppDatabase extends RoomDatabase {
    public abstract DatabaseDao databaseDao();
    public abstract UserDao userDao();

    // Define migration from version 2 to 3
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add the new order_date column to the order_table
            database.execSQL("ALTER TABLE order_table ADD COLUMN order_date INTEGER"); // Store as INTEGER (Unix timestamp)
        }
    };
}