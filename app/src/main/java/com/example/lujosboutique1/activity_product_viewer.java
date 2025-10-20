package com.example.lujosboutique1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem; 
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class ProductViewerActivity extends AppCompatActivity {

    private ImageView backArrow;
    private RecyclerView productGridRecyclerView;
    private BottomNavigationView bottomNav;

    // The two TextViews inside the sort_filter_bar LinearLayout
    private TextView sortTextView;
    private TextView filterTextView;

    // Placeholder data for the grid
    private String[] productNames = {"Product A", "Product B", "Product C", "Product D", "Product E", "Product F", "Product G", "Product H", "Product I", "Product J"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_product_viewer);

        // 1. Initialize views using their IDs
        backArrow = findViewById(R.id.back_arrow);
        productGridRecyclerView = findViewById(R.id.product_grid_recycler_view);
        bottomNav = findViewById(R.id.bottom_nav);


        ViewGroup sortFilterBar = findViewById(R.id.sort_filter_bar);



        if (sortFilterBar != null && sortFilterBar.getChildCount() >= 1) {

            sortTextView = (TextView) sortFilterBar.getChildAt(0);
        }
        if (sortFilterBar != null && sortFilterBar.getChildCount() >= 3) {

            filterTextView = (TextView) sortFilterBar.getChildAt(2);
        }


        // 2. Setup the Header Bar (Back Arrow)
        backArrow.setOnClickListener(v -> {
            // This navigates back to the previous activity or finishes the current one
            onBackPressed();
        });

        // 3. Setup the Sort/Filter Bar listeners
        if (sortTextView != null) {
            sortTextView.setOnClickListener(v -> {
                Toast.makeText(this, "Show Sort Options", Toast.LENGTH_SHORT).show();

            });
        }
        if (filterTextView != null) {
            filterTextView.setOnClickListener(v -> {
                Toast.makeText(this, "Show Filter Options", Toast.LENGTH_SHORT).show();

            });
        }

        // 4. Setup the RecyclerView

        productGridRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Set the custom adapter
        ProductGridAdapter adapter = new ProductGridAdapter(productNames);
        productGridRecyclerView.setAdapter(adapter);

        // 5. Setup the Bottom Navigation View listener

        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Toast.makeText(ProductViewerActivity.this, "Bottom Nav Item Selected: " + item.getTitle(), Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }



    private class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ProductViewHolder> {

        private String[] localDataSet;

        public ProductGridAdapter(String[] dataSet) {
            localDataSet = dataSet;
        }

        // Corrected ProductViewHolder to find views from the inflated item_product_card
        public class ProductViewHolder extends RecyclerView.ViewHolder {
            public final TextView productName;
            // public final ImageView productThumbnail; // Placeholder for image view

            public ProductViewHolder(View view) {
                super(view);












                productName = new TextView(view.getContext());
                productName.setPadding(16, 16, 16, 16);
                productName.setTextSize(14);
                productName.setBackgroundColor(0xFFEFEFEF);
                ((ViewGroup) view).addView(productName, 0);

                // Set click listener for the whole item
                view.setOnClickListener(v -> {
                    // Check if the position is valid before accessing the array
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String product = localDataSet[position];
                        Toast.makeText(view.getContext(), "View product details for: " + product, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_product_card, viewGroup, false);

            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder viewHolder, final int position) {
            // Set the product name
            viewHolder.productName.setText(localDataSet[position]);
        }

        @Override
        public int getItemCount() {
            return localDataSet.length;
        }
    }
}