package com.example.yummap.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yummap.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView rvSearchResults;
    private TrendingAdapter searchAdapter;
    private List<ModelTrending> searchResultsList;
    private List<ModelTrending> menuItems; // Full list of menu items
    private TextView tvNoResults; // Optional: For displaying "No results" message
    private List<ModelCategories> categories; // List of categories
    private Handler handler = new Handler(); // NEW: Handler for debouncing
    private Runnable searchRunnable; // NEW: Runnable for debouncing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize RecyclerView
        rvSearchResults = findViewById(R.id.rvSearchResults);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setHasFixedSize(true);

        // Optional: Initialize TextView for no results message
        tvNoResults = findViewById(R.id.tvNoResults); // Add this to activity_search.xml if needed

        // Initialize menu items dataset from Intent
        menuItems = new ArrayList<>();
        if (getIntent().hasExtra("TRENDING_LIST")) {
            menuItems = (ArrayList<ModelTrending>) getIntent().getSerializableExtra("TRENDING_LIST");
        }
        if (menuItems.isEmpty()) {
            initializeMenuItems(); // Fallback to local initialization
        }

        categories = new ArrayList<>();
        initializeCategories();

        // Initialize search results list and adapter
        searchResultsList = new ArrayList<>();
        searchAdapter = new TrendingAdapter(this, searchResultsList);
        rvSearchResults.setAdapter(searchAdapter);

        // MODIFIED: Set up SearchView with debouncing
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                searchView.clearFocus(); // Clear focus to hide keyboard after submit
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        performSearch(newText);
                    }
                }; // Proper initialization
                handler.postDelayed(searchRunnable, 300); // 300ms delay
                return true;
            }
        });
    }

    // Fallback: Initialize menu items dataset if not passed from MainActivity
    private void initializeMenuItems() {
        // Align with MainActivity's data
        menuItems.add(new ModelTrending(R.drawable.complete_1, "Menu 1", "2.200 disukai", "Bento"));
        menuItems.add(new ModelTrending(R.drawable.complete_2, "Menu 2", "1.220 disukai", "Bento"));
        menuItems.add(new ModelTrending(R.drawable.complete_3, "Menu 3", "345 disukai", "Bento"));
        menuItems.add(new ModelTrending(R.drawable.complete_4, "Menu 4", "590 disukai", "Bento"));
    }

    // Initialize categories dataset
    private void initializeCategories() {
        categories.add(new ModelCategories(R.drawable.ic_complete, "Complete Package"));
        categories.add(new ModelCategories(R.drawable.ic_saving, "Saving Package"));
        categories.add(new ModelCategories(R.drawable.ic_healthy, "Healthy Package"));
        categories.add(new ModelCategories(R.drawable.ic_fast, "FastFood"));
        categories.add(new ModelCategories(R.drawable.ic_event, "Event Packages"));
        categories.add(new ModelCategories(R.drawable.ic_more_food, "Others"));
    }

    // Perform search based on query, including category filtering
    private void performSearch(String query) {
        searchResultsList.clear();
        if (!query.isEmpty()) {
            String queryLowerCase = query.trim().toLowerCase();
            for (ModelTrending item : menuItems) {
                String itemCategory = getCategoryForMenuItem(item.getTvPlaceName());
                if (item.getTvPlaceName().toLowerCase().contains(queryLowerCase) ||
                        item.getTvVote().toLowerCase().contains(queryLowerCase) ||
                        itemCategory.toLowerCase().contains(queryLowerCase)) {
                    searchResultsList.add(item);
                }
            }
        } else {
            // If query is empty, show all menu items
            searchResultsList.addAll(menuItems);
        }

        // Optional: Show/hide no results message
        if (tvNoResults != null) {
            tvNoResults.setVisibility(searchResultsList.isEmpty() && !query.isEmpty() ? View.VISIBLE : View.GONE);
        }

        searchAdapter.notifyDataSetChanged();
    }

    // Helper method to map menu items to categories
    private String getCategoryForMenuItem(String menuName) {
        // Simple mapping for demo purposes
        if (menuName.equals("Menu 1") || menuName.equals("Menu 2")) {
            return "Complete Package";
        } else if (menuName.equals("Menu 3")) {
            return "Saving Package";
        } else if (menuName.equals("Menu 4")) {
            return "Healthy Package";
        }
        return "Others"; // Default category
    }

    // Handle toolbar back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Return to MainActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}