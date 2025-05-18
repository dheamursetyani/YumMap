package com.example.yummap.history;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.yummap.R;
import com.example.yummap.database.DatabaseModel;
import com.example.yummap.order.OrderActivity;
import com.example.yummap.payment.PaymentActivity;
import com.example.yummap.utils.FunctionHelper;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<DatabaseModel> modelDatabase;
    Context mContext;

    public HistoryAdapter(Context context, List<DatabaseModel> modelInputList) {
        this.mContext = context;
        this.modelDatabase = modelInputList;
    }

    public void setDataAdapter(List<DatabaseModel> items) {
        modelDatabase.clear();
        modelDatabase.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_riwayat, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        final DatabaseModel data = modelDatabase.get(position);

        holder.tvNama.setText(data.getNama_menu());
        holder.tvDate.setText(FunctionHelper.getToday());
        holder.tvJml.setText(data.getItems() + " item");
        holder.tvPrice.setText(FunctionHelper.rupiahFormat(data.getHarga()));
        holder.tvStatus.setText(data.getStatus());

        // Toggle button text and visibility based on payment status
        if (data.isPaid()) {
            holder.btnPayNow.setText("Pesan Lagi");
            if ("makanan sudah sampai".equals(data.getStatus())) {
                holder.btnPayNow.setVisibility(View.GONE); // Hide if already delivered
            } else {
                holder.btnPayNow.setVisibility(View.VISIBLE);
            }
        } else {
            holder.btnPayNow.setText("Bayar Sekarang");
            holder.btnPayNow.setVisibility(View.VISIBLE);
        }

        // Handle button click
        holder.btnPayNow.setOnClickListener(v -> {
            if (data.isPaid()) {
                // Navigate to OrderActivity with preset menu
                Intent intent = new Intent(mContext, OrderActivity.class);
                intent.putExtra("PRESET_MENU", data.getNama_menu());
                mContext.startActivity(intent);
                Toast.makeText(mContext, "Mengisi ulang pesanan: " + data.getNama_menu(), Toast.LENGTH_SHORT).show();
            } else {
                // Navigate to PaymentActivity for unpaid orders
                Intent intent = new Intent(mContext, PaymentActivity.class);
                intent.putExtra(PaymentActivity.EXTRA_MENU, data.getNama_menu());
                intent.putExtra(PaymentActivity.EXTRA_ITEMS, data.getItems());
                intent.putExtra(PaymentActivity.EXTRA_PRICE, data.getHarga());
                intent.putExtra("EXTRA_UID", data.getUid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelDatabase.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNama, tvDate, tvJml, tvPrice, tvStatus;
        public MaterialButton btnPayNow;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvJml = itemView.findViewById(R.id.tvJml);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnPayNow = itemView.findViewById(R.id.btnPayNow);
        }
    }

    public void setSwipeRemove(int position) {
        modelDatabase.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(DatabaseModel databaseModel, int position) {
        modelDatabase.add(position, databaseModel);
        notifyItemInserted(position);
    }

    public List<DatabaseModel> getData() {
        return modelDatabase;
    }
}