package com.example.yummap.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.yummap.database.DatabaseClient;
import com.example.yummap.database.UserModel;
import com.example.yummap.database.dao.UserDao;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "LoginViewModel";
    private LiveData<List<UserModel>> userDatabase;
    private UserDao userDao;

    // For initializing userDao
    public LoginViewModel(@NonNull Application application) {
        super(application);
        userDao = DatabaseClient.getInstance(application).getAppDatabase().userDao();
    }

    // For checking login credentials by fetching data from the database
    public LiveData<List<UserModel>> getDataUser(String username, String password) {
        userDatabase = userDao.getUserByName(username, password);
        if (userDatabase.getValue() == null || userDatabase.getValue().isEmpty()) {
            Log.d(TAG, "No user found for username: " + username);
        } else {
            Log.d(TAG, "User found for username: " + username);
        }
        return userDatabase;
    }
}