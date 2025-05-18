package com.example.yummap.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseClient {

    private static DatabaseClient mInstance;
    private AppDatabase mAppDatabase;

    private DatabaseClient(Context context) {
        mAppDatabase = Room.databaseBuilder(context, AppDatabase.class, "yummap_db")
                .addMigrations(MIGRATION_1_2)
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Rename tbl_catering to order_table
            database.execSQL("ALTER TABLE tbl_catering RENAME TO order_table");

            // Create user_table
            database.execSQL("CREATE TABLE IF NOT EXISTS user_table (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "email TEXT, " +
                    "username TEXT, " +
                    "password TEXT)");
        }
    };
}