package com.example.yummap.history;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.yummap.database.DatabaseClient;
import com.example.yummap.database.DatabaseModel;
import com.example.yummap.database.dao.DatabaseDao;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HistoryViewModel extends AndroidViewModel {

    private static final String TAG = "HistoryViewModel";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveData<List<DatabaseModel>> modelDatabase;
    DatabaseDao databaseDao;

    // For initializing databaseDao
    public HistoryViewModel(@NonNull Application application) {
        super(application);

        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
        modelDatabase = databaseDao.getAllOrder();
    }

    // For displaying data from database to RecyclerView
    public LiveData<List<DatabaseModel>> getDataList() {
        return modelDatabase;
    }

    // For deleting data by ID in real-time
    public void deleteDataById(final int uid) {
        Disposable disposable = Completable.fromAction(() -> databaseDao.deleteSingleData(uid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d(TAG, "Order deleted for UID: " + uid),
                        throwable -> Log.e(TAG, "Failed to delete order", throwable)
                );
        compositeDisposable.add(disposable);
    }

    // For updating order status and payment status
    public void updateOrderStatus(final int uid, final String status, final boolean isPaid) {
        Disposable disposable = Completable.fromAction(() -> {
                    DatabaseModel databaseModel = databaseDao.getOrderById(uid);
                    if (databaseModel != null) {
                        databaseModel.setStatus(status);
                        databaseModel.setPaid(isPaid);
                        databaseDao.updateData(databaseModel);
                    } else {
                        Log.e(TAG, "Order with UID " + uid + " not found");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d(TAG, "Order status updated for UID: " + uid),
                        throwable -> Log.e(TAG, "Failed to update order status", throwable)
                );
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}