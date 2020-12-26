package com.example.petmarket2020.Adapters.items;

public class PosterItem {
    private int image;
    private String title;
    private long price;
    private String address;
    private String date;
    private boolean favorite; // Thả tym
    private boolean isBuy;// Loại tin

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public PosterItem(int image, String title, long price, String address, String date) {
        this.image = image;
        this.title = title;
        this.price = price;
        this.address = address;
        this.date = date;
    }


    public PosterItem() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        String buy = "[MUA] ";
        if (!isBuy) {
            buy = "[BÁN] ";
        }
        return buy + this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
