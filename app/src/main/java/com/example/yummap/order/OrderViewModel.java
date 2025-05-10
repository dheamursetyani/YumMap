package com.example.yummap.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.yummap.database.DatabaseClient;
import com.example.yummap.database.DatabaseModel;
import com.example.yummap.database.dao.DatabaseDao;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderViewModel extends AndroidViewModel {

    LiveData<List<DatabaseModel>> modelDatabase;
    DatabaseDao databaseDao;

    //untuk inisialisasi databaseDao
    public OrderViewModel(@NonNull Application application) {
        super(application);

        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
    }

    //untuk mengambil data dari database
    public LiveData<List<DatabaseModel>> getDataIdUser() {
        modelDatabase = databaseDao.getAllOrder();
        return modelDatabase;
    }

    //untuk update data berdasarkan Id secara realtime
    public void addDataOrder(final String strMenu, final int strJmlItems, final int strHarga) {
        Completable.fromAction(() -> {
                    DatabaseModel databaseModel = new DatabaseModel();
                    databaseModel.nama_menu = strMenu;
                    databaseModel.items = strJmlItems;
                    databaseModel.harga = strHarga;
                    databaseDao.insertData(databaseModel);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}
