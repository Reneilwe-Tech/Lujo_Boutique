package com.example.lujosboutique1;

public class Customer {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String status;
    private int ordersCount;
    private String joinDate;

    public Customer() {
        // Default constructor required for Firestore
    }

    public Customer(String id, String name, String email, String phone, String status, int ordersCount, String joinDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.ordersCount = ordersCount;
        this.joinDate = joinDate;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getOrdersCount() { return ordersCount; }
    public void setOrdersCount(int ordersCount) { this.ordersCount = ordersCount; }

    public String getJoinDate() { return joinDate; }
    public void setJoinDate(String joinDate) { this.joinDate = joinDate; }
}