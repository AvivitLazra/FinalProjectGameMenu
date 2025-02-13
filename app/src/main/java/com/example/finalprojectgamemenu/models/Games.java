package com.example.finalprojectgamemenu.models;

public class Games {
    private String gName;
    private int gImage;

    // ✅ חובה: בנאי ריק כדי ש-Firebase יוכל להמיר נתונים לאובייקטים
    public Games() {
        // נדרש על ידי Firebase - לא לשנות
    }

    public Games(int gImage, String gName) {
        this.gImage = gImage;
        this.gName = gName;
    }

    public String getgName() { return gName; }

    public int getgImage() { return gImage; }

    public void setgName(String gName) { this.gName = gName; }

    public void setgImage(int gImage) { this.gImage = gImage; }
}
