package com.example.lujosboutique1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.lujosboutique1.AdminSupportTicketAdapter;
import com.example.lujosboutique1.SupportTicket;
import com.example.lujosboutique1.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminHelpCenterActivity extends AppCompatActivity implements AdminSupportTicketAdapter.OnAdminTicketClickListener {

    private RecyclerView adminTicketsRecyclerView;
    private AdminSupportTicketAdapter adminTicketAdapter;
    private List<SupportTicket> ticketList;
    private List<SupportTicket> filteredList;

    private TextView totalTicketsText, urgentTicketsText, avgResponseTimeText, satisfactionRateText;
    private EditText searchEditText;
    private ChipGroup statusFilterGroup, priorityFilterGroup;
    private Spinner categorySpinner;
    private Button btnAssignToMe, btnBulkAction;
    private View emptyState;

    private List<String> selectedStatuses = new ArrayList<>();
    private List<String> selectedPriorities = new ArrayList<>();
    private String currentSearchQuery = "";
    private boolean isSelectionMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_help_center);

        initViews();
        setupRecyclerView();
        setupCategorySpinner();
        loadTickets();
        setupSearch();
        setupFilterChips();
        setupClickListeners();
        updateAdminStats();
    }

    private void initViews() {
        adminTicketsRecyclerView = findViewById(R.id.adminTicketsRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        statusFilterGroup = findViewById(R.id.statusFilterGroup);
        priorityFilterGroup = findViewById(R.id.priorityFilterGroup);
        categorySpinner = findViewById(R.id.categorySpinner);
        btnAssignToMe = findViewById(R.id.btnAssignToMe);
        btnBulkAction = findViewById(R.id.btnBulkAction);
        emptyState = findViewById(R.id.emptyState);

        totalTicketsText = findViewById(R.id.totalTicketsText);
        urgentTicketsText = findViewById(R.id.urgentTicketsText);
        avgResponseTimeText = findViewById(R.id.avgResponseTimeText);
        satisfactionRateText = findViewById(R.id.satisfactionRateText);
    }

    private void setupRecyclerView() {
        ticketList = new ArrayList<>();
        filteredList = new ArrayList<>();

        adminTicketAdapter = new AdminSupportTicketAdapter(filteredList, this);
        adminTicketsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminTicketsRecyclerView.setAdapter(adminTicketAdapter);
    }

    private void setupCategorySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.support_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void loadTickets() {
        // Sample data - replace with actual API/database calls
        ticketList.clear();

        Calendar cal = Calendar.getInstance();

        // Urgent ticket
        ticketList.add(createSampleTicket("1", "TKT-001234", "John Doe",
                "URGENT: Order not delivered", "My express delivery was supposed to arrive 2 days ago...",
                "Shipping", "High", "Open", cal.getTime(), "Unassigned", 1));

        cal.add(Calendar.HOUR, -1);
        ticketList.add(createSampleTicket("2", "TKT-001235", "Jane Smith",
                "Product damaged upon arrival", "The package arrived with visible damage...",
                "Product Quality", "High", "In Progress", cal.getTime(), "You", 3));

        cal.add(Calendar.DAY_OF_MONTH, -1);
        ticketList.add(createSampleTicket("3", "TKT-001236", "Mike Johnson",
                "Return request", "I would like to return a product that doesn't fit...",
                "Returns", "Medium", "Open", cal.getTime(), "Unassigned", 1));

        cal.add(Calendar.DAY_OF_MONTH, -2);
        ticketList.add(createSampleTicket("4", "TKT-001237", "Sarah Wilson",
                "Payment issue - double charge", "I was charged twice for order #ORD-78901...",
                "Payment", "High", "Open", cal.getTime(), "Unassigned", 1));

        cal.add(Calendar.DAY_OF_MONTH, -3);
        ticketList.add(createSampleTicket("5", "TKT-001238", "David Brown",
                "Question about product availability", "When will the winter collection be available?...",
                "General Inquiry", "Low", "Resolved", cal.getTime(), "Support Team", 5));

        applyFilters();
    }

    private SupportTicket createSampleTicket(String id, String ticketNumber, String customerName,
                                             String subject, String message, String category,
                                             String priority, String status, Date createdAt,
                                             String assignedTo, int messageCount) {
        SupportTicket ticket = new SupportTicket(id, ticketNumber, customerName, subject, message,
                category, priority, status, createdAt);
        ticket.setAssignedTo(assignedTo);
        ticket.setMessageCount(messageCount);
        return ticket;
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFilterChips() {
        // Status filter
        statusFilterGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            selectedStatuses.clear();

            for (int i = 0; i < group.getChildCount(); i++) {
                Chip chip = (Chip) group.getChildAt(i);
                if (chip.isChecked()) {
                    selectedStatuses.add(chip.getText().toString());
                }
            }

            if (selectedStatuses.isEmpty() || selectedStatuses.contains("All")) {
                selectedStatuses.clear();
                selectedStatuses.addAll(Arrays.asList("Open", "In Progress", "Resolved"));
            }

            applyFilters();
        });

        // Priority filter
        priorityFilterGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            selectedPriorities.clear();

            for (int i = 0; i < group.getChildCount(); i++) {
                Chip chip = (Chip) group.getChildAt(i);
                if (chip.isChecked()) {
                    selectedPriorities.add(chip.getText().toString());
                }
            }

            if (selectedPriorities.isEmpty() || selectedPriorities.contains("All")) {
                selectedPriorities.clear();
                selectedPriorities.addAll(Arrays.asList("High", "Medium", "Low"));
            }

            applyFilters();
        });

        // Select default chips
        Chip allStatusChip = findViewById(R.id.chipAllTickets);
        Chip allPriorityChip = findViewById(R.id.chipPriorityAll);
        allStatusChip.setChecked(true);
        allPriorityChip.setChecked(true);
    }

    private void applyFilters() {
        filteredList.clear();
