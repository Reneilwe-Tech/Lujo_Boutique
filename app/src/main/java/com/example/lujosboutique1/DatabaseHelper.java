package com.example.lujosboutique1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LujosBoutique.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_CUSTOMERS = "customers";
    private static final String TABLE_ORDER_ITEMS = "order_items";

    // Common column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_TOTAL = "total";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_CREATED_AT = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tables should already be created, but we define the structure for reference
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades
    }

    // Get total products count
    public int getTotalProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(query, null);

        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return count;
    }

    // Get total orders count
    public int getTotalOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_ORDERS;
        Cursor cursor = db.rawQuery(query, null);

        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return count;
    }

    // Get total revenue
    public double getTotalRevenue() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_TOTAL + ") FROM " + TABLE_ORDERS + " WHERE " + COLUMN_STATUS + " = 'completed'";
        Cursor cursor = db.rawQuery(query, null);

        double revenue = 0.0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                revenue = cursor.getDouble(0);
            }
            cursor.close();
        }
        db.close();
        return revenue;
    }

    // Get total customers count
    public int getTotalCustomers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_CUSTOMERS;
        Cursor cursor = db.rawQuery(query, null);

        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return count;
    }

    // Get dashboard stats all at once
    public DashboardStats getDashboardStats() {
        int products = getTotalProducts();
        int orders = getTotalOrders();
        double revenue = getTotalRevenue();
        int customers = getTotalCustomers();

        return new DashboardStats(products, orders, revenue, customers);
    }
}