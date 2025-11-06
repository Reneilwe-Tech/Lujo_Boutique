package com.example.lujosboutique1;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private LineChart revenueChart;
    private Button todayButton, weeklyButton, monthlyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        setupChart();
        setupTabs();
        loadWeeklyData(); // Default view
    }

    private void setupChart() {
        revenueChart = findViewById(R.id.revenueChart);

        // Basic chart configuration
        revenueChart.getDescription().setEnabled(false);
        revenueChart.getLegend().setEnabled(false);
        revenueChart.setTouchEnabled(true);
        revenueChart.setPinchZoom(true);

        // X-axis configuration
        XAxis xAxis = revenueChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        // Y-axis configuration
        revenueChart.getAxisLeft().setDrawGridLines(false);
        revenueChart.getAxisRight().setEnabled(false);
    }

    private void setupTabs() {
        todayButton = findViewById(R.id.btnToday);
        weeklyButton = findViewById(R.id.btnWeekly);
        monthlyButton = findViewById(R.id.btnMonthly);

        // Set IDs for buttons in your XML if not already set
        // android:id="@+id/todayButton"
        // android:id="@+id/weeklyButton"
        // android:id="@+id/monthlyButton"

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTodayData();
            }
        });

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWeeklyData();
            }
        });

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMonthlyData();
            }
        });
    }

    private void loadTodayData() {
        updateTabColors(todayButton, weeklyButton, monthlyButton);

        List<Entry> entries = new ArrayList<>();
        // Sample today data (last 24 hours)
        entries.add(new Entry(0f, 500f)); // 12 AM
        entries.add(new Entry(4f, 300f)); // 4 AM
        entries.add(new Entry(8f, 800f)); // 8 AM
        entries.add(new Entry(12f, 1200f)); // 12 PM
        entries.add(new Entry(16f, 900f)); // 4 PM
        entries.add(new Entry(20f, 1500f)); // 8 PM
        entries.add(new Entry(23f, 1100f)); // 11 PM

        updateChart(entries, "Hour", "R ");
    }

    private void loadWeeklyData() {
        updateTabColors(weeklyButton, todayButton, monthlyButton);

        List<Entry> entries = new ArrayList<>();
        // Sample weekly data
        entries.add(new Entry(0f, 8500f)); // Mon
        entries.add(new Entry(1f, 10200f)); // Tue
        entries.add(new Entry(2f, 7800f)); // Wed
        entries.add(new Entry(3f, 15600f)); // Thu
        entries.add(new Entry(4f, 13400f)); // Fri
        entries.add(new Entry(5f, 9800f)); // Sat
        entries.add(new Entry(6f, 11200f)); // Sun

        updateChart(entries, "Day", "R ");
    }

    private void loadMonthlyData() {
        updateTabColors(monthlyButton, todayButton, weeklyButton);

        List<Entry> entries = new ArrayList<>();
        // Sample monthly data (4 weeks)
        entries.add(new Entry(0f, 42000f)); // Week 1
        entries.add(new Entry(1f, 58000f)); // Week 2
        entries.add(new Entry(2f, 51000f)); // Week 3
        entries.add(new Entry(3f, 67000f)); // Week 4

        updateChart(entries, "Week", "R ");
    }

    private void updateChart(List<Entry> entries, final String xAxisLabel, final String valuePrefix) {
        LineDataSet dataSet = new LineDataSet(entries, "Revenue");
        dataSet.setColor(ContextCompat.getColor(this, R.color.black));
        dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.darkGray));
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.black));
        dataSet.setCircleRadius(4f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return valuePrefix + String.valueOf((int) value);
            }
        });

        LineData lineData = new LineData(dataSet);
        revenueChart.setData(lineData);

        // Set X-axis formatter based on data type
        revenueChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if ("Hour".equals(xAxisLabel)) {
                    return String.valueOf((int) value) + ":00";
                } else if ("Day".equals(xAxisLabel)) {
                    return getDayName((int) value);
                } else if ("Week".equals(xAxisLabel)) {
                    return "Week " + String.valueOf((int) value + 1);
                } else {
                    return String.valueOf((int) value);
                }
            }
        });

        revenueChart.animateXY(500, 500);
        revenueChart.invalidate();
    }

    private void updateTabColors(Button selected, Button unselected1, Button unselected2) {
        selected.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
        selected.setTextColor(ContextCompat.getColor(this, R.color.white));

        unselected1.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(this, R.color.lightGray)));
        unselected1.setTextColor(ContextCompat.getColor(this, R.color.black));

        unselected2.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(this, R.color.lightGray)));
        unselected2.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    private String getDayName(int dayIndex) {
        switch (dayIndex) {
            case 0: return "Mon";
            case 1: return "Tue";
            case 2: return "Wed";
            case 3: return "Thu";
            case 4: return "Fri";
            case 5: return "Sat";
            case 6: return "Sun";
            default: return "";
        }
    }
}