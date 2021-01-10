package com.example.petmarket2020.Adapters.items;

import java.util.List;
import java.util.Objects;

public class PosterItem {
    private String postId;
    private List<String> images;
    private String area;
    private double latitude;
    private double longitude;
    private long price;
    private String poType;
    private String title;
    private String timeStart;

    public PosterItem() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getPoType() {
        return poType;
    }

    public void setPoType(String poType) {
        this.poType = poType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PosterItem that = (PosterItem) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0 &&
                price == that.price &&
                postId.equals(that.postId) &&
                area.equals(that.area) &&
                poType.equals(that.poType) &&
                title.equals(that.title) &&
                timeStart.equals(that.timeStart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, area, latitude, longitude, price, poType, title, timeStart);
    }
}
