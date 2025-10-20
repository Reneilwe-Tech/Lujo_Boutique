package com.example.lujosboutique1.Activity;

import android.os.Bundle;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.ViewPager2;

import com.example.lujosboutique1.Adapter.BannerAdapter;
import com.example.lujosboutique1.Adapter.ProductAdapter;
import com.example.lujosboutique1.Model.Product;
import com.example.lujosboutique1.R;
import java.util.ArrayList;
import java.util.List;

public class HomeOverviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ViewPager2 bannerPager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_overview);

        recyclerView = findViewById(R.id.recyclerView);
        bannerPager = findViewById(R.id.bannerImage);
        searchView = findViewById(R.id.searchView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(new ArrayList<>());
        recyclerView.setAdapter(productAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        loadBanners();
        loadProducts("women"); // default

        setupSearch();
    }

    private void loadBanners() {
        new Thread(() -> {
            List<String> banners = ApiHelper.getBanners();
            runOnUiThread(() -> bannerPager.setAdapter(new BannerAdapter(banners)));
        }).start();
    }

    private void loadProducts(String category) {
        new Thread(() -> {
            List<Product> products = ApiHelper.getProducts(category);
            runOnUiThread(() -> productAdapter.updateData(products));
        }).start();
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void women(android.view.View v) { loadProducts("women"); }
    public void Men(android.view.View v) { loadProducts("men"); }
    public void kids(android.view.View v) { loadProducts("kids"); }
    public void beauty(android.view.View v) { loadProducts("beauty"); }
    public void home(android.view.View v) { loadProducts("home"); }
}
