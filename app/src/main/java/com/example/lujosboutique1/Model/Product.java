package com.example.lujosboutique1.Model;

public class Product {
    private int id;
    private String name;
    private double price;
    private String imageUrl;

    public Product(int id, String name, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}
