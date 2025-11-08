package com.example.lujosboutique1;

public class DashboardStats {
    private int totalProducts;
    private int totalOrders;
    private double totalRevenue;
    private int totalClients;

    public DashboardStats() {
        // Default constructor
    }

    public DashboardStats(int totalProducts, int totalOrders, double totalRevenue, int totalClients) {
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.totalClients = totalClients;
    }

    // Getters and setters
    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(int totalClients) {
        this.totalClients = totalClients;
    }
}