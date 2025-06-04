package com.example.yummap.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.yummap.database.DatabaseClient;
import com.example.yummap.database.UserModel;
import com.example.yummap.database.dao.UserDao;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterViewModel extends AndroidViewModel {

    private UserDao userDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    // For initializing userDao
    public RegisterViewModel(@NonNull Application application) {
        super(application);
        userDao = DatabaseClient.getInstance(application).getAppDatabase().userDao();
    }

    // For inserting registration data
    public void addDataRegister(final String strEmail, final String strUsername, final String strPassword) {
        Disposable disposable = Completable.fromAction(() -> {
                    UserModel userModel = new UserModel();
                    userModel.setEmail(strEmail);
                    userModel.setUsername(strUsername);
                    userModel.setPassword(strPassword);
                    userDao.insertUser(userModel);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}