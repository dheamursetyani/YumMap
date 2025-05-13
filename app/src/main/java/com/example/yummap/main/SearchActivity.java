package com.example.yummap.main;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yummap.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView rvSearchResults;
    private TrendingAdapter searchAdapter;
    private List<ModelTrending> searchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rvSearchResults = findViewById(R.id.rvSearchResults);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setHasFixedSize(true);

        searchResultsList = new ArrayList<>();
        searchAdapter = new TrendingAdapter(this, searchResultsList);
        rvSearchResults.setAdapter(searchAdapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });
    }

    private void performSearch(String query) {
        searchResultsList.clear();
        if (!query.isEmpty()) {
            searchResultsList.add(new ModelTrending(R.drawable.complete_1, "Search Result 1", "Found"));
            searchResultsList.add(new ModelTrending(R.drawable.complete_2, "Search Result 2", "Found"));
        }
        searchAdapter.notifyDataSetChanged();
    }
}