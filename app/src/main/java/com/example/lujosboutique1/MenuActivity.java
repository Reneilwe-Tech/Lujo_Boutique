package com.example.lujosboutique1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // Replace with your actual layout name

        setupMenuClickListeners();
        setupBackButton();
    }

    private void setupMenuClickListeners() {
        findViewById(R.id.menuDashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDashboard();
            }
        });

        findViewById(R.id.menuProducts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProducts();
            }
        });

        findViewById(R.id.menuRefund).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRefund();
            }
        });

        findViewById(R.id.menuReports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReports();
            }
        });

        findViewById(R.id.menuAnalysis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAnalysis();
            }
        });

        findViewById(R.id.menuReviews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReviews();
            }
        });

        findViewById(R.id.menuOrders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrders();
            }
        });

        findViewById(R.id.menuCustomers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomers();
            }
        });

        findViewById(R.id.menuSalesCalendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSalesCalendar();
            }
        });

        findViewById(R.id.menuReturns).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReturns();
            }
        });
    }


    private void setupBackButton() {
        findViewById(R.id.backIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openDashboard() {
        // Navigate to Dashboard
        startActivity(new Intent(this, AdminDashboardActivity.class));
    }

    private void openProducts() {
        // Navigate to Products
        startActivity(new Intent(this, ProductListingActivity.class));
    }

    private void openRefund() {
        // Navigate to Refund
        startActivity(new Intent(this, RefundActivity.class));
    }

    private void openReports() {
        // Navigate to Reports
        startActivity(new Intent(this, ReportsActivity.class));
    }

    private void openAnalysis() {
        // Navigate to Analysis
        startActivity(new Intent(this, AnalyticsActivity.class));
    }

    private void openReviews() {
        // Navigate to Reviews
        startActivity(new Intent(this, ReviewsActivity.class));
    }

    private void openOrders() {
        // Navigate to Orders
        startActivity(new Intent(this, OrdersActivity.class));
    }

    private void openCustomers() {
        // Navigate to Customers
        startActivity(new Intent(this, CustomersActivity.class));
    }

    private void openSalesCalendar() {
        // Navigate to Sales Calendar
        startActivity(new Intent(this, SalesCalendarActivity.class));
    }

    private void openReturns() {
        // Navigate to Returns
        startActivity(new Intent(this, ReturnRequestActivity.class));
    }
}