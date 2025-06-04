package com.example.yummap.order;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.yummap.database.DatabaseClient;
import com.example.yummap.database.DatabaseModel;
import com.example.yummap.database.dao.DatabaseDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderViewModel extends AndroidViewModel {

    private static final String TAG = "OrderViewModel";
    LiveData<List<DatabaseModel>> modelDatabase;
    DatabaseDao databaseDao;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
    }

    public LiveData<List<DatabaseModel>> getDataIdUser() {
        modelDatabase = databaseDao.getAllOrder();
        return modelDatabase;
    }

    public void addDataOrder(final String strMenu, final int strJmlItems, final int strHarga) {
        Completable.fromAction(() -> {
            DatabaseModel databaseModel = new DatabaseModel();
            databaseModel.nama_menu = strMenu;
            databaseModel.items = strJmlItems;
            databaseModel.harga = strHarga;
            // Set order date as formatted string in WIB time zone
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
            databaseModel.orderDate = dateFormat.format(new Date());
            databaseModel.status = "segera diantar";
            databaseModel.isPaid = false;
            databaseDao.insertData(databaseModel);
            Log.d(TAG, "Order saved: " + strMenu + ", Date: " + databaseModel.orderDate);
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(() -> Log.d(TAG, "Order insertion completed"),
                throwable -> Log.e(TAG, "Error inserting order", throwable));
    }
}