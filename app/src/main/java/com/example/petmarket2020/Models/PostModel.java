package com.example.petmarket2020.Models;

import java.util.List;

public class PostModel {
    private String postId;
    private String area;
    private boolean isActive;
    private boolean isHidden;
    private double latitude;
    private double longitude;
    private long limitDay;
    private long price;
    private String breed;
    private String peAge;
    private String peType;
    private String poType;
    private String poster;
    private String gender;
    private String timeActive;
    private String title;
    private long viewCounts;
    private String healthGuarantee;
    private String injectStatus;
    private List<String> images;

    public PostModel() {
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPeAge() {
        return peAge;
    }

    public void setPeAge(String peAge) {
        this.peAge = peAge;
    }

    public long getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(long limitDay) {
        this.limitDay = limitDay;
    }

    public String getPeType() {
        return peType;
    }

    public void setPeType(String peType) {
        this.peType = peType;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getPoType() {
        return poType;
    }

    public void setPoType(String poType) {
        this.poType = poType;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTimeActive() {
        return timeActive;
    }

    public void setTimeActive(String timeActive) {
        this.timeActive = timeActive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getViewCounts() {
        return viewCounts;
    }

    public void setViewCounts(long viewCounts) {
        this.viewCounts = viewCounts;
    }

    public String getHealthGuarantee() {
        return healthGuarantee;
    }

    public void setHealthGuarantee(String healthGuarantee) {
        this.healthGuarantee = healthGuarantee;
    }

    public String getInjectStatus() {
        return injectStatus;
    }

    public void setInjectStatus(String injectStatus) {
        this.injectStatus = injectStatus;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
