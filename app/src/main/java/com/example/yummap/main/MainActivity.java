package com.example.yummap.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yummap.R;
import com.example.yummap.history.HistoryOrderActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

public class MainActivity extends AppCompatActivity {

    List<ModelCategories> modelCategoriesList = new ArrayList<>();
    List<ModelTrending> modelTrendingList = new ArrayList<>();
    CategoriesAdapter categoriesAdapter;
    TrendingAdapter trendingAdapter;
    ModelCategories modelCategories;
    ModelTrending modelTrending;
    RecyclerView rvCategories, rvTrending;
    CardView cvHistory;
    LinearLayout cvSearch;
    TextView tvGreeting, tvLocation;
    FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setStatusbar();
        setInitLayout();
        setCategories();
        setTrending();
        setGreeting();
        setLocation();
    }

    private void setInitLayout() {
        cvHistory = findViewById(R.id.cvHistory);
        cvSearch = findViewById(R.id.cvSearch);
        tvGreeting = findViewById(R.id.tvGreeting);
        tvLocation = findViewById(R.id.tvLocation);

        cvHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryOrderActivity.class);
            startActivity(intent);
        });

        cvSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("TRENDING_LIST", new ArrayList<>(modelTrendingList)); // Pass trending list
            startActivity(intent);
            // Removed finish() to allow back navigation
        });

        rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new GridLayoutManager(this, 3));
        rvCategories.setHasFixedSize(true);

        rvTrending = findViewById(R.id.rvTrending);
        rvTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTrending.setHasFixedSize(true);
    }

    private void setCategories() {
        modelCategories = new ModelCategories(R.drawable.ic_complete, "Complete Package");
        modelCategoriesList.add(modelCategories);
        modelCategories = new ModelCategories(R.drawable.ic_saving, "Saving Package");
        modelCategoriesList.add(modelCategories);
        modelCategories = new ModelCategories(R.drawable.ic_healthy, "Healthy Package");
        modelCategoriesList.add(modelCategories);
        modelCategories = new ModelCategories(R.drawable.ic_fast, "FastFood");
        modelCategoriesList.add(modelCategories);
        modelCategories = new ModelCategories(R.drawable.ic_event, "Event Packages");
        modelCategoriesList.add(modelCategories);
        modelCategories = new ModelCategories(R.drawable.ic_more_food, "Others");
        modelCategoriesList.add(modelCategories);

        categoriesAdapter = new CategoriesAdapter(this, modelCategoriesList);
        rvCategories.setAdapter(categoriesAdapter);
    }

    private void setTrending() {
        modelTrending = new ModelTrending(R.drawable.chicken_teriyaki_bento, "Chicken Teriyaki Bento", "956 disukai", "Bento");
        modelTrendingList.add(modelTrending);
        modelTrending = new ModelTrending(R.drawable.udang_keju, "Udang Keju", "475 disukai", "Saving Food");
        modelTrendingList.add(modelTrending);
        modelTrending = new ModelTrending(R.drawable.kebab, "Kebab", "345 disukai", "Healthy Food");
        modelTrendingList.add(modelTrending);
        modelTrending = new ModelTrending(R.drawable.samyang, "Samyang Roll", "590 disukai", "Fast Food");
        modelTrendingList.add(modelTrending);
        modelTrending = new ModelTrending(R.drawable.mochi, "Mochi", "330 disukai", "Event Food");
        modelTrendingList.add(modelTrending);
        modelTrending = new ModelTrending(R.drawable.tiramisu, "Tiramisu", "300 disukai", "Event Food");
        modelTrendingList.add(modelTrending);
        modelTrending = new ModelTrending(R.drawable.shrip_rpr, "Shrimp Roll", "330 disukai", "Healthy Food");
        modelTrendingList.add(modelTrending);

        trendingAdapter = new TrendingAdapter(this, modelTrendingList);
        rvTrending.setAdapter(trendingAdapter);
    }

    private void setGreeting() {
        String username = getIntent().getStringExtra("USERNAME");
        if (username == null || username.isEmpty()) {
            username = "User";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String greeting;

        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon";
        } else if (hour >= 17 && hour < 18) {
            greeting = "Good Evening";
        } else {
            greeting = "Good Night";
        }

        tvGreeting.setText(greeting + ", " + username + ".");
    }

    private void setLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fetchLocation();
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(this, location -> {
            if (location != null) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        String addressLine = address.getAddressLine(0);
                        if (addressLine != null) {
                            tvLocation.setText(addressLine);
                        } else {
                            tvLocation.setText("Lokasi: " + address.getLocality());
                        }
                    } else {
                        tvLocation.setText("Tidak dapat menemukan lokasi");
                    }
                } catch (IOException e) {
                    tvLocation.setText("Error: Tidak dapat mengambil lokasi");
                }
            } else {
                tvLocation.setText("Lokasi tidak tersedia");
            }
        }).addOnFailureListener(this, e -> {
            tvLocation.setText("Gagal mengambil lokasi");
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                tvLocation.setText("Izin lokasi ditolak");
            }
        }
    }

    public void setStatusbar() {
        if (Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(@NonNull Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }
}