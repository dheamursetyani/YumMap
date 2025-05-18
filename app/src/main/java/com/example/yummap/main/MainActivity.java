package com.example.yummap.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yummap.R;
import com.example.yummap.history.HistoryOrderActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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
    TextView tvGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusbar();
        setInitLayout();
        setCategories();
        setTrending();
        setGreeting();
    }

    private void setInitLayout() {
        cvHistory = findViewById(R.id.cvHistory);
        cvSearch = findViewById(R.id.cvSearch);
        tvGreeting = findViewById(R.id.tvGreeting);

        cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryOrderActivity.class);
                startActivity(intent);
            }
        });

        cvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
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
        modelTrending = new ModelTrending(R.drawable.complete_1, "Menu 1", "2.200 disukai");
        modelTrendingList.add(modelTrending);
        modelTrending = new ModelTrending(R.drawable.complete_2, "Menu 2", "1.220 disukai");
        modelTrendingList.add(modelTrending);
        modelTrending = new ModelTrending(R.drawable.complete_3, "Menu 3", "345 disukai");
        modelTrendingList.add(modelTrending);
        modelTrending = new ModelTrending(R.drawable.complete_4, "Menu 4", "590 disukai");
        modelTrendingList.add(modelTrending);

        trendingAdapter = new TrendingAdapter(this, modelTrendingList);
        rvTrending.setAdapter(trendingAdapter);
    }

    private void setGreeting() {
        // Get username from Intent
        String username = getIntent().getStringExtra("USERNAME");
        if (username == null || username.isEmpty()) {
            username = "User"; // Fallback if username is not provided
        }

        // Determine time of day (using WIB, UTC+7)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String greeting;

        if (hour >= 5 && hour < 12) {
            greeting = "Good morning";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good afternoon";
        } else if (hour >= 17 && hour < 22) {
            greeting = "Good evening";
        } else {
            greeting = "Good night";
        }

        // Set the greeting text
        tvGreeting.setText(greeting + ", " + username + ".");
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