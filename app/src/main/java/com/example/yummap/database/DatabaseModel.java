package com.example.yummap.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "order_table")
public class DatabaseModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "nama_menu")
    public String nama_menu;

    @ColumnInfo(name = "jml_items")
    public int items;

    @ColumnInfo(name = "harga")
    public int harga;

    @ColumnInfo(name = "status")
    public String status; // e.g., "segera diantar" or "sudah sampai"

    @ColumnInfo(name = "is_paid")
    public boolean isPaid; // true if paid, false otherwise

    @ColumnInfo(name = "order_date")
    public String orderDate; // Changed to String for order date

    public DatabaseModel() {}

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        this.isPaid = paid;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}