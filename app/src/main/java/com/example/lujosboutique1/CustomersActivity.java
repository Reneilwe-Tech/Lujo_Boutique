package com.example.lujosboutique1;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lujosboutique1.CustomerAdapter;
import com.example.lujosboutique1.Customer;
import com.example.lujosboutique1.R;
import java.util.ArrayList;
import java.util.List;

public class CustomersActivity extends AppCompatActivity implements CustomerAdapter.OnCustomerClickListener {

    private RecyclerView customersRecyclerView;
    private CustomerAdapter customerAdapter;
    private List<Customer> customerList;
    private List<Customer> filteredList;
    private EditText searchEditText;
    private TextView totalCustomersText;
    private TextView activeCustomersText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        initViews();
        setupRecyclerView();
        loadCustomers();
        setupSearch();
        setupClickListeners();
    }

    private void initViews() {
        customersRecyclerView = findViewById(R.id.customersRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        totalCustomersText = findViewById(R.id.totalCustomersText);
        activeCustomersText = findViewById(R.id.activeCustomersText);
    }

    private void setupRecyclerView() {
        customerList = new ArrayList<>();
        filteredList = new ArrayList<>();

        customerAdapter = new CustomerAdapter(filteredList, this);
        customersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        customersRecyclerView.setAdapter(customerAdapter);
    }

    private void loadCustomers() {
        // Sample data - replace with actual data from your database
        customerList.clear();
        customerList.add(new Customer("1", "John Doe", "john.doe@email.com", "+1 234 567 8900", "Active", 12, "2024-01-15"));
        customerList.add(new Customer("2", "Jane Smith", "jane.smith@email.com", "+1 234 567 8901", "Active", 8, "2024-01-20"));
        customerList.add(new Customer("3", "Mike Johnson", "mike.j@email.com", "+1 234 567 8902", "Inactive", 3, "2024-01-10"));
        customerList.add(new Customer("4", "Sarah Wilson", "sarah.w@email.com", "+1 234 567 8903", "Active", 15, "2024-01-25"));
        customerList.add(new Customer("5", "David Brown", "david.b@email.com", "+1 234 567 8904", "Active", 6, "2024-01-30"));

        filteredList.addAll(customerList);
        customerAdapter.notifyDataSetChanged();

        updateStats();
    }

    private void updateStats() {
        totalCustomersText.setText(String.valueOf(customerList.size()));

        long activeCount = customerList.stream()
                .filter(customer -> "Active".equals(customer.getStatus()))
                .count();
        activeCustomersText.setText(String.valueOf(activeCount));
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCustomers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterCustomers(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(customerList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Customer customer : customerList) {
                if (customer.getName().toLowerCase().contains(lowerCaseQuery) ||
                        customer.getEmail().toLowerCase().contains(lowerCaseQuery) ||
                        customer.getPhone().contains(query)) {
                    filteredList.add(customer);
                }
            }
        }

        customerAdapter.notifyDataSetChanged();
    }

    private void setupClickListeners() {
        findViewById(R.id.backIcon).setOnClickListener(v -> finish());

        findViewById(R.id.searchIcon).setOnClickListener(v -> {
            // Focus on search field when search icon is clicked
            searchEditText.requestFocus();
        });

        findViewById(R.id.filterIcon).setOnClickListener(v -> {
            // Show filter dialog
            showFilterDialog();
        });
    }

    private void showFilterDialog() {
        // Implement filter dialog for status, date range, etc.
        // This is a placeholder for filter functionality
    }

    @Override
    public void onCustomerClick(Customer customer) {
        // Open customer details activity
        Intent intent = new Intent(this, CustomerDetailsActivity.class);
        intent.putExtra("customer_id", customer.getId());
        startActivity(intent);
    }
}