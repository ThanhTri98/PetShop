package com.example.petmarket2020.Adapters.items;

public class PetCategoryItem {
    private int image;
    private String title;

    public PetCategoryItem(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public PetCategoryItem() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
