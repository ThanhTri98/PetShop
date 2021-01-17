package com.example.petmarket2020.Models;

public class RankingModel {
    private String postId;
    private String userId;
    private long rate;
    private String time;
    private String comment;

    public RankingModel() {
    }

    public RankingModel(String postId, String userId, long rate, String time, String comment) {
        this.postId = postId;
        this.userId = userId;
        this.rate = rate;
        this.time = time;
        this.comment = comment;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
