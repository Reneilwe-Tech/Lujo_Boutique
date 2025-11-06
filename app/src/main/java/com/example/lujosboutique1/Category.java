package com.example.lujosboutique1;

public class Category {
    private String id;
    private String name;
    private int itemCount;
    private String imageUrl;

    public Category(String id, String name, int itemCount, String imageUrl) {
        this.id = id;
        this.name = name;
        this.itemCount = itemCount;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getItemCount() { return itemCount; }
    public String getImageUrl() { return imageUrl; }
}
