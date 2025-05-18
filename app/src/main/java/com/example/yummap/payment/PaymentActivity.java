package com.example.yummap.payment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.yummap.R;
import com.example.yummap.history.HistoryViewModel;
import com.example.yummap.utils.FunctionHelper;
import com.google.android.material.button.MaterialButton;

public class PaymentActivity extends AppCompatActivity {

    public static final String EXTRA_MENU = "EXTRA_MENU";
    public static final String EXTRA_ITEMS = "EXTRA_ITEMS";
    public static final String EXTRA_PRICE = "EXTRA_PRICE";
    private static final String TAG = "PaymentActivity";

    private TextView tvOrderSummary;
    private EditText etFullName, etAddress;
    private MaterialButton btnCod, btnTransfer, btnQris, btnBankTransfer;
    private LinearLayout llTransferOptions;
    private ImageView ivQris;
    private TextView tvBankDetails;
    private Toolbar toolbar;
    private MaterialButton btnSubmitPayment;
    private String selectedPaymentMethod = null;
    private HistoryViewModel historyViewModel;
    private int orderUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        setInitLayout();
        setOrderSummary();
        setPaymentMethodListeners();
        setSubmitPayment();

        // Initialize ViewModel and get order UID
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        orderUid = getIntent().getIntExtra("EXTRA_UID", -1);
        if (orderUid == -1) {
            Log.e(TAG, "Invalid order UID received");
            Toast.makeText(this, "Gagal memproses pembayaran: Order ID tidak valid", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setInitLayout() {
        toolbar = findViewById(R.id.toolbar);
        tvOrderSummary = findViewById(R.id.tvOrderSummary);
        etFullName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);
        btnCod = findViewById(R.id.btnCod);
        btnTransfer = findViewById(R.id.btnTransfer);
        llTransferOptions = findViewById(R.id.llTransferOptions);
        btnQris = findViewById(R.id.btnQris);
        btnBankTransfer = findViewById(R.id.btnBankTransfer);
        ivQris = findViewById(R.id.ivQris);
        tvBankDetails = findViewById(R.id.tvBankDetails);
        btnSubmitPayment = findViewById(R.id.btnSubmitPayment);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pembayaran");
        }
    }

    private void setOrderSummary() {
        String menu = getIntent().getStringExtra(EXTRA_MENU);
        int items = getIntent().getIntExtra(EXTRA_ITEMS, 0);
        int price = getIntent().getIntExtra(EXTRA_PRICE, 0);

        String summary = "Pesanan: " + menu + "\n" +
                "Jumlah: " + items + " item\n" +
                "Total: " + FunctionHelper.rupiahFormat(price);
        tvOrderSummary.setText(summary);
    }

    private void setPaymentMethodListeners() {
        btnCod.setOnClickListener(v -> {
            selectedPaymentMethod = "COD";
            btnCod.setSelected(true);
            btnTransfer.setSelected(false);
            llTransferOptions.setVisibility(View.GONE);
            ivQris.setVisibility(View.GONE);
            tvBankDetails.setVisibility(View.GONE);
        });

        btnTransfer.setOnClickListener(v -> {
            selectedPaymentMethod = "Transfer";
            btnCod.setSelected(false);
            btnTransfer.setSelected(true);
            llTransferOptions.setVisibility(View.VISIBLE);
        });

        btnQris.setOnClickListener(v -> {
            ivQris.setVisibility(View.VISIBLE);
            tvBankDetails.setVisibility(View.GONE);
        });

        btnBankTransfer.setOnClickListener(v -> {
            ivQris.setVisibility(View.GONE);
            tvBankDetails.setVisibility(View.VISIBLE);
        });
    }

    private void setSubmitPayment() {
        btnSubmitPayment.setOnClickListener(v -> {
            Log.d(TAG, "Submit button clicked for order UID: " + orderUid);
            String fullName = etFullName.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (fullName.isEmpty()) {
                etFullName.setError("Nama lengkap harus diisi");
                return;
            }
            if (address.isEmpty()) {
                etAddress.setError("Alamat harus diisi");
                return;
            }
            if (selectedPaymentMethod == null) {
                Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentDetails = selectedPaymentMethod.equals("Transfer") ?
                    (ivQris.getVisibility() == View.VISIBLE ? "QRIS" : "Transfer Bank") :
                    "COD";

            // Update order status and payment status
            historyViewModel.updateOrderStatus(orderUid, "makanan sudah sampai", true);

            Toast.makeText(this, "Pembayaran berhasil!\n" +
                    "Nama: " + fullName + "\n" +
                    "Alamat: " + address + "\n" +
                    "Metode: " + paymentDetails, Toast.LENGTH_LONG).show();
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}