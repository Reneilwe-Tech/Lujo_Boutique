package com.example.lujosboutique1;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeOverviewActivity extends AppCompatActivity {

    private static final String TAG = "HomeOverviewActivity";
    private SearchView searchView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_overview);

        // 1. Initialize UI components
        searchView = findViewById(R.id.searchView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 2. Setup SearchView Listener
        setupSearchView();

        // 3. Setup Bottom Navigation Listener
        setupBottomNavigationView();




//
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search submission
                Toast.makeText(HomeOverviewActivity.this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Search submitted: " + query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    private void setupBottomNavigationView() {




        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();


                if (itemId == R.id.nav_home) {
                    Toast.makeText(HomeOverviewActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_discover) {
                    Toast.makeText(HomeOverviewActivity.this, "Discover selected", Toast.LENGTH_SHORT).show();
                    // Intent discoverIntent = new Intent(HomeOverviewActivity.this, DiscoverActivity.class);
                    // startActivity(discoverIntent);
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    Toast.makeText(HomeOverviewActivity.this, "Cart selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Toast.makeText(HomeOverviewActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
}