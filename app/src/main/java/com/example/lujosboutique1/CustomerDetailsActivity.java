package com.example.lujosboutique1;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

public class CustomerDetailsActivity extends AppCompatActivity {

    private TextView customerName, customerStatus, customerSince, totalOrdersText, totalSpentText;
    private TextView customerEmail, customerPhone, customerAddress;
    private ImageView backIcon, moreIcon, emailCopy, phoneCall;
    private Button viewAllOrdersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        initViews();
        loadCustomerData();
        setupClickListeners();
    }

    private void initViews() {
        // Top bar
        backIcon = findViewById(R.id.backIcon);
        moreIcon = findViewById(R.id.moreIcon);

        // Profile section
        customerName = findViewById(R.id.customerName);
        customerStatus = findViewById(R.id.customerStatus);
        customerSince = findViewById(R.id.customerSince);

        // Stats section
        totalOrdersText = findViewById(R.id.totalOrdersText);
        totalSpentText = findViewById(R.id.totalSpentText);

        // Contact section
        customerEmail = findViewById(R.id.customerEmail);
        customerPhone = findViewById(R.id.customerPhone);
        customerAddress = findViewById(R.id.customerAddress);
        emailCopy = findViewById(R.id.emailCopy);
        phoneCall = findViewById(R.id.phoneCall);

        // Orders section
        viewAllOrdersButton = findViewById(R.id.viewAllOrdersButton);
    }

    private void loadCustomerData() {
        // Get customer ID from intent
        String customerId = getIntent().getStringExtra("customer_id");

        // In a real app, you would fetch customer data from your database
        // For now, we'll use sample data
        if (customerId != null) {
            // Sample data - replace with actual data fetching
            customerName.setText("John Doe");
            customerStatus.setText("Active");
            customerSince.setText("Customer since Jan 2024");
            totalOrdersText.setText("12");
            totalSpentText.setText("$1,247.50");
            customerEmail.setText("john.doe@email.com");
            customerPhone.setText("+1 234 567 8900");
            customerAddress.setText("123 Main St, New York, NY 10001");

            // Set status background
            setStatusBackground("Active");
        }
    }

    private void setStatusBackground(String status) {
        if ("Active".equals(status)) {
            customerStatus.setBackgroundResource(R.drawable.status_active_background);
            customerStatus.setTextColor(getColor(R.color.green));
        } else {
            customerStatus.setBackgroundResource(R.drawable.status_inactive_background);
            customerStatus.setTextColor(getColor(R.color.red));
        }
    }

    private void setupClickListeners() {
        // Back button
        backIcon.setOnClickListener(v -> finish());

        // More options menu
        moreIcon.setOnClickListener(v -> showMoreOptions());

        // Copy email
        emailCopy.setOnClickListener(v -> copyToClipboard(customerEmail.getText().toString(), "Email copied to clipboard"));

        // Call phone
        phoneCall.setOnClickListener(v -> makePhoneCall(customerPhone.getText().toString()));

        // View all orders
        viewAllOrdersButton.setOnClickListener(v -> viewAllOrders());

        // Recent order clicks
        findViewById(R.id.order1).setOnClickListener(v -> openOrderDetails("ORD-001"));
        findViewById(R.id.order2).setOnClickListener(v -> openOrderDetails("ORD-002"));
    }

    private void showMoreOptions() {
        PopupMenu popupMenu = new PopupMenu(this, moreIcon);
        popupMenu.getMenuInflater().inflate(R.menu.customer_details_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_edit_customer) {
                editCustomer();
                return true;
            } else if (itemId == R.id.menu_send_email) {
                sendEmail();
                return true;
            } else if (itemId == R.id.menu_block_customer) {
                blockCustomer();
                return true;
            } else if (itemId == R.id.menu_delete_customer) {
                deleteCustomer();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void copyToClipboard(String text, String message) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void makePhoneCall(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        } catch (SecurityException e) {
            Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewAllOrders() {
        // Navigate to orders activity filtered by this customer
        Intent intent = new Intent(this, OrdersActivity.class);
        intent.putExtra("customer_id", getIntent().getStringExtra("customer_id"));
        startActivity(intent);
    }

    private void openOrderDetails(String orderId) {
        // Navigate to order details
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("order_id", orderId);
        startActivity(intent);
    }

    private void editCustomer() {
        // Navigate to edit customer activity
        Intent intent = new Intent(this, EditCustomerActivity.class);
        intent.putExtra("customer_id", getIntent().getStringExtra("customer_id"));
        startActivity(intent);
    }

    private void sendEmail() {
        String email = customerEmail.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        startActivity(Intent.createChooser(intent, "Send email"));
    }

    private void blockCustomer() {
        // Implement block customer logic
        // This would update the customer status in your database
        Toast.makeText(this, "Customer blocked", Toast.LENGTH_SHORT).show();
    }

    private void deleteCustomer() {
        // Implement delete customer logic
        // This would remove the customer from your database
        Toast.makeText(this, "Customer deleted", Toast.LENGTH_SHORT).show();
        finish(); // Go back to customers list
    }
}