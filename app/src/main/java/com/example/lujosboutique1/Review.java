package com.example.lujosboutique1;

public class Review {
    private String userName;
    private String userAvatar;
    private float rating;
    private String date;
    private String comment;
    private int helpfulCount;

    public Review(String userName, String userAvatar, float rating, String date, String comment, int helpfulCount) {
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.rating = rating;
        this.date = date;
        this.comment = comment;
        this.helpfulCount = helpfulCount;
    }

    // Getters and setters
    public String getUserName() { return userName; }
    public String getUserAvatar() { return userAvatar; }
    public float getRating() { return rating; }
    public String getDate() { return date; }
    public String getComment() { return comment; }
    public int getHelpfulCount() { return helpfulCount; }
}
