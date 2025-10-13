package com.example.lujosboutique1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// This activity corresponds to the layout file activity_product_viewer.xml
public class activity_product_viewer extends AppCompatActivity {

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
        // Set the content view to your XML layout file (R.layout.activity_product_viewer)
        setContentView(R.layout.activity_product_viewer);

        // 1. Initialize views using their IDs
        backArrow = findViewById(R.id.back_arrow);
        productGridRecyclerView = findViewById(R.id.product_grid_recycler_view);
        bottomNav = findViewById(R.id.bottom_nav);

        // Find the LinearLayout containing the sort and filter TextViews
        ViewGroup sortFilterBar = findViewById(R.id.sort_filter_bar);

        // The TextViews don't have IDs, so we access them by their position in the LinearLayout
        if (sortFilterBar.getChildCount() >= 1) {
            // The first child is the "SORT" TextView
            sortTextView = (TextView) sortFilterBar.getChildAt(0);
        }
        if (sortFilterBar.getChildCount() >= 3) { // 3 because there's a View separator in the middle
            // The third child is the "FILTER" TextView
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
                // Logic to show a sorting dialog or bottom sheet
            });
        }
        if (filterTextView != null) {
            filterTextView.setOnClickListener(v -> {
                Toast.makeText(this, "Show Filter Options", Toast.LENGTH_SHORT).show();
                // Logic to show a filtering activity or bottom sheet
            });
        }

        // 4. Setup the RecyclerView
        // Configure the LayoutManager as GridLayoutManager with a spanCount of 2 (as specified in XML)
        productGridRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Set the custom adapter
        ProductGridAdapter adapter = new ProductGridAdapter(productNames);
        productGridRecyclerView.setAdapter(adapter);

        // 5. Setup the Bottom Navigation View listener
        // The menu resource R.menu.bottom_nav_menu needs to exist with defined item IDs.
        bottomNav.setOnItemSelectedListener(item -> {
            // You would use actual R.id.menu_item_name here
            Toast.makeText(ProductGridActivity.this, "Bottom Nav Item Selected", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    // =========================================================================
    // Placeholder RecyclerView Adapter for Products
    // =========================================================================

    // NOTE: This adapter uses a minimal approach since the item_product_card layout is not available.
    // In a real application, you would inflate R.layout.item_product_card inside onCreateViewHolder
    private class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ProductViewHolder> {

        private String[] localDataSet;

        public ProductGridAdapter(String[] dataSet) {
            localDataSet = dataSet;
        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {
            public final TextView productName;
            // public final ImageView productThumbnail; // You'd typically have an image view here

            public ProductViewHolder(View view) {
                super(view);
                // Placeholder TextView for the product name
                productName = new TextView(view.getContext());
                productName.setPadding(16, 16, 16, 16);
                productName.setTextSize(14);
                productName.setBackgroundColor(0xFFEFEFEF); // Light gray background

                ((ViewGroup) view).addView(productName);

                // Set click listener for the whole item
                view.setOnClickListener(v -> {
                    String product = localDataSet[getAdapterPosition()];
                    Toast.makeText(view.getContext(), "View product details for: " + product, Toast.LENGTH_SHORT).show();
                });
            }
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // In a real app, you'd inflate your item layout:
            // View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_card, viewGroup, false);

            // Placeholder: a simple ConstraintLayout container
            View view = new androidx.constraintlayout.widget.ConstraintLayout(viewGroup.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)); // Fixed height for visibility
            view.setPadding(8, 8, 8, 8);

            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder viewHolder, final int position) {
            // Set the product name (simulating data binding)
            viewHolder.productName.setText(localDataSet[position]);
        }

        @Override
        public int getItemCount() {
            return localDataSet.length;
        }
    }
}